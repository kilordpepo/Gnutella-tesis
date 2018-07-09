package servant;

public class Key {

	private byte[] k;
	private String ky;
	
	public Key(String key){
		
		this.setK(key);
	}
	
   public Key(byte[] key){
		
		this.setK(key);
	}

	public byte[] getK() {
		return k;
	}
	
	public String getKey(){
		return ky;
	}

	public void setK(byte[] k) {
		this.k = k;
	}
	
	public void setK(String key)
	{
		this.ky = key;
		this.k = key.getBytes();
		
	}
	
	
	
	
}
