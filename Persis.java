import java.io.*;  
class Persis{  
 @SuppressWarnings("resource")
public static void main(String args[])throws Exception{  
  Points s1 =new Points("100");  
  //Points s2 =new Points(600,"Gold");
  //Points s3 =new Points(3000,"Platinum");
  
  FileOutputStream fout=new FileOutputStream("points.ser");  
ObjectOutputStream out=new ObjectOutputStream(fout);  
  
  out.writeObject(s1);  
  //out.writeObject(s2);
  //out.writeObject(s3);

  out.flush();  
  System.out.println("success");  
 }  
  
}  