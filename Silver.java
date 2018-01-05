import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

//Creating Threads for group server
public class Silver extends Thread
{
	private static String algorithm = "RC4";
	public static final int PORT_NUMBER = 2201 ;
	protected Socket socket;
	//Creating TCP connection for getting connected with group server
	
	private Silver(Socket gsocket) 
	{
		this.socket = gsocket;
		System.out.println("Group Server connected from " + socket.getInetAddress().getHostAddress());
		//starting the thread
		start();
	}
	public void run()
	{
		InputStream in = null;
		PrintWriter out = null;
		try
		{
			in = socket.getInputStream();
			out = new PrintWriter(socket.getOutputStream(),true);
			
			//Creating a buffer for receiving input from the Group Server
			BufferedReader stream=new BufferedReader(new InputStreamReader(in));
			String IP;
			while(true)
			{
				String encIP=stream.readLine();
				System.out.println("Encrypted client IP: "+encIP);
				IP=decrypt(encIP,"key");
				out.println("Please provide client IP that I need to communicate with");
				System.out.println("Decrypted client IP: "+IP);
				//Creating UDP connection to communicate with the client
			    
				DatagramSocket sock = null;
			    int gport=9999;			    
				try 
				{	
					//Marking the Client IP address for sending datagram packets
					InetAddress chost = InetAddress.getByName(IP);					
					sock=new DatagramSocket();				          
				    while(true)
				    {	
				        System.out.println("Current IP address: " + chost.getHostAddress());
				        
				        // deserializing to get product details
				        ObjectInputStream input=new ObjectInputStream(new FileInputStream("pert.txt"));  
						Category d1=(Category)input.readObject();  
						Category d3=(Category)input.readObject();
						Category d4=(Category)input.readObject();
						Category d5=(Category)input.readObject();				
			  			String s2 = "Server : Choose the following product from" + " "+ d1.SNo+" " +d1.Product+" "+d1.Amount+" "+d1.Bonus+"\t"+ d3.SNo+" "+d3.Product+" "
			  			+ d3.Amount+" "+ d3.Bonus+"\t"+d4.SNo+" "+d4.Product+" "+d4.Amount+" "+ d4.Bonus+"\t"+d5.SNo+" "+d5.Product+" "+d5.Amount+" "+d5.Bonus;
			  			System.out.println(s2);
			  			String encS2=encrypt(s2,"key");
			  			System.out.println("Encrypted String: "+encS2);
			  			//String decS2=decrypt(encS2,"key");
			  			//System.out.println("Decrypted: "+decS2);
			            DatagramPacket dp = new DatagramPacket(encS2.getBytes() , encS2.getBytes().length ,chost , gport);
			            
			            sock.send(dp);
			            
			            echo("Sent encrypted data to client");
			            while(true)
			            {	
			            	//receiving client data
			            	byte[] buffer12 = new byte[65536];
			            	DatagramPacket incoming = new DatagramPacket(buffer12, buffer12.length);
			            	sock.receive(incoming);			            				            				          
			            	byte[] data10 = incoming.getData();
			            	String decS2=new String(data10, 0, incoming.getLength());			            	
			            	System.out.println("Encrypted data from client: "+decS2);
			            	s2 = decrypt(decS2,"key");
			            	System.out.println("Decrypted data from client: "+s2);
			            	//performing operation based on client input
			                if(s2.equals("101"))
			                { 			 
			                	String encS101="Enter quantity?";			                	
								String s101 = encrypt(encS101,"key");
								System.out.println("Encrypted data to client: "+s101);
			                    DatagramPacket dpd = new DatagramPacket(s101.getBytes() , s101.getBytes().length , incoming.getAddress() , incoming.getPort());
			                    sock.send(dpd);			                    
			                    sock.receive(incoming);
			                    System.out.println("client entered quantity ");
			                    
			                    byte[] data1 = incoming.getData();
			                    String decS3 = new String(data1, 0, incoming.getLength());
				            	System.out.println("Encrypted data from client: "+decS3);				            	
			                    String s11 = decrypt(decS3,"key");			                     
			                    echo("Decrypted data from client: " + s11);
			                    
			                    //sending details about product and points
			                    
			                    ObjectInputStream input1=new ObjectInputStream(new FileInputStream("pert.txt"));   
								int amt=d1.Amount;
								Integer s12 = Integer.valueOf(s11);
			                    Object s15 = "Total amount and Bonus Point earned is " + (amt * s12);
			                    String encS4=encrypt(s15.toString(),"key");
			                    DatagramPacket dp2 = new DatagramPacket(encS4.getBytes() , encS4.getBytes().length , incoming.getAddress() , incoming.getPort());
			                    sock.send(dp2);
			                    System.out.println(s15);
			                    System.out.println("Encrypted data to Client: "+encS4);
			                    input1.close();
			                }
			                else if(s2.equals("102"))
			                { 
			                	String encS101="Enter quantity?";			                	
								String s101 = encrypt(encS101,"key");
								System.out.println("Encrypted data to client: "+s101);
			                    DatagramPacket dpd = new DatagramPacket(s101.getBytes() , s101.getBytes().length , incoming.getAddress() , incoming.getPort());
			                    sock.send(dpd);			                    
			                    sock.receive(incoming);
			                    System.out.println("client entered quantity ");
			                    
			                    byte[] data1 = incoming.getData();
			                    String decS3 = new String(data1, 0, incoming.getLength());
				            	System.out.println("Encrypted data from client: "+decS3);				            	
			                    String s11 = decrypt(decS3,"key");			                     
			                    echo("Decrypted data from client: " + s11);
			                    
			                    //sending details about product and points
			                    
			                    ObjectInputStream input1=new ObjectInputStream(new FileInputStream("pert.txt"));   
			                    Category s3=(Category)input1.readObject(); 
								int amt=s3.Amount;
								Integer s12 = Integer.valueOf(s11);
			                    Object s15 = "Total amount and Bonus Point earned is " + (amt * s12);
			                    String encS4=encrypt(s15.toString(),"key");
			                    DatagramPacket dp2 = new DatagramPacket(encS4.getBytes() , encS4.getBytes().length , incoming.getAddress() , incoming.getPort());
			                    sock.send(dp2);
			                    System.out.println(s15);
			                    System.out.println("Encrypted data to Client: "+encS4);
			                    input1.close();
			                }
			                else if(s2.equals("103"))
			                { 
			                	String encS101="Enter quantity?";			                	
								String s101 = encrypt(encS101,"key");
								System.out.println("Encrypted data to client: "+s101);
			                    DatagramPacket dpd = new DatagramPacket(s101.getBytes() , s101.getBytes().length , incoming.getAddress() , incoming.getPort());
			                    sock.send(dpd);			                    
			                    sock.receive(incoming);
			                    System.out.println("client entered quantity ");
			                    
			                    byte[] data1 = incoming.getData();
			                    String decS3 = new String(data1, 0, incoming.getLength());
				            	System.out.println("Encrypted data from client: "+decS3);				            	
			                    String s11 = decrypt(decS3,"key");			                     
			                    echo("Decrypted data from client: " + s11);
			                    
			                    //sending details about product and points
			                    
			                    ObjectInputStream input1=new ObjectInputStream(new FileInputStream("pert.txt"));   
			                    Category s4=(Category)input1.readObject(); 
								int amt=s4.Amount;
								Integer s12 = Integer.valueOf(s11);
			                    Object s15 = "Total amount and Bonus Point earned is " + (amt * s12);
			                    String encS4=encrypt(s15.toString(),"key");
			                    DatagramPacket dp2 = new DatagramPacket(encS4.getBytes() , encS4.getBytes().length , incoming.getAddress() , incoming.getPort());
			                    sock.send(dp2);
			                    System.out.println(s15);
			                    System.out.println("Encrypted data to Client: "+encS4);
			                    input1.close();
			                }
			                else if(s2.equals("104"))
			                { 
			                	String encS101="Enter quantity?";			                	
								String s101 = encrypt(encS101,"key");
								System.out.println("Encrypted data to client: "+s101);
			                    DatagramPacket dpd = new DatagramPacket(s101.getBytes() , s101.getBytes().length , incoming.getAddress() , incoming.getPort());
			                    sock.send(dpd);			                    
			                    sock.receive(incoming);
			                    System.out.println("client entered quantity ");
			                    
			                    byte[] data1 = incoming.getData();
			                    String decS3 = new String(data1, 0, incoming.getLength());
				            	System.out.println("Encrypted data from client: "+decS3);				            	
			                    String s11 = decrypt(decS3,"key");			                     
			                    echo("Decrypted data from client: " + s11);
			                    
			                    //sending details about product and points
			                    
			                    ObjectInputStream input1=new ObjectInputStream(new FileInputStream("pert.txt"));   
			                    Category s5=(Category)input1.readObject(); 
								int amt=s5.Amount;
								Integer s12 = Integer.valueOf(s11);
			                    Object s15 = "Total amount and Bonus Point earned is " + (amt * s12);
			                    String encS4=encrypt(s15.toString(),"key");
			                    DatagramPacket dp2 = new DatagramPacket(encS4.getBytes() , encS4.getBytes().length , incoming.getAddress() , incoming.getPort());
			                    sock.send(dp2);
			                    System.out.println(s15);
			                    System.out.println("Encrypted data to Client: "+encS4);
			                    input1.close();
			                }
							else if(s2.equals("no"))	
							{ 
								String s1no = null;
								String encS1no = "Server : Thanks for shopping with us";
								System.out.println(encS1no);
								s1no = encrypt(encS1no,"key");
								System.out.println("Encrypted data to Client: "+s1no);
							    DatagramPacket dp3 = new DatagramPacket(s1no.getBytes() , s1no.getBytes().length , incoming.getAddress() , incoming.getPort());
							    sock.send(dp3);
							    input.close();
							    System.exit(1);
							}		
			                //reiterating if the client wants to continue shopping
							else if(s2.equals("yes"))
							{
								break;
							}
			            }
				    }
				}
				catch (UnknownHostException e) 
				{
				      e.printStackTrace();

				} 
			    catch (ClassNotFoundException e) 
				{							
					e.printStackTrace();
				}
				catch (IOException ex) 
				{
					System.out.println("Unable to get streams from UDP client"+ex);
				} 
				
			}
		}
		catch (IOException ex) 
		{
			System.out.println("Unable to get streams from Group server"+ex);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		finally 
		{
			try 
			{
				in.close();
				out.close();
				socket.close();
			} 
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
	}
	public static String encrypt(String toEncrypt, String key) throws Exception {
	      // create a binary key from the argument key (seed)
	      SecureRandom sr = new SecureRandom(key.getBytes());
	      KeyGenerator kg = KeyGenerator.getInstance(algorithm);
	      kg.init(sr);
	      SecretKey sk = kg.generateKey();
	  
	      // create an instance of cipher
	      Cipher cipher = Cipher.getInstance(algorithm);
	  
	      // initialize the cipher with the key
	      cipher.init(Cipher.ENCRYPT_MODE, sk);
	  
	      // enctypt!
	      byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
	      String enc = new String(encrypted);
	      return enc;
	   }
	  
	   public static String decrypt(String toDecrypt, String key) throws Exception {
	      // create a binary key from the argument key (seed)
	      SecureRandom sr = new SecureRandom(key.getBytes());
	      KeyGenerator kg = KeyGenerator.getInstance(algorithm);
	      kg.init(sr);
	      SecretKey sk = kg.generateKey();
	  
	      // do the decryption with that key
	      Cipher cipher = Cipher.getInstance(algorithm);
	      cipher.init(Cipher.DECRYPT_MODE, sk);
	      byte[] decrypted = cipher.doFinal(toDecrypt.getBytes());
	  
	      return new String(decrypted);
	   }
    public static void main(String args[])
    {
    	System.out.println("Hosting Silver server");
    	ServerSocket SilverServer=null;
    	try 
    	{
    		SilverServer = new ServerSocket(PORT_NUMBER);
			while (true) {
				//opens up for multiple clients to get connected
				new Silver(SilverServer.accept());
			}
		} 
    	catch (IOException ex) 
    	{
			System.out.println("Unable to start server.");
		} 
    	finally 
    	{
			try {
				if (SilverServer != null)
					SilverServer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    }     
    //simple function to echo data to terminal
    public static void echo(String msg)
    {
        System.out.println(msg);
    }
}