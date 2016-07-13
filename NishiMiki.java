package u1402161;
import robocode.*;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/*  NishiMiki - a robot by Danielle O'Sullivan 
 *  Intended for CS255 Artificial Intelligence Coursework (2016) 
 */

public class NishiMiki extends Robot {

	double rotateGun = 0;
	double initialEnergy = 100;
	int consecMisses = 0;
	boolean isGunAligned = true;
	boolean ramRobot = false;
	boolean shotRobot = false;
	
	//run: My Robot's default behavior
	public void run() { 
		ramRobot = false;       
        // Set colours
        setColors(Color.orange, Color.orange, Color.orange, Color.magenta, Color.orange);
        
        // Robot main loop
		while(true) {
			turnGunRight(360); //scans automatically for targets
			turnGunLeft(360);
		}
	}

	//onScannedRobot: What happens when NM sees another robot
	public void onScannedRobot(ScannedRobotEvent e) {
		ramRobot = false;
		shotRobot = false;
		rotateGun = completeAngle((getHeading() + e.getBearing()) - getGunHeading()); //finds the shortest amount to turn gun to face other bot

		//Assume an energy drop between 0.0 and 3.0 is the event when the bot we have scanned has fired a bullet
		//running into walls for AdvancedRobots has a damaging effect
		//However we assume, for simplicity, that all opponenets do not take damage from hitting walls
		double energyChange = initialEnergy - e.getEnergy(); //keeps track of scanned bot's energy
		initialEnergy = e.getEnergy(); //updates bot's energy to current energy
	
		if (consecMisses <= 3) {
	       	if (e.getDistance() < 50 && getEnergy() >= 60) { //if bot is close and NM has sufficient energy
				turnGunRight(rotateGun);
				fire(3.0); //powerful bullet
				if (shotRobot == true) {
					fire(3.0); //if successful, fire again
				} 
    	    } else if (e.getDistance() >= 50 && getEnergy() >= 60) { //if bot isn't that close but NM has energy
				turnGunRight(rotateGun);
				fire(1.0); //less powerful because larger distance - more likely to miss
				if (shotRobot == true) {
					fire(2.0); //fires more powerful bullet
				}
        	    alignGun();
				turnRight(e.getBearing()); //turns to face scanned bot
				fire(1.0);
					if (shotRobot == true) {
						ahead(e.getDistance() / 2); //halves distance
						scan();
					}
	        } else { //if energy is low
				alignGun();
				turnRight(e.getBearing());
            	fire(0.1); //want to conserve energy no matter what distance we are away
				if (e.getDistance() < 100) {
            		back(200); //backs away
					alignGun();
					turnRight(completeAngle(e.getBearing() + 90)); //body is perpendicular to other bot
					turnGunRight(-90); //gun aiming at other bot
					back(50); //move around a lot to reduce possibility of getting hit
					ahead(100); //no bullets fired to conservce energy
					back(100);
					scan(); //bot likely to have moved so we need to call scan again
				}
        	}
		
				if (energyChange > 0.0 && energyChange <= 3.0){ //a bullet has been fired
					turnRight(completeAngle(e.getBearing() + 90));
					turnGunRight(-90);
					back(50);
					ahead(100);
					back(100);
					scan(); 
				}

			} else { //if we've shot too many times and not hit anything in a row we change strategy
				turnRight(e.getBearing());
				fire(0.1); //last shot before we give up on shooting!
				ahead(e.getDistance()); //want to ram the bot (no more expending energy on missing bullets
				if (ramRobot = true) {
					consecMisses = 0; //if successfully ram, we can resume shooting
				} else {
					turnRight(e.getBearing());
					ahead(e.getDistance());
				}
			}
		}   

    
	//onBulletMissed: if NM misses the robot
	public void onBulletMissed(BulletMissedEvent e) {
		System.out.println("I missed");
		consecMisses++; //we count how many times NM misses in a row
		System.out.println("Number of consecutive missed bullets = " + consecMisses);
	}

    //onHitRobot: What happens when NM hits another bot with a bullet
    public void onBulletHit(BulletHitEvent e) {
        System.out.println("I shot " + e.getName());
		consecMisses = 0; //number of consecutive missed bullets gets reset
		shotRobot = true;
    }
    
	//onHitByBullet: What happens when NM is hit by a bullet
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		System.out.println("Shot by " + e.getName());
		
        //scan();
	}
	
	//onHitRobot: What happens when NM collides with another bot
	public void onHitRobot(HitRobotEvent e) {
		ramRobot = true;
		consecMisses = 0;
	}

	//onHitWall: What happens when NM hits a wall
	public void onHitWall(HitWallEvent e) {
        if (Math.abs(e.getBearing()) < 180) {
            turnRight(completeAngle(e.getBearing() + 180)); //turns around to face the centre of the battle field
			ahead(Math.min(getBattleFieldHeight(),getBattleFieldWidth())/2); //moves half of the width or height of the battle field
        } else {
            ahead(Math.min(getBattleFieldHeight(),getBattleFieldWidth())/2);
        }
	}
	
    //completeAngle: changes angles bigger than 360 degrees to equivalent 'smaller' angle in interval [-180,180]
    public double completeAngle(double a) {
		a = ((a % 360) + 360) % 360; //finds remainder of 'large' angle -- converts into a 'small' angle
        if (a > 180) {
            a = -(360 - a); //converts angles bigger than 180 into negative equivalent
        }
		return a;
    }
	
	//alignGun: makes the gun in line with NM's heading
	public void alignGun() {
		if (getHeading() == getGunHeading()) {
			isGunAligned = true; //gun is aligned
		} else {
			isGunAligned = false; //gun is not aligned
			rotateGun = completeAngle(getHeading() - getGunHeading());
			turnGunRight(rotateGun);
		}
		
	}

    
    //onWin: NM's victory dance!
    public void onWin(WinEvent e) {
        while (true) {
            turnRight(45);
            turnLeft(45);
        }
    }
    
    
    
}
