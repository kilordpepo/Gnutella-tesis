package servant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;




public class PingWorker implements Runnable
{
    private static final int SLEEP_TIME = 5000;
    //private final Servant servent;
    private MessageHandler handler  = new MessageHandler();
    
    
    public PingWorker()
    {
       
    }
    
    
    
    public PingWorker(Servant servent )
    {
    
    }
    
    public void start()
    {
        Thread thread = new Thread();
        thread.setPriority( Thread.NORM_PRIORITY );
        thread.setDaemon( true );
        thread.start();
    }
    
    public void run()
    {
        
        while( true )
        {
            // Sleep some time...
            try
            {
                Thread.sleep( SLEEP_TIME );
            }
            catch (InterruptedException e)
            {
            }
            
                Message msg = new JoinMessage();
               
                
                DataOutputStream outToServer;
                List<Socket> hosts = handler.getClients();
         
               for (Socket host : hosts){
            	   msg.generateId();
            	   try {
					outToServer = new DataOutputStream(host.getOutputStream());
					 DataInputStream input = new DataInputStream(host.getInputStream());
					outToServer.write(msg.header());
					
					 for (byte b : msg.header())
						{
							System.out.print(b + " ");
						}
					 
			         System.out.println();
			         byte[] answer = new byte[18];
				     input.read(answer);
				     handler.handler(answer, host);
			         
						System.out.println();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               }
            }
        }
    
}