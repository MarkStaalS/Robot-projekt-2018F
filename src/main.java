import java.util.concurrent.Semaphore;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.utility.Delay;

class sound {
	public sound() {
		start();
	}
	
	public void start() {
		/*
		 * Sound to play at the start
		 * TODO replace sounds
		 */
		Sound.playTone(100, 500);
	}
	public void stop() {
		/*
		 * sound when stopping
		 * TODO replace sounds
		 */
		Sound.playTone(300, 500);
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
		/*
		 * Initialize sensor
		 */
		EV3IRSensor irS = new EV3IRSensor(SensorPort.S1);
		/*
		 * TODO while loop , when distance is below something return false
		 */
		
		/*
		 * while loop for detecting when an object is in range of the sensor
		 */
		
	}
	public boolean obsticleInTheWay() {
		return false;
	}
}

/*
 * remote control and motor control
 */
class remoteControl extends Thread {
	boolean state;
	
	public void run() {
		/*
		 * Initialize motors
		 */
		UnregulatedMotor b = new UnregulatedMotor(MotorPort.B);
		UnregulatedMotor c = new UnregulatedMotor(MotorPort.C);
		/*
		 * Initialize IR
		 */
		EV3IRSensor ir = new EV3IRSensor(SensorPort.S4);
		state = true;
		/*
		 * Keep looping untill 8 is pressed
		 */
		while(state) {
			Delay.msDelay(25);
			/*
			 * Get commands from remote
			 */
			int command = ir.getRemoteCommand(1);
			/*
			 * TODO switch for different inputs and actions
			 */
			
			/*
			 * If 8 is pressed stop robot
			 */
			if(command == 8);
				state = false;
		}
		ir.close();
		stopMotorsAndClose(b, c);
	}
	
	public void stopMotorsAndClose(UnregulatedMotor b, UnregulatedMotor c) {
		b.flt();
		c.flt();
		b.close();
		c.close();
	}
}

public class main {
	public void main(String[] args) {
		/*
		 * Initialize objects
		 */
		detection det = new detection();
		remoteControl rc = new remoteControl();
		Semaphore mutex = new Semaphore(1);
		sound s = new sound();
		/*
		 * Start multi threading
		 */
		boolean state = true;
		
		det.start();
		rc.start();
		
		while(state == true) {
			try {
				mutex.acquire();
				try {
					state = false;
					System.out.println("det modsatte af fisk");
				} finally {
					mutex.release();
				}
			} catch (Exception e) {
				
			}
		}
		s.stop();
	}
}
