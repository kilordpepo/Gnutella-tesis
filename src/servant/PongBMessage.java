package servant;

import java.net.Socket;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class PongBMessage extends Message {

	
	
	
	  public PongBMessage(int id, byte ttl, List<Socket> dest, Socket client){
			
		    dest.remove(client);   
		  
		    this.id = id;
			this.setIp();
			this.setMsg_type("pong");
		    this.setTtl(--ttl);
            this.setBody(dest);
		    this.setLength((short) this.body.length);
		    
		}
		
	  
	  
	  public void setBody(List<Socket> dest) {
		  
		 int len = 0;
		 int ip=0;
 
		 if(dest.size() >= 5)
			 len = 5;
		 else len = dest.size();
		 
		 this.body = new byte[len*8+4];
		 
		 body[0] = 0;
		 body[1] = (byte)len;
		 body[2] = 0;
		 body[3] = 0;
		 int j = 4;
		 
		 for(int i = len; i==1; i--){
			 
			 byte[] addr = new byte[4];
			 byte[] port = new byte[2];
			 ip = Helper.ipToInt(dest.get(i).getInetAddress().toString());
			 addr = Helper.intToBytes(ip);
			 port = Helper.shortToBytes((short)dest.get(i).getPort());
			 
			 body[j] = addr[0];
			 body[j+1] = addr[1];
			 body[j+2]=addr [2];
			 body[j+3]=addr[3];
			 body[j+4]=port[0];
			 body[j+5]=port[1];
			 body[j+6] = 0;
			 body[j+7] = 0;
			 j+=8; 
		 }
		}
	  
	  @Override
		public byte[] message()
		{
			byte[] message;
			
			message = ArrayUtils.addAll(super.header(), this.body);
			
			return message;
		}
		
}
