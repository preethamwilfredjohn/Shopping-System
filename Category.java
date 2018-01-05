import java.io.Serializable;  
@SuppressWarnings("serial")
public class Category implements Serializable
{  
 int SNo;  
 String Product;
 int Amount;  
 int Bonus;
 public Category(int SNo, String Product, int Amount, int Bonus) 
 {  
  this.SNo = SNo;  
  this.Product = Product;  
  this.Amount = Amount;
  this.Bonus = Bonus;
 }
public byte[] getBytes() {
	// TODO Auto-generated method stub
	return null;
}  
}  
