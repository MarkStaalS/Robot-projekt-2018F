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
	public static void main(String[] args) {
		/*
		 * Initialize objects
		 */
		detection det = new detection();
		remoteControl rc = new remoteControl();
		sound s = new sound();
		/*
		 * Start multi threading
		 */
		boolean state = true;
		
		det.start();
		rc.start();
		while(state == true) {
			try {
				rc.acquire(state);
			} catch (Exception e) {
				
			} finally {
				rc.release(state);
			}
		}
		s.stop();
	}
}
