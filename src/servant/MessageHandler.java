package servant;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.Entidades.Nodo;
import com.Entidades.NodoRF;
import com.Utils.SistemaUtil;
import com.google.common.collect.*;

/**
 * 
 * @author dima
 * CLass that Handle all incoming messages, alanyse them and redirect to methods to responce
 * 
 */

public class MessageHandler extends Message {
	
	
	public static List<Socket> clients = new ArrayList<Socket>();
	List<IncomingMessage> queries = new ArrayList<IncomingMessage>();
	static Multimap<Key, Resourse> keys = ArrayListMultimap.create();

	
	 
	
	
	public List<Socket> getClients() {
		return clients;
	}

	public void handler(byte[] header, Socket clientSocket) throws Exception{
		
		//answer output for testing
		System.out.println("Receiving:");
		/*for (byte b : header)
		{
			System.out.print(b + " ");
		}*/
		        System.out.println();
		
			//clientSocket.getInputStream();
			if (header[0] != (byte)1)
			System.out.println("Wrong Message");
			else {
			switch(header[2]){
				case 0x00: 
					if ((header[1]==(byte) 0x01)) 
						//&& ((header[7]==(byte) 0x00) &&  (header[8]==(byte) 0x00)))
						 {
						System.out.println("Ping A received");
						IncomingMessage ping = new IncomingMessage(header);
						System.out.println("Sending Pong A....");
						reply(new PongAMessage(ping.id, ping.ttl), clientSocket,false);
					}
					else{
						System.out.println("Ping B received");
						IncomingMessage ping = new IncomingMessage(header);
						for (byte b: ping.message())
								System.out.print(b + " ");
						System.out.println();
						System.out.println("Sending Pong B....");
						reply(new PongBMessage(ping.id, ping.ttl, clients, clientSocket), clientSocket,false);
					}
					break;		
				case 0x01:
					if ((header[6]==(byte)0x00) && (header[7]==(byte)0x00)){
						System.out.println("PongA received");
					}
					else{
						System.out.println("PongB received");
						IncomingMessage msg = new IncomingMessage(header);
						
						Map<String, Short> list = msg.parsePong();
					
						
						
						for (Socket s : clients)
						{
							if ((list.containsKey(s.getInetAddress().toString())			
							     && (list.get(s.getInetAddress().toString())
									 == s.getPort())))
									 {
								     list.remove(s.getPort());
								     System.out.println("Duplicated client removed");
									 }
						}
						 try{
						for ( Map.Entry<String, Short> entry : list.entrySet())
						{
							    System.out.println("Connecting to " + entry.getKey() + ":" + entry.getValue());
							    reply(new JoinMessage(), new Socket(entry.getKey(),entry.getValue()),false);
						}
						 	}
						 catch(Exception ex)
						    {
						    	System.out.println(ex.getMessage());
						    }
						
					}
					break;
				case 0x02:
					System.out.println("Bye received");
					clientSocket.close();
					break;
				case 0x03:
					if ((header[6]==(byte) 0x00 &&
				       (header[7]==(byte) 0x00))){
						
						System.out.println("Join request received");
						IncomingMessage msg = new IncomingMessage(header);   
						reply(msg, clientSocket,true);
					}
					
					System.out.println("Join responce received");
					if (header[17] == (byte)0x200)
					{
						System.out.println("Accepted");
						clients.add(clientSocket);
						Servant.isConnected = true;
						NodoRF mynodorf = new NodoRF(Nodo.obtenerInstancia().getDireccion(),Nodo.getInstancia().getPuertopeticion());
						SistemaUtil.reportarTiempo("addnode", "final", mynodorf);
					}
					else 
					{
						System.out.println("Rejected");
						clientSocket.close();
						System.out.println("Connection CLosed");
					}
					break;
				case (byte) 0x80:
				     System.out.println("Query received");

                     NodoRF mynodorf = new NodoRF(Nodo.obtenerInstancia().getDireccion(),Nodo.getInstancia().getPuertopeticion());
		             SistemaUtil.reportarTiempo("addnode", "final", mynodorf);
				     IncomingMessage msg = new IncomingMessage(header);  
				     if(queries.size()>0) {
				     for (IncomingMessage m : queries)
				    	 if (m.id == msg.id){
				    		 System.out.println("Query already exists. Discard");
				    		 break;
				    	 }	 
				    	 else
				    	 {
				    		
				    		 //List<Resourse> res = new ArrayList<Resourse>();
				    		 for (Entry<Key, Collection<Resourse>> k : keys.asMap().entrySet())
				    		 {
				    			 if(k.getKey().getK()[0] == msg.body[0]){
				    				 System.out.println("Key: " + k.getKey().getKey() + " matched");
				    		         
				    		          reply(new QueryHitMessage(msg.id, k.getValue()), clientSocket,true);
				    			 }
				    			 
				    			 
				    		 }
				    		 
				    		 queries.add(msg);
				    	 }
				     }
				     else {
				    	 //List<Resourse> res = new ArrayList<Resourse>();
			    		 for (Entry<Key, Collection<Resourse>> k : keys.asMap().entrySet())
			    		 {
			    			 if(k.getKey().getK()[0] == msg.body[0]){
			    				 System.out.println("Key: " + k.getKey().getKey() + " matched");
			    		         
			    		          reply(new QueryHitMessage(msg.id, k.getValue()), clientSocket,true);
			    			 }
			    			 
			    			 
			    		 }
			    		 
			    		 queries.add(msg);
				    	 
				     }
				     if (msg.ttl > 1)
				     {
				    	 for (Socket s : clients)
				    	 {
				    		 if (s != clientSocket)
				    		 reply(msg, s,false);
				    	 }
				    	// msg.forward(clients);
				     }
				     break;
				case (byte) 0x81:
					 System.out.println("Query hit received");
				     IncomingMessage hit = new IncomingMessage(header);
				     
				     for(IncomingMessage q: queries)
				     {
				    	 if (q.id == hit.id){
				    		 hit.setTtl( (byte) (hit.getTtl() - 1));
				    		 
				    		 for(Socket s: clients){
				    			 
				    		     if((s.getInetAddress().toString() == Helper.longToIp(q.getIp()))
				    		    		 && (s.getPort() == q.PORT)) reply(hit, s,false);
				    		     
				    		    	 
				    		     
				    		 }
				    		 
				    		 reply(hit, new Socket(Helper.longToIp(hit.getIp()),hit.PORT),false);
				     }
				    	 
				    	 }
				     
				     System.out.println("Succes!!!! ");
				     break;
				    
				default: System.out.println("Unknown Message type");			
					}
			}
	}
			

	/**
	 * 	
	 * @param msg
	 * @param client
	 */
	
	public void reply(Message msg, Socket client, boolean noAnswer)
	{
          
		try {
 
            DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());
            DataInputStream input = new DataInputStream(client.getInputStream());
            
            
            for (byte b : msg.message())
			{
				System.out.print(b + " ");
			}
            System.out.println();
            
          outToServer.write(msg.message());
          if(!noAnswer) {
          byte[] answer = new byte[60];
	      input.read(answer);
	      handler(answer, client);
          }
         
	     
			System.out.println();
             }	
         catch(Exception ex)
         {
        	 System.out.println(ex.toString());
         }	
		

		
	}
	
	
	public void reply(Message msg)
	{
		
		
        try{ 
        	
        	Socket client = clients.get(0);
 
            DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());
			DataInputStream	input = new DataInputStream(client.getInputStream());
			
            byte[] answer = new byte[60];
            
            for (byte b : msg.message())
			{
				System.out.print(b + " ");
			}
            
            System.out.println();
          outToServer.write(msg.message());
	      input.read(answer);
	      try {
			handler(answer, client);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
		  System.out.println();
		  
             }	
        catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IndexOutOfBoundsException ex)
        {
        	System.out.println("Connection lost");
        	
        }
		

		
	}
	
	
}
