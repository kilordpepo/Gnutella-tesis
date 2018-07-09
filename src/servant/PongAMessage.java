package servant;

public class PongAMessage extends Message {

	
	
   public PongAMessage(int id, byte ttl){
		
	    this.id = id;
		this.setIp();
		this.setMsg_type("pong");
	    this.setTtl(--ttl);
		//this.length = 0;
		
	}
	
	
}

