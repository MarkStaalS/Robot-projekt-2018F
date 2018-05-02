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
		robot.detection = false;
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
			if ( ranger.getRange() < stopDistance ) robot.detection = true;	
		}
		/*
		 * Close sensor and alocated resources
		 */
		us.close();
	}
}
/*
 * Control loop
 */
class control{
	public control() {
	}
	
	static void controlLoop(){
		EV3IRSensor ir = new EV3IRSensor(SensorPort.S4);
		int command = ir.getRemoteCommand(1);
		/*
		 * Loop for handling inputs from remote 
		 */
		while(robot.mainLoop) {
			Delay.msDelay(25);
			/*
			 *  Get and act upon the IR commands
			 *  If there is an obstical do not move any further
			 */
			if (robot.detection == true) stop();
			else if (command == 1 && robot.detection == false) forward(50);
			else if (command == 2 && robot.detection == false) backward(50);
			else if (command ==3 && robot.detection == false) right(25);
			else if(command == 4 && robot.detection == false) left(25);
			else if(command == 8) robot.mainLoop = false;
		}
		/*
		 * Closing devices
		 */
		ir.close();
		finish();
	}
	private static void forward(int power){
		robot.b.setPower(power);
		robot.c.setPower(power);
		robot.b.forward();
		robot.c.forward();
		Delay.msDelay(25);
	}
	
	private static void backward(int power){
		robot.b.setPower(power);
		robot.c.setPower(power);
		robot.b.backward();
		robot.c.backward();
		Delay.msDelay(25);
	}
	
	private static void right(int power){
		robot.b.setPower(power);
		robot.b.forward();
		Delay.msDelay(25);
	}
	
	private static void left(int power){
		robot.c.setPower(power);
		robot.c.forward();
		Delay.msDelay(25);
	}
	
	private static void stop() {
		robot.b.flt();
		robot.c.flt();
		Delay.msDelay(25);
	}
	
	private static void finish(){
		robot.b.flt();
		robot.c.flt();
		robot.b.close();
		robot.c.close();
	}	
}
public class robot  {
	/*
	 * Variables used for communicating between threads
	 */
	static Boolean mainLoop = true;
	static Boolean detection = false;
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
		control ctrl = new control();
		/*
		 * Start multi threading
		 */
		det.start();
		/*
		 * Play start melody
		 */
		s.start();
		/*
		 * run control loop
		 */
		ctrl.controlLoop();
		/*
		 * Playing stop sound
		 */
		s.stop();
	}
}
