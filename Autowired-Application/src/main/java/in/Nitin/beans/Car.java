package in.Nitin.beans;

public class Car {
	
	
	
	private DieselEngine dieselEng;
	
	public void setDieselEng(DieselEngine diselEng, DieselEngine dieselEng) {
		this.dieselEng = dieselEng;
	}
	public Car() {
		System.out.println("Car::Constructor");
	}
	
	public void drive() {
		int start = dieselEng.start();
		if (start >= 1) {
			System.out.println("Journey Started...");	
		} else {
			System.out.println("Engine Failed to Start...!!!");
		
	   }
    }

}
