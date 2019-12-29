/*

 */

package com.zoubworld.ros.robot;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import geometry_msgs.Vector3;
import sensor_msgs.*;
/**
 * A simple {@link Subscriber} {@link NodeMain}.
 * it manages the message linear.x,linear.y and angular.z for  robot 
 * with  omni-directional wheels
 */
public class TurtlePos extends AbstractNodeMain implements IRobot {

	String rosmsg="/turtle1/pos";
  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("rosjava/TurtleSim");
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
double x=0.0,y=0.0,theta=0.0;
  long time=Calendar.getInstance().getTime().getTime();
  @Override
  public void onStart(ConnectedNode connectedNode) {
    final Log log = connectedNode.getLog();
    TurtleFrame tf2=new TurtleFrame();
    tf2.setRobot(this);
     tf2.fireUpScreen();
    Subscriber<geometry_msgs.Twist> subscriber = connectedNode.newSubscriber(rosmsg, geometry_msgs.Twist._TYPE);
    subscriber.addMessageListener(new MessageListener<geometry_msgs.Twist>() {
    

	@Override
	public void onNewMessage(geometry_msgs.Twist message) {
		Vector3 v = message.getLinear();
		Vector3 a = message.getAngular();
		long time2=Calendar.getInstance().getTime().getTime();
		long dt=time2-time;
		x=v.getX();
		
		y=v.getY();
		
		theta=a.getZ();
		theta=theta%(Math.PI*2);
		
		// TODO Auto-generated method stub
		log.info("I heard: \"" + x+", "+y+", "+theta + "\"");
		time=time2;
	}
    });
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
