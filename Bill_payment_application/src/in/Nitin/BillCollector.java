package in.Nitin;

public class BillCollector {
	
	private ipayment payment; 
	
	public void setPay(ipayment payment) {
		this.payment = payment;
	}
	
	public BillCollector() {
		// TODO AUto-generated constructor stub
		
	}
	
	public BillCollector(ipayment payment) {
		this.payment = payment;    
		
	}

	public void collectPayment(double amount) {
		  
		String status = payment.pay(amount);
		
		System.out.println(status);
	}
}

// Bill collector class talking to interface directly, not using interface,
// not using inheritance, not directly creating objects, not talking to any class directly 
// Bill collector class not  talking with any class directly, hence we can say the Billcollector class class is loosely couple 