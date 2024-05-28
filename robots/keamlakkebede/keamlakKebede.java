package keamlakkebede;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;
import robocode.Robot;
import robocode.*;
//import java.util.List;
//import java.util.*;

public class keamlakKebede extends Robot{
    
    boolean movingForward;
    
    public void run(){
        //setup

        setColors(new Color(0, 0, 0), new Color(0,0,0), new Color(0,0,0));
        setScanColor(new Color(0,0,0));
        
        movingForward = true;
        setAdjustGunForRobotTurn(true);
        while(true){
            turnGunRight(360);
            moveRandomly();


            /* 
            before moveRandom():
            ahead(Math.random() * 300);
            movingForward = true;
            turnRight(Math.random() * 180);
            back(Math.random() * 100);
            turnLeft(Math.random() * 200);
            */
            

        }

    }

    private void moveRandomly(){
        if (movingForward){
            ahead(Math.random() * 200 + 100);
        } else {
            back(Math.random() * 200 + 100);
        }
        movingForward = !movingForward; //makes it so that it changes where it goes

        turnRight(Math.random() * 180 - 90); // turns 45 degree right or left, found this on yt
    }

    public void onScannedRobot(ScannedRobotEvent e){
        
        double lockOn = getHeading() + e.getBearing() - getRadarHeading();
        double copy = getPerepheral(lockOn);
        
        if (Math.abs(copy) > Rules.GUN_TURN_RATE){
        turnRight(lockOn);
        }
        turnGunRight(copy);
        
        //took this from "Fire" sample robot :)
        if(e.getDistance() < 50 && getEnergy() > 75){
            fire(6);
        //conserving energy adaption
        } else if(getEnergy() < 20){
            fire(0.5);
        } else if (getGunHeat() > 0){
            turnGunRight(copy);
        } else {
        //takes the lesser number | used chatGPT for the algorithm, I forgot "Math.min()" existed but i totally understand how this works
            fire(Math.min(400 / e.getDistance(), 6));
        }
        
        /* 
        //took this from "Track Fire"
        double absoluteBearing = getHeading() + e.getBearing();
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
        if (Math.abs(bearingFromGun) <= 3){
            turnGunRight(bearingFromGun);


            if (getGunHeat() == 0){
                fire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
            }
        } else {
            turnGunRight(bearingFromGun);
        }
        */

        //if the robot is far ahead it aims a bit ahead of it
        //if (e.getDistance() > 50){
        //    lockOn += 1;
        //}else if(e.getDistance() < 50){
        //    lockOn = getHeading() + e.getBearing() - getRadarHeading();
        //}

        //turnRight(lockOn);
        //turnGunRight(lockOn);
    }

    public void onHitWall(HitWallEvent event) {
        reverseDirection();
    }

    public void onBulletHit(BulletHitEvent event){
        reverseDirection();
    }

    private void reverseDirection(){
        if (movingForward){
            back(Math.random() * 200 + 100);
            movingForward = false;
        } else {
            ahead(Math.random() * 200 + 100);
            movingForward = true;
        }
    }

    //this gets the perepheral and finds how far to the left or right the robot was
    //I used chatgpt to help with the logic of this (I couldn't find how to do this myself)
    private double getPerepheral(double angle){
        while (angle > 180){
            angle -= 360;
        }
        while (angle < -180){
            angle += 360;
        }
        return angle;
    }

    public void onWin(WinEvent e){
        for (int i = 0; i < 3; i++){
            //yippie
            turnRadarRight(360);
        }
    }
}