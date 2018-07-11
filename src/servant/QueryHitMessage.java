package servant;


import java.util.Collection;


import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Iterables;

public class QueryHitMessage extends Message {
	
	
	
	public QueryHitMessage(int id, Collection<Resourse> res){
		
		
		this.id = id;
		this.setIp();
		this.setTtl((byte)5);
		this.setMsg_type("queryHit");
		this.setBody(res);
		this.setLength((short)countLenght(this.body));
		
	}
	
	@Override
	public byte[] getBody(){
		return body;
	}
	
	public int countLenght(byte[] body)
	{	
		return body.length;
		
	}
   
	
	
		public void setBody(Collection<Resourse> res) {
		  
		 int len = res.size();
		
		 
		 this.body = new byte[len*8+4];
		 
		 body[0] = 0;
		 body[1] = (byte)len;
		 body[2] = 0;
		 body[3] = 0;
		 int j = 4;
		 
		 for(int i = len; i==1; i--){
			 
	     byte[] id = new byte[2]; 
		 byte[] value = new byte[4];
		 
		 
		   value = Helper.intToBytes(Iterables.get(res, i-1).getValue());
		   id = Helper.shortToBytes(Iterables.get(res, i-1).getId());
		 
			 body[j] = id[0];
			 body[j+1] = id[1];
			 body[j+2]= 0;
			 body[j+3]= 0;
			 body[j+4]= value[0];
			 body[j+5]= value[1];
			 body[j+6] = value[2];
			 body[j+7] = value[3];
			 j+=8; 
		 }
		}
	
	
	
	@Override
	public byte[] message()
	{
		byte[] message;
		
		message = ArrayUtils.addAll(super.header(), super.body);
		
		return message;
	}

}
