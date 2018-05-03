import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.utility.Delay;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RangeFinderAdapter;

class sound {
	/*
	 * Constructor
	 */
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
		/*
		 * Local variable
		 */
		double stopDistance = 0.1; 
		/*
		 * Initialize sensor
		 */
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S1);
		SensorMode distMode = us.getMode("Distance");
		RangeFinderAdapter ranger = new RangeFinderAdapter(distMode);
		
		while (robot.mainLoop == true) {
			/*
			 * Prints the current command to the ev3
			 */
			LCD.drawString("Range:" + ranger.getRange() + " ", 0, 3);
			Delay.msDelay(100);
			/*
			 * if statement used for detecting when distance is to close
			 */
			if ( ranger.getRange() < stopDistance ) robot.detection = true;
			else robot.detection = false;
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
	/*
	 * Constructor
	 */
	public control() {
	}
	
	static void controlLoop(){
		EV3IRSensor ir = new EV3IRSensor(SensorPort.S4);
		/*
		 * Loop for handling inputs from remote 
		 */
		while(robot.mainLoop == true) {
			/*
			 * Needs to be in the loop otherwise the command is static and the robot will not respont to new inputs
			 */
			int command = ir.getRemoteCommand(1);
			Delay.msDelay(150);
			/*
			 * Prints the current command to the ev3
			 */
			LCD.drawString("COM:" + command + " ", 0, 2);
			/*
			 *  If there is an obstical do not move any further, when obstical is gone start moving again
			 */
			if (command == 1 && robot.detection == false) forward(100);
			else if (command == 2 && robot.detection == false) backward(100);
			else if (command ==3 && robot.detection == false) right(50);
			else if(command == 4 && robot.detection == false) left(50);
			else if(command == 8) robot.mainLoop = false;
			else stop();
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
	 * Class variables used for communicating between threads
	 */
	static Boolean mainLoop = true;
	static Boolean detection = false;
	/*
	 * Initialize motors
	 */
	static UnregulatedMotor b = new UnregulatedMotor(MotorPort.B);
	static UnregulatedMotor c = new UnregulatedMotor(MotorPort.C);
	
	public static void main(String[] args) {
		System.out.println("start");
		/*
		 * Declaration of objects
		 */
		detection det = new detection();
		sound s = new sound();
		control ctrl = new control();
		/*
		 * Start multi threading 
		 */
		det.start();
		/*
		 * Play start melody, access method from other class
		 */
		s.start();
		/*
		 * Run control loop
		 */
		ctrl.controlLoop();
		/*
		 * Playing stop sound
		 */
		s.stop();
	}
}
