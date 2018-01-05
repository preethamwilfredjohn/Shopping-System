import java.io.*;  
class Persist{  
 @SuppressWarnings("resource")
public static void main(String args[])throws Exception
 {  
  Category s1 =new Category(101,"Dress",20,20);  
  Category s2 =new Category(102,"Bag",15,15);
  Category s3 =new Category(103,"Shoe",10,10);
  Category s4 =new Category(104,"Books",5,5);
  FileOutputStream fout=new FileOutputStream("pert.txt");  
  ObjectOutputStream out=new ObjectOutputStream(fout);  
  out.writeObject(s1);  
  out.writeObject(s2);
  out.writeObject(s3);
  out.writeObject(s4);
  out.flush();  
  System.out.println("success");  
 }  
}  