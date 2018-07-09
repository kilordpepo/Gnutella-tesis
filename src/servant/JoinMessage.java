package servant;

import org.apache.commons.lang3.ArrayUtils;

public class JoinMessage extends Message {

	
	public JoinMessage(){
		this.id = generateId();
		this.setIp();
		this.setTtl((byte)1);
		this.setMsg_type("join");
		this.PORT = 8601;
	}
	
	public JoinMessage(String responce, int id){
		
		this.id = id;
		this.setIp();
		this.setTtl((byte)1);
		this.setMsg_type("join");
		this.PORT = 8601;
		if(responce.toLowerCase() == "accept")
		this.setBody((byte)0x200);
		
	}
	
	@Override
	public byte[] getBody(){
		return body;
	}
   
	   public void setBody(byte b){
			
		this.body = new byte[2];
		this.body[0] = b;
		
		
	}
	   
	   
	   public byte[] message()
		{
			byte[] message;
			message = ArrayUtils.addAll(this.header(), this.body);
			return message;
		}
     
     
}
