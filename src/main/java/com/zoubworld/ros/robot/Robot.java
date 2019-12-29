/**
 * 
 */
package com.zoubworld.ros.robot;

/**
 * @author M43507
 *
 */
public class Robot implements IRobot{
	double x=0.0,y=0.0,theta=0.0;
	/**
	 * 
	 */
	public Robot() {
		// TODO Auto-generated constructor stub
	}

	  /* (non-Javadoc)
	 * @see com.zoubworld.robot.IRobot#getX()
	 */
	@Override
	public double getX() {
		return x;
	}
	/* (non-Javadoc)
	 * @see com.zoubworld.robot.IRobot#getY()
	 */
	@Override
	public double getY() {
		return y;
	}
	/* (non-Javadoc)
	 * @see com.zoubworld.robot.IRobot#getTheta()
	 */
	@Override
	public double getTheta() {
		return theta;
	}

/**
 * @param x the x to set
 */
public void setX(double x) {
	this.x = x;
}
/**
 * @param y the y to set
 */
public void setY(double y) {
	this.y = y;
}
/**
 * @param theta the theta to set
 */
public void setTheta(double theta) {
	this.theta = theta;
}
}
