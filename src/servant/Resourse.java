package servant;

public class Resourse {
	
	private short id;
	private int value;
	
	
	public Resourse(){
		
		this.id = (short) (1 + (Math.random()*100000));
		this.value = (int) Math.random()/2;
		
	}
	
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}


	public short getId() {
		return id;
	}


	public void setId(short id) {
		this.id = id;
	}
	
	

}
