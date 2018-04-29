import java.util.concurrent.Semaphore;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.utility.Delay;
import java.util.concurrent.Semaphore;


class sound {
	public sound() {
		start();
	}
	
	public void start() {
		/*
		 * Sound to play at the start
		 * TODO replace sounds
		 */
		
	}
	public void stop() {
		/*
		 * sound when stopping
		 * TODO replace sounds
		 */
		
	}
	/*
	sub MyPlayTone(unsigned int frequency, unsigned int duration)
	{
	    PlayToneEx(frequency, duration, 1, false);
	}

	void playMusic()
	{
	    MyPlayTone(440,200);
	    Wait(240);
	    MyPlayTone(784,200);
	    Wait(240);
	    MyPlayTone(740,200);
	    Wait(240);
	    MyPlayTone(659,200);
	    Wait(240);
	    MyPlayTone(659,400);
	    Wait(480);
	    MyPlayTone(740,200);
	    Wait(240);
	    Wait(480);
	    MyPlayTone(587,400);
	    Wait(480);
	    MyPlayTone(659,200);
	    Wait(240);
	    MyPlayTone(440,400);
	    Wait(240);

	}*/
}

/*
 * handles detection of objects
 */
class detection extends Thread {
	public void run() {
		System.out.println("start detec");
		/*
		 * Initialize sensor
		 */
		
		double distance = 0;
		double minDistance = 5; 
		while (true) {
			/*
			 * if statement used for detecting when distance is to close
			 */
			if (distance >= minDistance ) {
				try {
					main.mutex.acquire();
					try {
						main.state = false;
						System.out.println("det, end");
					} finally {
						main.mutex.release();
					}
				} catch (Exception e) {
				}
			} else {
				main.state = true;
			}
		}
		/*
		 * evt. running avg
		 */
	}
}

public class main {
	/*
	 * Used for communicating between threads
	 */
	static boolean state = true;
	/*
	 * Makes sure that multiple threads are not accessing the same variable simultainiously
	 */
	static Semaphore mutex = new Semaphore(1);
	
	public static void main(String[] args) {
		/*
		 * Initialize objects
		 */
		detection det = new detection();
		sound s = new sound();
		/*
		 * Start multi threading
		 */
		det.start();
		
		/*
		 * TODO insert rc commands, if needed can be a while true loop
		 */
		
		/*
		 * If statement , knap trykket og detetcion == false,
		 */
		s.stop();
	}
}
