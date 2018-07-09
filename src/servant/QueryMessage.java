package servant;

import org.apache.commons.lang3.ArrayUtils;

public class QueryMessage extends Message {

	
	
	public QueryMessage(String key){
		this.id = generateId();
		this.setIp();
		this.setTtl((byte)5);
		this.setMsg_type("query");
		this.setBody(new Key(key).getK());
		this.setLength((short)countLenght(this.body));
	}
	
	@Override
	public byte[] getBody(){
		return body;
	}
   
	@Override
	public void setBody(byte[] key){
		
		
		int len = key.length;
		this.body = new byte[len+1];
		
		System.arraycopy(key, 0, this.body, 0,len);
		
		this.body[len] = 0;
		
		
	}
	
	public int countLenght(byte[] body)
	{	
		return body.length;
		
	}
	
	@Override
	public byte[] message()
	{
		byte[] message;
		
		message = ArrayUtils.addAll(super.header(), super.body);
		
		return message;
	}
	
	
}
