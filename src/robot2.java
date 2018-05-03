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
	    /*Delay.msDelay(240);
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
	    Delay.msDelay(240);*/
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
		robot2.detectionLoop = true;
		double stopDistance = 0.1; 
		/*
		 * Initialize sensor
		 */
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S1);
		SensorMode distMode = us.getMode("Distance");
		RangeFinderAdapter ranger = new RangeFinderAdapter(distMode);
		
		while (robot2.mainLoop == true) {
			System.out.println("detection");
			Delay.msDelay(100);
			/*
			 * if statement used for detecting when distance is to close
			 */
			if ( ranger.getRange() < stopDistance ) {
				robot2.detectionLoop = false;	
			}
		}
		/*
		 * Close sensor and alocated resources
		 */
		us.close();
	}
}

public class robot2  {
	/*
	 * Variables used for communicating between threads
	 */
	static Boolean mainLoop = true;
	static Boolean detectionLoop = true;
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
		int i = 0;
		while(mainLoop ) {
			Delay.msDelay(25);
			System.out.println("main loop");
			/*
			 *  Get and act upon the IR commands
			 *  If there is an obstical do not move any further
			 */
			if (command == 1 && detectionLoop == true) {
				forward( 50);
			}
			else if (command == 2 && detectionLoop == true) {
				backward( 50);
			}
			else if (command ==3 && detectionLoop == true) {
				right(25);
			}
			else if(command == 4 && detectionLoop == true) {
				left(25);
			}
			else if(command == 8) {
				mainLoop = false;
			}
			
			if (i == 100000) {
				mainLoop = false;
			}
			i++;
		}
		/*
		 * Closing motors and playing final sound
		 */
		ir.close();
		finish();
		s.stop();
	}

	static void forward( int pow){
		b.setPower(pow);
		c.setPower(pow);
		b.forward();
		c.forward();
		Delay.msDelay(25);
	}
	
	static void backward( int pow){
		b.setPower(pow);
		c.setPower(pow);
		b.backward();
		c.backward();
		Delay.msDelay(25);
	}
	
	static void right( int pow){
		b.setPower(pow);
		b.forward();
		Delay.msDelay(25);
	}
	
	static void left( int pow){
		c.setPower(pow);
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