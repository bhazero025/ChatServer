import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client 
{

	private Socket client;
	public static String userName = "USERNAME_NOT_SET: ";
	
	public Client()
	{
		this.searchForServer();
	}
	
	private void searchForServer()
	{
		try 
		{
			while (client == null || !client.isConnected())
			{
				client = new Socket("127.0.0.1", Server.port);
			}
			
			if (client.isConnected())
			{
				System.out.println("Connected to the server");
				new Thread(new ClientReaderThread()).start();
				//chat();
			}
			
		} 
		catch (UnknownHostException e)
		{
		
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			System.out.println("Could not find server...");
			searchForServer();
		}

	}
	
	/**
	 * @deprecated use sendMessage instead
	 * @throws IOException
	 */
	private void chat() throws IOException
	{
		OutputStream os = client.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		Scanner keyboard = new Scanner(System.in);
		//new Thread(new ClientReaderThread()).start();
		while (true)
		{
			String buff = "";
			buff += userName;
			buff += keyboard.nextLine();
			buff += '\n';
			bw.write(buff);
			bw.flush();
		}
	}
	
	public void sendMessage(String message) throws IOException
	{
		OutputStream os = client.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		String buffer = userName;
		buffer += " " + message;
		buffer += '\n';
		bw.write(buffer);
		bw.flush();
	}
	
	
	class ClientReaderThread implements Runnable
	{
		public void run()
		{
			while (true)
			{
				try 
				{
					checkNewMessages();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * 
		 * Reads message from the server
		 * 
		 * @throws IOException
		 */
		public void checkNewMessages() throws IOException
		{
			InputStream is = client.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			if (br.ready())
			{
				String buff = br.readLine();
				ClientGUI.textArea.appendText(buff + "\n");
				System.out.println(buff);
			}
		}
		
	}
}
