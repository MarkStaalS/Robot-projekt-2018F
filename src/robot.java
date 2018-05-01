//import java.util.concurrent.Semaphore;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.utility.Delay;
import java.util.concurrent.Semaphore;
import lejos.hardware.*;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RangeFinderAdapter;

class sound {
	public sound() {
	}
	
	public void start() {
		/*
		 * Sound to play at the start
		 */
		Delay.msDelay(240);
	    Sound.playTone(784,200);
	    Delay.msDelay(240);
	    Sound.playTone(740,200);
	    Delay.msDelay(240);
	    Sound.playTone(659,200);
	    Delay.msDelay(240);
	    Sound.playTone(659,400);
	    Delay.msDelay(480);
	    Sound.playTone(740,200);
	    Delay.msDelay(240);
	    Delay.msDelay(480);
	    Sound.playTone(587,400);
	    Delay.msDelay(480);
	    Sound.playTone(659,200);
	    Delay.msDelay(240);
	    Sound.playTone(440,400);
	    Delay.msDelay(240);
	}
	public void stop() {
		/*
		 * sound when stopping
		 */
	    Sound.playTone(784,200);
	    Delay.msDelay(240);
	    Sound.playTone(740,200);
	    
	}
}

/*
 * handles detection of objects
 */
class detection extends Thread {
	public void run() {
		robot.detection = true;
		double stopDistance = 0.1; 
		/*
		 * Initialize sensor
		 */
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S1);
		SensorMode distMode = us.getMode("Distance");
		RangeFinderAdapter ranger = new RangeFinderAdapter(distMode);
		
		while (robot.mainLoop == true) {
			Delay.msDelay(100);
			/*
			 * if statement used for detecting when distance is to close
			 */
			if ( ranger.getRange() < stopDistance ) robot.detection = false;	
		}
		/*
		 * Close sensor and alocated resources
		 */
		us.close();
	}
}

public class robot  {
	/*
	 * Variables used for communicating between threads
	 */
	static Boolean mainLoop = true;
	static Boolean detection = true;
	/*
	 * Initialize motors
	 */
	static UnregulatedMotor b = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor c = new UnregulatedMotor(MotorPort.C);
	
	public static void main(String[] args) {
		/*
		 * Initialize objects
		 */
		detection det = new detection();
		sound s = new sound();
		EV3IRSensor ir = new EV3IRSensor(SensorPort.S4);
		
		int command = ir.getRemoteCommand(1);
		/*
		 * Start multi threading
		 */
		det.start();
		/*
		 * Play start melody
		 */
		s.start();
		/*
		 * Loop for handling inputs from remote 
		 */
		while(mainLoop) {
			Delay.msDelay(25);
			/*
			 *  Get and act upon the IR commands
			 *  If there is an obstical do not move any further
			 */
			if (command == 1 && detection == true) forward( 50);
			else if (command == 2 && detection == true) backward( 50);
			else if (command ==3 && detection == true) right(25);
			else if(command == 4 && detection == true) left(25);
			if(command == 8) mainLoop = false;
		}
		/*
		 * Closing devices
		 */
		ir.close();
		finish();
		/*
		 * Playing stop sound
		 */
		s.stop();
	}

	static void forward(int power){
		b.setPower(power);
		c.setPower(power);
		b.forward();
		c.forward();
		Delay.msDelay(25);
	}
	
	static void backward(int power){
		b.setPower(power);
		c.setPower(power);
		b.backward();
		c.backward();
		Delay.msDelay(25);
	}
	
	static void right(int power){
		b.setPower(power);
		b.forward();
		Delay.msDelay(25);
	}
	
	static void left(int power){
		c.setPower(power);
		c.forward();
		Delay.msDelay(25);
	}
	
	static void finish(){
		b.flt();
		c.flt();
		b.close();
		c.close();
	}
}
