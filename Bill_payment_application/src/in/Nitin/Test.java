
 package in.Nitin;

public class Test {
	
	public static void main(String[] args) {
		
//		BillCollector bc = new BillCollector();
//		
//		// injecting CreditCard obj into BillCollector ( Setter Injection)
//		bc.setPayment(new CreaditcardPayment());
		
		
		//injecting CrediCard obj into BillController (Constructor Injection)
//		BillCollector bc1 = new BillCollector(new CreaditcardPayment());
//		bc1.collectPayment(1500.00);
		
		BillCollector bc1 = new BillCollector(new DebitcardPayment());
		bc1.collectPayment(1500.00);
		
		
		
		
		     
	}	    

}
