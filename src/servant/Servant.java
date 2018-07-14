package servant;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import com.Entidades.Nodo;
import com.Entidades.NodoRF;
import com.Utils.SistemaUtil;


public class Servant {
        
	 public static int PORT;
     public static MessageHandler handler = new MessageHandler();
     public static boolean isNodeOn = false;
	 public static boolean isConnected = false;
	 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
	
		System.out.println("Bienvenido a Gnutella!\n");
		System.out.println("Please indicate your port:");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		PORT = Integer.parseInt(input.readLine().split("\\s+")[0]);
		System.out.println("Please indicate the server time address:");
		input = new BufferedReader(new InputStreamReader(System.in));
		SistemaUtil.servidorTiempo = input.readLine().split("\\s+")[0];
		printMenu();
		while (true) {
			
			System.out.print(">> ");
			String in = null;
			try {
				// trim() deletes leading and trailing whitespace
				in = input.readLine().trim(); 
			} catch (IOException e) {
				System.out.println("Cannot parse command, please try again");
				continue;
			}
			//if no input, try again
			if (in.length() == 0) {
				continue;
			}
			
			// This regex ignores whitespace between words
			String[] opt = in.split("\\s+"); 
			try{
			option(opt[0]);
			}
			catch(IndexOutOfBoundsException ex)
			{
				ex.getMessage();
				continue;
			}
			
		}

	}

	
	private static void printMenu() {
    
		
		System.out.println("Commands:\n" +
				" 1 - Start Node\n" +
				" 2 - Search key\n" +
				" 3 - Publish key\n"+
				" 4 - Expole Network\n" +
				" 5 - Show connections\n" +
				" 6 - Bootstrap\n" +
				" 7 - Join node manually\n" +
				" 8 - Send Ping A\n" +
				"9  - Search test key\n" +
	            " 0 - Exit\n");   
		
	}


	private static void option(String opt)
	{
		switch (opt){
		    case "1": if (!isNodeOn){
		    	      System.out.println("Starting Node");
		    	      
			          Server.start();
			          isNodeOn = true;
			          //Thread worker = new Thread(new PingWorker());
			          //worker.start();
			          //worker.run();
		                     }
		              else System.out.println("Node is already on!");
		              break;
		    case "9": if (isNodeOn && isConnected){
		    	      System.out.println("Sending query...");
		    	      
		    	      try {
		    	      handler.reply(new QueryMessage("testKey"));
		    	      }
		    	      catch(Exception ex)
		    	      {
		    	    	  ex.getMessage();
		    	      }
		              
		                      } 
		              else System.out.println("Connect first!");
		              break;
		    case "3": if (isNodeOn && isConnected){
   	         		  BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
   	         			System.out.println("Enter the key:");
   	         				while (true) {
   	         						String key = null;

   	         						try {
   	         							key = input.readLine(); 
   	         						} catch (IOException e) {
   	         							System.out.println("Upps, try again");
   	         							continue;
   	         						}
   	         						//if no input, try again
   	         						if (key.length() == 0) {
   	         							continue;
   	         						}
   	         						//System.out.println("Sending query...");
   	         						
   	         						MessageHandler.keys.put(new Key(key), new Resourse());
   	         						
   	         						System.out.println("Key: " + key + " added");
   	         						break;
   	         				}
   	         
				}
				else System.out.println("Connect first");
         		break;         
		    case "4": if (isNodeOn && isConnected){
		    	      System.out.println("Ping B sending");
		              handler.reply(new PingMessage("B"));  
		               }
		              else System.out.println("Connect first!");
		              break;
		    case "5": System.out.println("Client list:");
		    		  for (Socket s: handler.getClients())
		    	           System.out.println(s.getInetAddress().toString() + ":" + s.getPort());
		    	      break;
		    case "6": if (isNodeOn){
		    	      System.out.println("Entering the network...");
					  try {
					  handler.reply(new JoinMessage(), new Socket("169.254.94.215", 8602));
					  
					   } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							}
		               }
		              else System.out.println("Start Node First");
		              break;
		    case "7": if (isNodeOn){
		    			while (true){
		    				System.out.println("Enter ip and port");
		    				BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		              String ip;
		              int port;
		              try {
		  				// trim() deletes leading and trailing whitespace
		  				ip = input.readLine().trim();
		  	            port = Integer.parseInt(input.readLine().trim());
		  	            NodoRF mynodorf = new NodoRF(Nodo.obtenerInstancia().getDireccion(),Nodo.getInstancia().getPuertopeticion());
						SistemaUtil.reportarTiempo("addnode", "inicio", mynodorf);
		  				System.out.println(ip + ":" + port);
		  				handler.reply(new JoinMessage(), new Socket(ip, port));
		  				  break;
		  				} catch (IOException e) {
		  					System.out.println("Something went wrong, try again");
		  				    break;
		  				} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			}
		    			}
		    			else System.out.println("Start Node First");
			              break;
		    case "8":  	if (isNodeOn && isConnected){
	    	            System.out.println("Ping A sending");
	                    handler.reply(new PingMessage("A")); 
	                    
	                     }
	                    else System.out.println("Connect first!");
	                    break;  
		    case "2": 	if (isNodeOn && isConnected){
		    	         BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		    	         System.out.println("Enter the key:");
		    	        String key = null;
		 
		    	 			try {
		    	 				key = input.readLine(); 
		    	 			} catch (IOException e) {
		    	 				System.out.println("Upps, try again");
		    	 			}
		    	 			 System.out.println("Sending query...");
		    	 			 NodoRF mynodorf;
								try {
									mynodorf = new NodoRF(Nodo.obtenerInstancia().getDireccion(),Nodo.getInstancia().getPuertopeticion());
									 SistemaUtil.reportarTiempo("search", "inicio", mynodorf);
								} catch (NoSuchAlgorithmException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		    			    
					         handler.reply(new QueryMessage(key));
		    	         
		    	         
    					}
    					else System.out.println("Connect first");
	              		break;         
		    case "0": System.exit(0);
		    default: System.out.println("wrong option");
		
		
		}
		
		
	}






	

	}   
	                
	
