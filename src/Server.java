import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class Server 
{
	
	public ServerSocket ss;
	public static final int port = 25800;
	private ChatServer cs;
	
	
	/*public static void main(String[] args)
	{
		//Start the server
		new Server();
	}*/
	
	//Starts the server
	public Server()
	{
		this.start();
	}
	
	public ChatServer getCS()
	{
		return this.cs;
	}
	
	public void start()
	{
		try 
		{
			ss = new ServerSocket(Server.port);
			cs = new ChatServer();
			new Thread(cs).start();
			System.out.println("Server created on " + Inet4Address.getLocalHost().getHostAddress() + " port " + Server.port);
		} 
		catch (IOException e)
		{
			System.err.println("Could not initialize the server... Exiting");
			e.printStackTrace();
			return;
		}
		
		System.out.println("Waiting for a client");
		
		//Thread to accept new connections
		Worker worker = new Worker(this);
		new Thread(worker).start();
		
		/*while (true)
		{
			Socket clientSocket = null;
			
			try
			{
				clientSocket = ss.accept();
				cs.addClient(clientSocket);
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}*/
		
	}
	
	/**
	 * @author student
	 * Handles new connections
	 */
	class Worker implements Runnable
	{
		
		private Server server;
		
		public Worker(Server server)
		{
			this.server = server;
		}
		@Override
		public void run() {
			Socket clientSocket = null;
			
			while (true)
			{
				try 
				{
					clientSocket = ss.accept();
					server.getCS().addClient(clientSocket);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				
			}
			
		}
	}

	
}


