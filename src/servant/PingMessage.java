package servant;



public class PingMessage extends Message {

	
	public PingMessage(String type){
		this.id = generateId();
		this.setIp();
		this.setMsg_type("ping");
		if (type == "A")
			this.setTtl((byte)1);
		else if (type == "B")
			this.setTtl(MAX_TTL);
		//this.length = 0;
	}
	
	
	
}
