package servant;


import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

public class IncomingMessage extends Message {
	
	
	short PORT = 0;
	
	 public IncomingMessage(byte[] msg)
	    {
	    	this.ttl = msg[1];
	    	this.msg_type = msg[2];
	    	byte[] l = {msg[6], msg[7]};
	    	this.length = 1;
	        byte[] p = {msg[4], msg[5]};
	        this.inPort = Helper.shortFromBytes(p);
	        byte[] addr = {msg[8], msg[9], msg[10], msg[11]};
	        this.ip = Helper.intFromBytes(addr);
	        byte[] i = {msg[12], msg[13], msg[14], msg[15]};
	        this.id = Helper.intFromBytes(i);
	        this.body = new byte[this.length];
	        System.arraycopy(msg, 16, this.body, 0,this.length);
	    }
	 
	 
	 
	 public byte[] message()
		{
			byte[] message;		
			message = ArrayUtils.addAll(this.header(), this.body);	
			return message;
		}
	 @Override
	 public void forward(List<Socket> dests)
	 {	
			try {
	 
				for (Socket clientSocket : dests)
				{
	              DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	              //DataInputStream input = new DataInputStream(clientSocket.getInputStream());
	              //  QueryMessage msg = new QueryMessage();
	              outToServer.write(this.message());
				}
	            }	
	         catch(Exception ex)
	         {
	        	 System.out.println(ex.toString());
	         }	
		 
	 }
	 
	 public  Map<String, Short>  parsePong()
	 {
		 Map<String, Short> list = new LinkedHashMap<String, Short>();
		 
		 if ((this.msg_type == 0x01) && (this.length > 0))
		 {
			 byte[] ent = {this.body[0], this.body[1]};
			 int entry = Helper.shortFromBytes(ent);
			 int j = 4;
			 for (int i = 0; i <  entry; i++)
			 {
				 byte[] addr = { body[j],body[j+1],body[j+2],body[j+3] };
				 byte[] p = {body[j+4], body[j+5]};
				 
				 short port = Helper.shortFromBytes(p);
				 int ip = Helper.intFromBytes(addr);
				 
			     String a = Helper.longToIp(ip);
				if (a == "127.0.0.1")
				{
					
					j+=8;
					continue;
				}
				list.put(a, port);
				
				 j += 8;
				 
			 }
			 
			 
			 return list;
		 }
		 else
			 return null;
	 }

	 
}
