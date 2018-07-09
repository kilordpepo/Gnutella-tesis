package servant;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;






public class Server {

	
  
    public static ServerSocket mySocket = null;

    public static int start() {
		
		Thread thread = new Thread(new TCPHost(Servant.PORT));
		thread.start();
		return 0;
	}

	
	
	
    
 private static class TCPHost implements Runnable {
	
		
	       private int portNumber;
				
			public TCPHost(int port) {
				this.portNumber = port;
			}

			public void run() {
				try {
					TCPHostThread(portNumber);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			
			
			
			
			
			
			/**
			 * TCP server thread.
			 *
			 * @param tcpHostPort the tcp server port
			 */
			private void TCPHostThread(int servantPort) throws Exception {			
				
				System.out.println("Starting server at port " + servantPort);
	
				try {//create server socket
					mySocket = new ServerSocket(servantPort);
					Thread list = new Thread(new Listener());
					list.start();
				} catch (SocketException e) {
					System.out.println("TCP Socket already in use.");
					mySocket.close();
					System.exit(1);
				} catch (IOException e) {
					System.out.println("Failed to start TCP server at " + servantPort);
					mySocket.close();
					System.exit(1);
				}
				
				
				}
				
			
			
	
			
			
 }
			
}
			
			
			




