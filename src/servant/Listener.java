package servant;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import com.Entidades.Nodo;
import com.Entidades.NodoRF;
import com.Utils.SistemaUtil;

//create client socket
public class Listener implements Runnable {
	private Socket clientSocket;

	public void run() {
		try {
			serverResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void serverResponse() throws Exception {

		// New connection, check for the connection headers
		
		System.out.println("Listener.... OK");

		int bufferSize = 8192;
		while (true) {
			try {
				ServerSocket welcomeSocket = Server.mySocket;

				byte[] header = new byte[18];
				Socket clientSocket = welcomeSocket.accept();
				System.out.println("New connection accepted" + clientSocket.getInetAddress());
				this.clientSocket = clientSocket;
				ByteBuffer bf = ByteBuffer.allocate(bufferSize);
				BufferedInputStream inFromClient = new BufferedInputStream(clientSocket.getInputStream());
				inFromClient.read(header);
				/*for (byte b : header)
					bf.put((byte) b);*/
				MessageHandler handler =new MessageHandler();
				handler.handler(header, clientSocket);
				// clientSocket.close();
			} catch (IOException e) {
				System.out.println("Accept failed at " + this.clientSocket.getPort());
				continue;
			}

		}

	}

}
