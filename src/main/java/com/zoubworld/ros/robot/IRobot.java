package com.zoubworld.ros.robot;

public interface IRobot {

	/**
	 * @return the x
	 */
	double getX();

	/**
	 * @return the y
	 */
	double getY();

	/**
	 * @return the theta
	 */
	double getTheta();

	void setY(double d);

	void setX(double d);
	public void setTheta(double theta);

}