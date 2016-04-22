package u1402161;
import robocode.*;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/*  NishiKimi - a robot by Danielle O'Sullivan
 *  
 *  A Melee and 1v1 Bot for CS255 Artificial Intelligence Coursework (2016)
 *  
 *  In both Melee and 1v1 Battles, the following behaviour is shown:
 *  - <radar type>
 *  - <movement type>
 *  - <targeting type>
 */

public class NishiKimi extends Robot {
	//run: My Robot's default behavior

	public void run() {
		// Initialization of the robot should be put here
        
        // Set colours
        setColors(Color.orange, Color.orange, Color.orange, Color.magenta, Color.orange);
        
        // Robot main loop
		while(true) {
			//want to move randomly?
            ahead(100);
            //want to find robots
		}
	}

	//onScannedRobot: What happens when NK sees another robot
	public void onScannedRobot(ScannedRobotEvent e) {
        //calculation concept taken from TrackFire by Nelson & Larsen
        double absBearing = getHeading() + e.getBearing(); //finds true bearing from 0 degrees
        double bearingFromGun = completeAngle(absBearing - getGunHeading()); //finds bearing from gun...
        
        
        if (e.getDistance() < 50 && getEnergy() >= 60) { //if bot is close and NK has sufficient energy
            turnGunRight(bearingFromGun);
            fire(3.0); //fires powerful bullet
        } else if (e.getDistance() >= 50 && getEnergy() >= 60) { //if bot isn't close but NK has energy
            turnGunRight(bearingFromGun); //turns gun to face scanned bot
            fire(1.0); //fires less powerful bullet because of larger distance
            turnRight(e.getBearing()); //turns to face scanned bot
            ahead(e.getDistance() / 2); //halves distance
        } else { //if energy is low
            turnGunRight(bearingFromGun); //turns gun to face scanned bot
            fire(1.0); //want to conserve energy no matter what distance we are away
            turnRight(e.getBearing()); //turns to face scanned bot
            back(200); //backs away
        }
	}

    
    //onHitRobot: What happens when NK hits another bot
    public void onBulletMissed(BulletMissedEvent e) {

    }
    
	//onHitByBullet: What happens when NK is hit by a bullet
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	//onHitWall: What happens when NK hits a wall
	public void onHitWall(HitWallEvent e) {
        if (Math.abs(e.getBearing()) < 180) {
            turnRight(e.getBearing() + 180);
        } else {
            doNothing();
        }
	}
	
    //completeAngle: changes angles bigger than 360 degrees to equivalent 'smaller' angle in interval [-180,180]
    public void completeAngle(double a) {
        a = ((a % 360) + 360) % 360; //finds remainder of 'large' angle -- converts into a 'small' angle
        if (a > 180) {
            a = -(360 - a); //converts angles bigger than 180 into negative equivalent
        }
        return a;
    }
    
    //onWin: Victory dance!
    public void onWin(WinEvent e) {
        while (true) {
            turnRight(45);
            turnLeft(45);
        }
    }
    
    
    
}

