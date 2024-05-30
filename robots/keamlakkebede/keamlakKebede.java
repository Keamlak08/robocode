package keamlakkebede;

import java.awt.*;
import robocode.Robot;
import robocode.util.Utils;
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
            moveRandomly();
            turnGunRight(360);
        }

    }



    private void moveRandomly(){
        if (movingForward){
            ahead(Math.random() * 200 + 100);
        } else {
            back(Math.random() * 200 + 100);
        }
        movingForward = !movingForward; //makes it so that it changes where it goes

        turnRight(Math.random() * 90 - 45); // turns 45 degree right or left, found this on yt
    }

    public void onScannedRobot(ScannedRobotEvent e){
        
        double lockOn = getHeading() + e.getBearing() - getRadarHeading();
        double copy = getPerepheral(lockOn);

        //track fire aim:
        //double getPosition = getHeading() + e.getBearing();
        //double predictAim = getPerepheral(getPosition - getGunHeading());

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
        

       
    }






    public void onHitWall(HitWallEvent event) {
        reverseDirection();
    }

    public void onHitByBullet(HitByBulletEvent event){
        turnRight(Math.random() * 50 - 25);
        reverseDirection();
    }


    public void onHitRobot(HitRobotEvent event){
        turnRight(90);
        reverseDirection();
    }

    private void reverseDirection(){
        if (movingForward){
            back(Math.random() * 100 + 50);
            movingForward = false;
        } else {
            ahead(Math.random() * 100 + 50);
            movingForward = true;
        }
    }

    //this gets the perepheral and finds how far to the left or right the robot's angle is
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