package in.Nitin.main;

import org.springframework.context.ApplicationContext;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import in.Nitin.beans.Car;

public class Driver{
	
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Beans.xml");
		
		
		Car car = context.getBean("car", Car.class);
		
		car.drive();
		
	}

}