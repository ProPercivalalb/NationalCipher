package nationalcipher.wip;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class NetworkingTest extends Thread {
	public static String SERVER_NAME = "localhost";
	public static int PORT = 22;
	
	private ServerSocket serverSocket;
	public static List<Socket> clientList;
	
	
	
	public NetworkingTest(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
		clientList = new ArrayList<Socket>();
	}

	@Override
	public void run() {
		while(true) {
			try {
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket client = serverSocket.accept();
				this.clientList.add(client);
				//System.out.println("Just connected to " + server.getRemoteSocketAddress());
				//DataInputStream in = new DataInputStream(server.getInputStream());
				//System.out.println(in.readUTF());
				//DataOutputStream out = new DataOutputStream(server.getOutputStream());
				//out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
				//server.close();
			}
			catch(SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				//break;
			}
			catch(IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
   
	public static class ListChecker extends Thread {
		@Override
		public void run() {
			while(true) {

				for(Socket client : clientList) {
					
					
					try {
						//out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
						System.out.println(client.getRemoteSocketAddress() + " closed? " + client.isClosed() + " connected? " + client.isConnected() + " isreachable? " + client.getInetAddress().isReachable(10000) + " read = " + this.isConnectedToClient(client));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(2000);
				} 
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public boolean isConnectedToClient(Socket client) {
			
			
			return client.isConnected() && !client.isClosed() && this.hasPulse(client);
		}

		private boolean hasPulse(Socket client) {
			try {
				client.getOutputStream().write(1);
				return true;
			} 
			catch(IOException e) {
				return false;
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			Thread t = new NetworkingTest(PORT);
			t.start();
			new ListChecker().start();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}