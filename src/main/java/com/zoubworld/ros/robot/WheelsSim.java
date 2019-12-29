package com.zoubworld.ros.robot;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import com.zoubworld.robot.Wheel;
import com.zoubworld.ros.Joy.JoyPs4;

import geometry_msgs.Vector3;
import sensor_msgs.Joy;
import std_msgs.Int32;

/** simulate the behavior of a wheel from a wheel speed order it return the encoder position and speed.
 * @author M43507
 * get a speed vx,vy,vtetha from 
 * provide Wheel1speed, Wheel2speed
 */
public class WheelsSim  extends AbstractNodeMain {

	/**
	 * 
	 */
	public WheelsSim() {
		// TODO Auto-generated constructor stub
	}
	
	String rosInMsg="/turtle1/wheelSpeed";
	String rosOutMsg="/turtle1/wheelEncodeur/";
	String rosOutMsg2="/turtle1/wheelEncodeur/speed/";
	long time=Calendar.getInstance().getTime().getTime();
	  Wheel wheels[]= {
			new Wheel(-0.1, 0.0, Math.PI/2, 0.073/2,220),
			new Wheel(0.1, 0.0, -Math.PI/2, 0.073/2,220),
			};
	
	std_msgs.Int32[] wheelEncodeur =null;
			@Override
			public GraphName getDefaultNodeName() {
				return GraphName.of("rosjava/teleop");
			}

		
			
			Int32[] rpmSpeed=null;
			@Override
			public void onStart(ConnectedNode connectedNode) {
				final Log log = connectedNode.getLog();
				Subscriber<std_msgs.Int32>[] subscriber =new Subscriber[wheels.length];
				for(int i=0;i<wheels.length;i++)
						subscriber[i] = connectedNode.newSubscriber(rosInMsg+i, std_msgs.Int32._TYPE);
				
				final Publisher<std_msgs.Int32>[] publisher =new Publisher[wheels.length];
				for(int i=0;i<wheels.length;i++)						
				publisher[i] = connectedNode.newPublisher(rosOutMsg+i,						
						std_msgs.Int32._TYPE);
						
				final Publisher<std_msgs.Int32>[] publisher2 =new Publisher[wheels.length];
				for(int i=0;i<wheels.length;i++)						
				publisher2[i] = connectedNode.newPublisher(rosOutMsg2+i,						
				std_msgs.Int32._TYPE);
				
						for(int i=0;i<wheels.length;i++)
						{
						final int j=i;
				subscriber[i].addMessageListener(new MessageListener<std_msgs.Int32>() {
					int index=j;
					@Override
					public void onNewMessage(std_msgs.Int32 cmd_vel2) {
						if(rpmSpeed==null)
							rpmSpeed=new Int32[wheels.length];
						rpmSpeed[index]=cmd_vel2;
						// TODO Auto-generated method stub
						log.info("I heard: \"" + cmd_vel2.toRawMessage() + "\"");
					}
				});}
						
				// This CancellableLoop will be canceled automatically when the node shuts
				// down.
				connectedNode.executeCancellableLoop(new CancellableLoop() {
					// private int sequenceNumber;

					@Override
					protected void setup() {
						wheelEncodeur = new std_msgs.Int32[wheels.length] ;
					}

					@Override
					protected void loop() throws InterruptedException {
						long time2=Calendar.getInstance().getTime().getTime();
						long dt=time2-time;
						if(rpmSpeed!=null)
						for(int i=0;i<wheels.length;i++)
							if(rpmSpeed[i]!=null)
						{
							//speed
							Int32[] wheelEncodeurSpeed= new std_msgs.Int32[wheels.length] ;
							wheelEncodeurSpeed[i] = publisher2[i].newMessage();						
							wheelEncodeurSpeed[i].setData((int)(rpmSpeed[i].getData()*dt*wheels[i].getTickPerRotation().intValue()));
							
							publisher2[i].publish(wheelEncodeurSpeed[i]);
							//position
							if (wheelEncodeur[i]==null)
							wheelEncodeur[i] = publisher[i].newMessage();						
							wheelEncodeur[i].setData(wheelEncodeur[i].getData()+wheelEncodeurSpeed[i].getData());
							
							publisher[i].publish(wheelEncodeur[i]);			
							
							
						}	
						time=time2;
						// sequenceNumber++;
						Thread.sleep(100);
					}
				});
			}
}
