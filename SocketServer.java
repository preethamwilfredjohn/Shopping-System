//package com.admfactory.io.socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

//Creating thread for each seprate client using TCP connection
public class SocketServer extends Thread 
{
	private static String algorithm = "RC4";
	public static final int PORT_NUMBER = 2007;
	protected Socket socket;
	//creating connection for the client
	private SocketServer(Socket socket) 
	{
		this.socket = socket;
		System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
		start();
	}
	public void run() 
	{
		//creating streams for communication with clients
		InputStream in = null;
		OutputStream output=null;
		PrintWriter out = null;
		try {
			in = socket.getInputStream();
			output=socket.getOutputStream();
			out = new PrintWriter(output,true);
			
			//getting username from client
			String un1;
				while(true)
				{
					//creating buffer for communication with client
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					
					while ((un1=br.readLine()) != null) 
					{
					String un= decrypt(un1,"key");
					System.out.println("Encrypted: "+un1);
					System.out.println("Decrypted: "+un);
					String client1="client1";
					String client2="client2";
					String client3="client3";
					String SilverHost = "10.91.60.78";
					String Goldhost = "10.91.60.78";
					String PlatHost = "10.91.60.78";
					int Silverport=2201; 
					int Goldport=2300;
					int Platport=2400;
					
					//checking if it is client1
					if(un.equals(client1))
					{
						String reqPw= "PW: ";
						String toClient=encrypt(reqPw,"key");						
						System.out.println("Encrypted: "+toClient);
						out.println(toClient);
						System.out.println("client : "+un);
						String pwEnc=br.readLine();
						System.out.println("Encryted password: "+pwEnc);
						String pw=decrypt(pwEnc,"key");
						System.out.println("client : "+pw);
						
						//checking for password
						if(pw.equals("1234"))
						{
							out.println("password match");
							String ptse=br.readLine();
							System.out.println("Encrypted points: "+ptse);
							String pts=decrypt(ptse,"key");
							System.out.println("Decrypted points: "+pts);
							String clientIP=encrypt(br.readLine(),"key");
							
							//checking for points for redirecting client connection to appropriate category server
								if(Integer.parseInt(pts)<=499)
								{
									try
									{	
										//connecting with silver server with TCP connection
										System.out.println("Connecting to Silver host " + SilverHost + " on port " + Silverport + ".");
										Socket cateSocketSilver = null;
										PrintWriter couts = null;
										BufferedReader cins = null;
										try 
										{
											cateSocketSilver = new Socket(SilverHost, Silverport);
											couts = new PrintWriter(cateSocketSilver.getOutputStream(), true);
											cins = new BufferedReader(new InputStreamReader(cateSocketSilver.getInputStream()));
										} 
										catch (UnknownHostException e) 
										{
											System.err.println("Unknown host: " + SilverHost);
											System.exit(1);
										} 
										catch (IOException e) 
										{
											System.err.println("Unable to get streams from silver server"+e);
											System.exit(1);
										}

										//sending client IP to silver server
										System.out.println("Encrypted client IP: "+clientIP);
										couts.println(clientIP);
										String read;
																			
										while ((read=cins.readLine())!=null) 
										{
											System.out.println("Silver server: " + read);
											System.out.println("Sent Client IP to Silver server");
										}
										/* Closing all the resources */
										couts.close();
										cins.close();
										cateSocketSilver.close();
									}
									catch (Exception e) 
									{
										e.printStackTrace();
									}
									out.println("Connecting to silver server");
									out.println("2200");
								}
								else if(Integer.parseInt(pts)<=1999)
								{
									try
									{
										//connecting with gold server with TCP connection
										System.out.println("Connecting to Gold host " + Goldhost + " on port " + Goldport + ".");
										Socket cateSocketSilver = null;
										PrintWriter couts = null;
										BufferedReader cins = null;
										try 
										{
											cateSocketSilver = new Socket(Goldhost, Goldport);
											couts = new PrintWriter(cateSocketSilver.getOutputStream(), true);
											cins = new BufferedReader(new InputStreamReader(cateSocketSilver.getInputStream()));
										} 
										catch (UnknownHostException e) 
										{
											System.err.println("Unknown host: " + Goldhost);
											System.exit(1);
										} 
										catch (IOException e) 
										{
											System.err.println("Unable to get streams from gold server"+e);
											System.exit(1);
										}
										
										//sending client IP to gold server
										couts.println(clientIP);
										String read;
										while ((read=cins.readLine())!=null) 
										{
											System.out.println("Gold server: " + read);
											System.out.println("Sent Client IP to Gold server");
										}
										/* Closing all the resources*/
										couts.close();
										cins.close();
										cateSocketSilver.close();
									}
									catch (Exception e) 
									{
										e.printStackTrace();
									}
									out.println("Connecting to Gold server");
									out.println("2300");
								}
								else if(Integer.parseInt(pts)>2000)
								{
									try
									{
										//connecting with Platinum server with TCP connection
										System.out.println("Connecting to Platinum host " + PlatHost + " on port " + Platport + ".");
										Socket cateSocketSilver = null;
										PrintWriter couts = null;
										BufferedReader cins = null;
										try 
										{
											cateSocketSilver = new Socket(PlatHost, Platport);
											couts = new PrintWriter(cateSocketSilver.getOutputStream(), true);
											cins = new BufferedReader(new InputStreamReader(cateSocketSilver.getInputStream()));
										} 
										catch (UnknownHostException e) 
										{
											System.err.println("Unknown host: " + PlatHost);
											System.exit(1);
										} 
										catch (IOException e) 
										{
											System.err.println("Unable to get streams from platinum server"+e);
											System.exit(1);
										}
										
										//Sending client IP to platinum server
										couts.println(clientIP);
										String read;
										while ((read=cins.readLine())!=null) 
										{
											System.out.println("Platinum server: " + read);
											System.out.println("Sent Client IP to Platinum server");
										}
										/* Closing all the resources*/ 
										couts.close();
										cins.close();
										cateSocketSilver.close();
									}
									catch (Exception e) 
									{
										e.printStackTrace();
									}
									out.println("Connecting to platinum server");
									out.println("2400");
								}
								else 
								{
									out.println("Connection cannot be made category server");
								}
							}
							else
							{
								out.println("password mis match. try again");
								break;
							}
					}
					
					//checking if it is client2
					else if(un.equals(client2))
					{
                        String reqPw= "PW: ";	
                        String toClient=encrypt(reqPw,"key");                                                	
						out.println(toClient);
						System.out.println("client : "+un);
						String pw=decrypt(br.readLine(),"key");
						System.out.println("client :  "+pw);
						
						//checking for client2 password
						if(pw.equals("1234"))
						{
							out.println("password match");
							String ptse=br.readLine();
							System.out.println("Encrypted points: "+ptse);
							String pts=decrypt(ptse,"key");
							System.out.println("Decrypted points: "+pts);
							String clientIP=encrypt(br.readLine(),"key");
							
							//checking for points for redirecting client connection to appropriate category server
								if(Integer.parseInt(pts)<=499)
								{									
									try
									{	
										//connecting with silver server with TCP connection
										System.out.println("Connecting to silver host " + SilverHost + " on port " + Silverport + ".");
										Socket cateSocketSilver = null;
										PrintWriter couts = null;
										BufferedReader cins = null;
										try 
										{
											cateSocketSilver = new Socket(SilverHost, Silverport);
											couts = new PrintWriter(cateSocketSilver.getOutputStream(), true);
											cins = new BufferedReader(new InputStreamReader(cateSocketSilver.getInputStream()));
										} 
										catch (UnknownHostException e) 
										{
											System.err.println("Unknown host: " + SilverHost);
											System.exit(1);
										} 
										catch (IOException e) 
										{
											System.err.println("Unable to get streams from silver server"+e);
											System.exit(1);
										}
										
										//sending client IP to silver server
										couts.println(clientIP);
										String read;
										while ((read=cins.readLine())!=null) 
										{
											System.out.println("Silver server: " + read);
											System.out.println("Sent Client IP to Silver server");
										}
										/* Closing all the resources*/
										couts.close();
										cins.close();
										cateSocketSilver.close();
									}
									catch (Exception e) 
									{
										e.printStackTrace();
									}
									out.println("Connecting to silver server");
									out.println("2200");
								}
								else if(Integer.parseInt(pts)<=1000)
								{
									try
									{
										//connecting with gold server with TCP connection
										System.out.println("Connecting to gold host " + Goldhost + " on port " + Goldport + ".");
										Socket cateSocketSilver = null;
										PrintWriter couts = null;
										BufferedReader cins = null;
										try 
										{
											cateSocketSilver = new Socket(Goldhost, Goldport);
											couts = new PrintWriter(cateSocketSilver.getOutputStream(), true);
											cins = new BufferedReader(new InputStreamReader(cateSocketSilver.getInputStream()));
										} 
										catch (UnknownHostException e) 
										{
											System.err.println("Unknown host: " + Goldhost);
											System.exit(1);
										} 
										catch (IOException e) 
										{
											System.err.println("Unable to get streams from gold server"+e);
											System.exit(1);
										}
										
										//sending client IP to gold server
										couts.println(clientIP);
										String read;
										while ((read=cins.readLine())!=null) 
										{
											System.out.println("Gold server: " + read);
											System.out.println("Sent Client IP to GOld server");
										}
										/** Closing all the resources*/ 
										couts.close();
										cins.close();
										//stdIns.close();
										cateSocketSilver.close();
									}
									catch (Exception e) 
									{
										e.printStackTrace();
									}
									out.println("Connecting to gold server");
									out.println("2300");
								}
								else if(Integer.parseInt(pts)<=1500)
								{
									try
									{
										//connecting with gold server with TCP connection
										System.out.println("Connecting to platinum host " + PlatHost + " on port " + Platport + ".");
										Socket cateSocketSilver = null;
										PrintWriter couts = null;
										BufferedReader cins = null;
										try 
										{
											cateSocketSilver = new Socket(PlatHost, Platport);
											couts = new PrintWriter(cateSocketSilver.getOutputStream(), true);
											cins = new BufferedReader(new InputStreamReader(cateSocketSilver.getInputStream()));
										} 
										catch (UnknownHostException e) 
										{
											System.err.println("Unknown host: " + PlatHost);
											System.exit(1);
										} 
										catch (IOException e) 
										{
											System.err.println("Unable to get streams from platinum server"+e);
											System.exit(1);
										}
										
										//sending client iP to platinum  server
										couts.println(clientIP);
										String read;
										while ((read=cins.readLine())!=null) 
										{
											System.out.println("platinum server: " + read);
											System.out.println("Sent Client IP to platinum server");
										}
										/* Closing all the resources*/
										couts.close();
										cins.close();									
										cateSocketSilver.close();
									}
									catch (Exception e) 
									{
										e.printStackTrace();
									}
									out.println("Connecting to platinum server");
									out.println("2400");
								}
								else 
								{
									out.println("Connection cannot be made category server");
								}
							}
							else
							{
								out.println("password mis match. try again");
								break;
							}
					}
					
					//checking for client 3 user name
					else if(un.equals(client3))
					{
						String reqPw= "PW: ";
						String toClient=encrypt(reqPw,"key");  
						System.out.println("Encrypted: "+toClient);
						out.println(toClient);
						String pw=decrypt(br.readLine(),"key");
						System.out.println("client :  "+pw);
						
						//checking for client 3 password
						if(pw.equals("1234"))
						{
							out.println("password match");
							String ptse=br.readLine();
							System.out.println("Encrypted points: "+ptse);
							String pts=decrypt(ptse,"key");
							System.out.println("Decrypted points: "+pts);
							String clientIP=encrypt(br.readLine(),"key");

							//checking for points for redirecting client connection to appropriate category server
								if(Integer.parseInt(pts)<=499)
								{
									try
									{
										
										//connecting with client server with TCP connection
										System.out.println("Connecting to silver host " + SilverHost + " on port " + Silverport + ".");
										Socket cateSocketSilver = null;
										PrintWriter couts = null;
										BufferedReader cins = null;
										try 
										{
											cateSocketSilver = new Socket(SilverHost, Silverport);
											couts = new PrintWriter(cateSocketSilver.getOutputStream(), true);
											cins = new BufferedReader(new InputStreamReader(cateSocketSilver.getInputStream()));
										} 
										catch (UnknownHostException e) 
										{
											System.err.println("Unknown host: " + SilverHost);
											System.exit(1);
										} 
										catch (IOException e) 
										{
											System.err.println("Unable to get streams from silver server"+e);
											System.exit(1);
										}
										
										//sending client IP to silver server
										couts.println(clientIP);
										String read;
										while ((read=cins.readLine())!=null) 
										{
											System.out.println("Silver server: " + read);
											System.out.println("Sent Client IP to silver server");
										}
										/** Closing all the resources*/
										couts.close();
										cins.close();
										//stdIns.close();
										cateSocketSilver.close();
									}
									catch (Exception e) 
									{
										e.printStackTrace();
									}
									out.println("Connecting to silver server");
									out.println("2200");
								}
								else if(Integer.parseInt(pts)<=1000)
								{
									try
									{
										//connecting with gold server with TCP connection

										System.out.println("Connecting to gold host " + Goldhost + " on port " + Goldport + ".");
										Socket cateSocketSilver = null;
										PrintWriter couts = null;
										BufferedReader cins = null;
										try 
										{
											cateSocketSilver = new Socket(Goldhost, Goldport);
											couts = new PrintWriter(cateSocketSilver.getOutputStream(), true);
											cins = new BufferedReader(new InputStreamReader(cateSocketSilver.getInputStream()));
										} 
										catch (UnknownHostException e) 
										{
											System.err.println("Unknown host: " + Goldhost);
											System.exit(1);
										} 
										catch (IOException e) 
										{
											System.err.println("Unable to get streams from gold server"+e);
											System.exit(1);
										}
										
										//sending client IP to gold server
										couts.println(clientIP);
										String read;
										while ((read=cins.readLine())!=null) 
										{
											System.out.println("gold server: " + read);
											System.out.println("Sent Client IP to gold server");
										}
										couts.close();
										cins.close();
										cateSocketSilver.close();
									}
									catch (Exception e) 
									{
										e.printStackTrace();
									}
									out.println("Connecting to gold server");
									out.println("2300");
								}
								else if(Integer.parseInt(pts)<=1500)
								{
									try
									{
										//connecting with platinum server with TCP connection
										System.out.println("Connecting to platinum host " + PlatHost + " on port " + Platport + ".");
										Socket cateSocketSilver = null;
										PrintWriter couts = null;
										BufferedReader cins = null;
										try 
										{
											cateSocketSilver = new Socket(PlatHost, Platport);
											couts = new PrintWriter(cateSocketSilver.getOutputStream(), true);
											cins = new BufferedReader(new InputStreamReader(cateSocketSilver.getInputStream()));
										} 
										catch (UnknownHostException e) 
										{
											System.err.println("Unknown host: " + PlatHost);
											System.exit(1);
										} 
										catch (IOException e) 
										{
											System.err.println("Unable to get streams from platinum server"+e);
											System.exit(1);
										}
										
										//sending client IP to platinum server
										couts.println(clientIP);
										String read;
										while ((read=cins.readLine())!=null) 
										{
											System.out.println("platinum server: " + read);
											System.out.println("Sent Client IP to platinum server");
										}
										/** Closing all the resources*/
										couts.close();
										cins.close();
										//stdIns.close();
										cateSocketSilver.close();
									}
									catch (Exception e) 
									{
										e.printStackTrace();
									}
									out.println("Connecting to platinum server");
									out.println("2400");
								}
								else 
								{
									out.println("Connection cannot be made category server");
								}
							}
							else
							{
								out.println("password mis match. try again");
								break;
							}
					}
					else
					{
						out.println("Please enter appropriate user name");
						break;
					}
				}
			}				
		}		
		catch (Exception ex) 
		{
			System.out.println("Unable to get streams from client");
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

	public static void main(String[] args) {
		System.out.println("Group Server Hosted");
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT_NUMBER);
			while (true) {
				//creating multiple TCP connection with clients
				new SocketServer(server.accept());
			}
		} catch (IOException ex) {
			System.out.println("Unable to start server.");
		} finally {
			try {
				if (server != null)
					server.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}