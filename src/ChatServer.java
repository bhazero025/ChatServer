import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer implements Runnable
{

	private volatile ArrayList<Socket> clientList;
	private volatile ArrayList<String> message;
	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;
	
	
	public ChatServer()
	{
		clientList = new ArrayList<>();
		message = new ArrayList<>();
	}
	
	public void addClient(Socket client)
	{
		clientList.add(client);
		System.out.println("Client added to chat " + client);
	}
	
	public void run() 
	{
		new Thread(new ServerMessageThread()).start();
		while (true)
		{
			for (int i = 0; i < clientList.size(); i++)
			{
				try 
				{
					Socket client = clientList.get(i);
					
					//Check if a client is active
					checkIfClientIsActive(client , i);
					
					/*
					 * If there is a message ready read the message
					 * print it to console
					 * and send it to all the other clients
					 */
					if (checkForMessage(client))
					{
						String buffer = br.readLine();
						System.out.println(buffer);
						message.add(buffer);
						sendMessageToAllClients(buffer);
						//ServerGUI.textArea.appendText(buffer + '\n');
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean checkForMessage(Socket client) throws IOException
	{
		//Is this going to generate memory problems?
		//Creating a new isr and br every time?
		//I assume garbage collector should take care
		//from the old ones
		is = client.getInputStream();
		isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
		
		return br.ready();
	}

	//Nao funciona mto bem
	private synchronized void checkIfClientIsActive(Socket client, int index)
	{
		if (!client.isConnected() || !client.isBound())
		{
			System.out.println("Removing clint " + index);
			clientList.remove(index);
		}
	}
	
	/**
	 * Sends message to all the clients connected
	 * @param message
	 * @throws IOException
	 */
	public synchronized void sendMessageToAllClients(String message) throws IOException
	{
		
		for (int i = 0; i < clientList.size(); i++)
		{
			checkIfClientIsActive(clientList.get(i), i);
			OutputStream os = clientList.get(i).getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			message += '\n';
			bw.write(message);
			bw.flush();
			/*
			 * Have to check if i == 0 or else its gonna add a bunch of new lines
			 * into the server window making it look ugly
			 * 
			 */
			if (message.length() > 1 && i == 0)
			{
				ServerGUI.textArea.appendText(message);
			}
			
		}
		
	
	}
	
	

	/**
	 * Thread that sends message to client from stdin
	 * @author student
	 *
	 */
	class ServerMessageThread implements Runnable
	{
		Scanner stdin = new Scanner(System.in);
		public void run()
		{
			while (true)
			{
				try
				{
					String buffer = "SERVER: ";
					buffer += stdin.nextLine();
					buffer += " -- FROM CONSOLE";
					buffer += '\n';
					
					sendMessageToAllClients(buffer);
				}
				catch (IOException e)
				{
					System.err.println("Could not send message to clients.");
					e.printStackTrace();
				}
			}
		}	
	}
	
}
