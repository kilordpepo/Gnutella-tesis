package servant;



import java.net.Socket;
import java.util.List;
import servant.Helper;

public class Message {



	final public static byte VERSION = 1;
	byte ttl;
	byte msg_type;
	final public static byte RESERVED = 0;
    short PORT = (short) Servant.PORT;
	short inPort;
	short length = 0;    
	int ip;
	int id;
	protected static byte MAX_TTL = 5;
	
	byte[] body = null;
	
	public Message(){
	    }
	    
    public Message(byte ttl, byte msg, short length, int ip, int id){
	    	 this.ttl = ttl;
	    	 this.msg_type = msg;
	    	 this.length = length;
	    	 this.ip = ip;
	    	 this.id = id; 
	    	 this.PORT = 8601;
	    	 
	    	 
	     }
    
   
  
    
    
    public byte[] header() {
    	
    	byte[] head = new byte[16];
   	    head[0] = VERSION;
   	    head[1] = ttl;
   	    head[2] = msg_type;
   	    head[3] = RESERVED;
   	    head[4] = (byte)(PORT & 0xff);
	   	head[5] = (byte)((PORT >> 8) & 0xff);
	   	head[6] = (byte)(length & 0xff);
	   	head[7] = (byte)((length >> 8) & 0xff);	
	   	head[8] =  getIpBytes()[0];
	   	head[9] =  getIpBytes()[1];
	   	head[10] = getIpBytes()[2];
	   	head[11] = getIpBytes()[3];
	   	head[12] = getIdBytes()[0];
	   	head[13] = getIdBytes()[1];
	   	head[14] = getIdBytes()[2];
	   	head[15] = getIdBytes()[3];
    	
    	return head;
    }
    
    
    
    
    
    
    
    

    
	public byte getTtl() {
		return ttl;
	}

	public void setTtl(byte ttl) {
		if(ttl <= MAX_TTL && ttl>=1)
		this.ttl = ttl;	
		else
		this.ttl = 1;
	}

	public byte getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msgType) {
		
		switch (msgType.toLowerCase()){
		case "join": this.msg_type =(byte)0x03;
		break;
		case "bye": this.msg_type = (byte)0x02;
		break;
		case "pong": this.msg_type = (byte)0x01;
		break;
		case "query": this.msg_type = (byte)0x80;
		break;
		case "queryhit": this.msg_type = (byte)0x81;
		break; 
		case "ping": this.msg_type = (byte)0x00;
		break;
		default: this.msg_type = (byte)0x00; //ping by default
		}
			
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public int getIp() {
		return ip;
		
	}
	
	
	public byte[] getBody(){
		return body;
	}
   
	public void setBody(String key){
		
		body = key.getBytes();
	}
	
	public void setBody(byte[] b){
		
		this.body = b;
	}
	
	
	
	public byte[] getIpBytes(){
		
		return Helper.intToBytes(this.ip);
	}
	
	public void setId(byte[] id)
	{
		this.id = Helper.byteArrayToInt(id);
	}
	
	public void setIp() {
		
		this.ip = Helper.ipToInt(Helper.generateIp());
	}
	
	

	public long getId() {
		return id;
	}
	
	public byte[] getIdBytes()
	{
		
		return Helper.intToBytes(this.id);
	}

	public int generateId() {
		
		 int id = 0;
		 id = (int) (getIp()  + (System.currentTimeMillis()/100)/(1 + (Math.random()*1000)));
		 this.id = id;
		 return id;
		 
	}
	
	public void forward(List<Socket> dests)
	{
		
		
	}

	public byte[] message() {
		// TODO Auto-generated method stub
		return header();
	}

	public void setBody(byte b) {
		// TODO Auto-generated method stub
		
	}


}
