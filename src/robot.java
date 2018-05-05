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

/*
 * Class is a colection of code that has a functionality,
 * Normal pratice is to keep functionality to a single purpose
 */
class sound {
	/*
	 * 3 spaces, designates who has access to what:
	 * 
	 * Private, here we have functions that should not be accessed from the outside, e.g flag for when 
	 * 	we are about to drive into a wall.
	 * 
	 * Public, everybody has access, things we use to interact with the class (methods and variables)
	 * 	Normal pratice: it is common that variables are private and can only be changed using set and get methods
	 * 
	 * Protected, no further comment
	 */
	
	/*
	 * Constructor, multiple types, this is how we create instances of that class
	 * 
	 * Normal:
	 * without arguments or parameters
	 * 
	 * Overload:
	 * Means that it has a set of arguments/parameters that is used when creating the object
	 * Way of instanciation the object with the desired attributes
	 */
	public sound() {
		/*
		 * We can have code here that runs when the object is created (once) 
		 */
	}
	/*
	 * Function/ method, collection of code that can be called and executed
	 */
	public void playStartMelody() {
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
	
	public void playStopMelody() {
	    Sound.playTone(784,200);
	    Delay.msDelay(240);
	    Sound.playTone(740,200);
	}
}

/*
 * handles detection of obstacles in path
 */
class detection extends Thread {
	public void run() {
		/*
		 * Global variable
		 */
		robot.detection = false;
		/*
		 * Local variable
		 */
		double stopDistance = 0.1; 
		/*
		 * Initialize sensor, creating instances of classes from libraries 
		 */
		EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S1);
		SensorMode distMode = us.getMode("Distance");
		RangeFinderAdapter ranger = new RangeFinderAdapter(distMode);
		/*
		 * Check condition in the start of the loop:
		 * for loop, is used when iterating over a set of values such as i++
		 * while loop, is dependent on other factors, such as when driving into a wall
		 * 
		 * Checks condition in the bottom of the loop:
		 * do while, we will always run the code atleast once
		 */
		while (robot.mainLoop == true) {
			/*
			 * Prints the current command to the ev3
			 */
			LCD.drawString("Range:" + ranger.getRange() + " ", 0, 3);
			Delay.msDelay(100);
			/*
			 * if statement used for detecting when distance is to close
			 */
			if ( ranger.getRange() < stopDistance )
				robot.detection = true;
			else
				robot.detection = false;
		}
		/*
		 * Close sensor and free up resources
		 * The associatet resources in the CPU are freed up for use in other applications
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
	
	public static void controlLoop(){
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
			 *  If there is an obstacle do not move any further, when obstacle is gone start moving again
			 */
			if (robot.detection == false) {
				switch (command) {
					case 1:
						forward(100); break;
					case 2:
						backward(100); break;
					case 3:
						right(50); break;
					case 4:
						left(50); break;
					case 8:
						robot.mainLoop = false; break;
					default:
						stop();
				}	
			}
		}
		/*
		 * Closing devices and freeing up resources
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

public class robot {
	/*
	 * Class variables used for communicating between threads
	 */
	static Boolean mainLoop;
	static Boolean detection;
	/*
	 * Initialize motors
	 */
	static UnregulatedMotor b;
	static UnregulatedMotor c;
	
	public robot() {
		/*
		 * These variables needs to be set once before the program runs
		 */
		mainLoop = true;
		detection = false;
		b = new UnregulatedMotor(MotorPort.B);
		c = new UnregulatedMotor(MotorPort.C);
	}
	
	public static void main(String[] args) {
		System.out.println("start");
		/*
		 * Instanciating of objects
		 */
		detection det = new detection();
		sound s = new sound();
		control ctrl = new control();
		/*
		 * Start multi-threading 
		 */
		det.start();
		s.playStartMelody();
		/*
		 * Run control loop
		 */
		ctrl.controlLoop();
		s.playStopMelody();
	}
}
