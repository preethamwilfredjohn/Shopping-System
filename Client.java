import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Client 
{
	private static String algorithm = "RC4";
	public static void main(String args[]) throws UnknownHostException 
	{
		String host = "10.91.60.78";
		int port = 2007;
		new Client(host, port);
	}
	
	public Client(String host, int port) 
	{
		
		//creating TCP connection with group server
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try 
		{
			String serverHostname = host;
			System.out.println("Connecting to host " + serverHostname + " on port " + port + ".");			
			
			try {
				
				//establishing connection and stream for communication
				echoSocket = new Socket(serverHostname, port);
				out = new PrintWriter(echoSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Unknown host: " + serverHostname);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Unable to get streams from server"+e);
				System.exit(1);
			}
			
			//creating buffer for sending input to server
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));			
			
			//sending user name to group server
			System.out.print("User Name: ");
			String userInput = encrypt(stdIn.readLine(),"key");
			System.out.println("Encrypted User Name: "+userInput);
			out.println(userInput);
			String serverData;
				while((serverData = in.readLine())!=null)
				{
					System.out.println("Encrypted from Server: " + serverData);
					String fromServer=decrypt(serverData,"key");
					System.out.println("Decrypted from Server: " + fromServer);
					
					//sending password to groupserver
					String userInput1=encrypt(stdIn.readLine(),"key");
					System.out.println("Encrypted password: "+userInput1);
					out.println(userInput1);
					String serverData3=in.readLine();
					
					if(serverData3.equals("password match"))
					{
						System.out.println("Sending IP address and points");
						
						//reading points using serialization and sending to group server
						ObjectInputStream input=new ObjectInputStream(new FileInputStream("Points.ser"));
						Points p=(Points)input.readObject();
						Integer poi = Integer.valueOf(p.Points);
						String pt=encrypt(p.Points.toString(),"key");						
						//sending points to group server
						System.out.println(poi);
						System.out.println("Encrypted points: "+pt);
						out.println(pt);
						input.close();
						
						//getting IP address for sending it to category server
						out.println(InetAddress.getLocalHost().getHostAddress()); 
						
						//creating a UDP connection with category server
						DatagramSocket sock = null;
						String s;	
						try
						{
							sock = new DatagramSocket(9999);			            							
							while(true)
							{
								byte[] buffer = new byte[6555];
								DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);			        
								echo("waiting for data from the category server");
								
								//Category server gives details about the products, prices and points
								sock.receive(incoming);
								//System.out.println(incoming);
								byte[] b = incoming.getData(); 			               			                
								s = new String(b, 0, incoming.getLength());
								System.out.println("Encrypted text from Category Server: "+s);
								String DecS=decrypt(s,"key");
								System.out.println("Decrypted Text from Category Server: " );
								System.out.println(DecS);
								
								//creating buffer for getting the details entered at client end
								BufferedReader clientin=new BufferedReader(new InputStreamReader(System.in));
								echo("Enter message to send : ");
								String encS=(String)clientin.readLine();
								s=encrypt(encS,"key");
								System.out.println("Encrypted Text: "+s);
								
								
								//sending data packet to category server
								DatagramPacket dp=new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
								sock.send(dp);
								System.out.println("Sending data to server");
								
								//receiving response from category server requesting client to enter the quantity of the product
								byte[] buffer1 = new byte[65536];
								DatagramPacket reply = new DatagramPacket(buffer1, buffer1.length);
								sock.receive(reply);																	
								byte[] data = reply.getData();
								s = new String(data, 0, reply.getLength());
								System.out.println("Encrypted text from Category Server: "+s);
								String desS1=decrypt(s,"key");
								System.out.println("Decrypted text from Category Server: "+desS1);																					
								echo("Enter quantity:");
								String encS1=(String)clientin.readLine();
								s=encrypt(encS1,"key");
								System.out.println("Encrypted Text: "+s);
								//Sending quantity to category server
								DatagramPacket dps=new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
								sock.send(dps);
								
								//receiving price and points from category server for the product and quantity chosen
								byte[] buffer2 = new byte[65536];
								DatagramPacket reply1 = new DatagramPacket(buffer2, buffer2.length);
								sock.receive(reply1);			                 
								byte[] data1 = reply1.getData();
								String decS2= new String(data1, 0, reply1.getLength());
								System.out.println("Encrypted text from Category Server: "+decS2);
								s = decrypt(decS2,"key");
								System.out.println("Decrypted text from Category Server: "+s);																													
								//updating the points file of the server with the points received from the client
								if (s.toUpperCase().startsWith("T"))
								{ 
									String s3 = s.replaceAll("[^0-9]", "");	
									Integer s4 = Integer.valueOf(s3);
									ObjectInputStream input1=new ObjectInputStream(new FileInputStream("Points.ser"));
									Points s1=(Points)input1.readObject();
									Integer s8 = Integer.valueOf(s1.Points);
			            			s8 = s8 + s4;
			            			System.out.println("Total points you earned is " + s8);
			             			String strI = "" + s8;
			            			Points s13 =new Points(strI);  
			            			FileOutputStream fout = new FileOutputStream("points.ser");  
			            			ObjectOutputStream out1=new ObjectOutputStream(fout);  			            			  
			            			out1.writeObject(s13);  
			            			if(s8 <= 499) 
			            			{			            				  
			            				System.out.println("You are a Silver member and you get 10% discount");
			            				double dis = 10;
			            				double amt;
			            				double mp = s4;
			            				double p1;
			            				p1=100-dis;
			            				amt = (p1*mp)/100;
			            				System.out.println("So amount after discount is" +amt);
			            				System.out.println("Thank you for shopping with us");
										echo("Would you like to shop again:(yes/no)");
										s=encrypt((String)clientin.readLine(),"key");
										DatagramPacket dps1=new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
										sock.send(dps1);			            			 
									}
			            			 else if(s8 <= 1999)
			            			 {
			            				 System.out.println("You are a Gold member and you get 15% discount");
			            				 double dis = 15;
			            				 double amt;
			            				 double mp = s4;
			            				 double p1;
			            				 p1=100-dis;
			            				 amt = (p1*mp)/100;
			            				 System.out.println("So amount after discount is" +amt);
			            				 System.out.println("Thank you for shopping with us");
			            				 echo("Would you like to shop again:(yes/no)");
			            				 s=encrypt((String)clientin.readLine(),"key");
			            				 DatagramPacket dps1=new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
			            				 sock.send(dps1);
			            			}			            			 
			            			 else if(s8 > 2000)
			            			 {
			            				 System.out.println("You are a Platinum member and you get 20% discount");
			            				 double dis = 20;
			            				 double amt;
			            				 double mp = s4;
			            				 double p1;
			            				 p1=100-dis;
			            				 amt = (p1*mp)/100;
			            				 System.out.println("So amount after discount is" +amt);
			            				 System.out.println("Thank you for shopping with us");
			            				 echo("Would you like to shop again:(yes/no)");
			            				 s=encrypt((String)clientin.readLine(),"key");
			            				 DatagramPacket dps1=new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
			            				 sock.send(dps1);
									 }
			            			input1.close();
			            			out1.close();
								}
								}
							}			
							catch(IOException e)
							{
								System.err.println("IOException " + e);
							}
							finally
							{
								sock.close();
								
							}
						}					
						/** Exit on 'q' char sent */
						else if ("q".equals(userInput)) 
						{
							break;
						}
						else
						{
							System.out.println(serverData3);
							System.out.println("Incorrect password try again");
							break;
						}

					}							
							
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally 
			{
				try 
				{
					out.close();
					in.close();
					//stdIn.close();
					echoSocket.close();
				} 
				catch (IOException ex) 
				{
					ex.printStackTrace();
				}
			}	
		}
		public static void echo(String msg)
		{
			System.out.println(msg);
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
		      String enc=new String(encrypted);
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
}