package servant;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Helper {
	
	
     /**
	 * Byte array to int.
     * Byte arry should be in Big Endian 
     * 
     * @param b the byte
      * @return the int
     */
   public static int byteArrayToInt(byte[] b)
   {
       return  b[3] & 0xFF |
               (b[2] & 0xFF) << 8 |
               (b[1] & 0xFF) << 16 |
               (b[0] & 0xFF) << 24;
   }

  
 /**
    * To bytes in Big Endian format.
    *
    * @param i the i
    * @return the byte[]
    */
   public static byte[] intToBytes(int i)
   {
     byte[] result = new byte[4];

     result[0] = (byte) (i >> 24);
     result[1] = (byte) (i >> 16);
     result[2] = (byte) (i >> 8);
     result[3] = (byte) (i); /* i >> 0 */

     return result;
   }
   
   public static byte[] shortToBytes(short p)
   {
	   byte[] result = new byte[2];
	    result[0] = (byte)(p & 0xff);
	   	result[1] = (byte)((p >> 8) & 0xff);
	   	
	   	return result;
   }
   
   
   
   public static short shortFromBytes(byte[] b)
   {
	   return (short)(b[1] & 0xFF | 
			  (b[0] &0xFF) << 8);
	   
   }
   
   /**
	 * Byte array to int.
    * Byte array should be in Big Endian 
    * 
    * @param b the byte
    * @return the int
    */
  public static int intFromBytes(byte[] b)
  {
      return  b[3] & 0xFF |
              (b[2] & 0xFF) << 8 |
              (b[1] & 0xFF) << 16 |
              (b[0] & 0xFF) << 24;
  }
   
   
   public static int ipToInt(String addr) {
	   	 
   	String[] addrArray = addr.split("\\.");
   	int num = 0;
   	for (int i=0;i<addrArray.length;i++) {
   	long power = 3-i;
   	num += ((Integer.parseInt(addrArray[i])%256 * Math.pow(256,power)));
   	}
   	return num;
   	}
   
   public static String longToIp(long ip) {
	   
		return ((ip >> 24) & 0xFF) + "." 
			+ ((ip >> 16) & 0xFF) + "." 
			+ ((ip >> 8) & 0xFF) + "." 
			+ (ip & 0xFF);
	 
	  }
   
   
   
   public static String generateIp(){
   	
   	String ip = new String();
	    List<String> ips = new ArrayList<String>();
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp())
	                continue;
             
	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	                InetAddress addr = addresses.nextElement();
	                ip = addr.getHostAddress();
	                ips.add(ip);
	                //System.out.println(iface.getDisplayName() + " " + ip);
	                
	            }
	        }
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }
	    
   	
   	return ips.get(0);
   	
   }
   

}
