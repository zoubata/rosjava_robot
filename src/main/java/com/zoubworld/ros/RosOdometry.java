package com.zoubworld.ros;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import com.zoubworld.java.math.Matrix;
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
public class RosOdometry  extends AbstractNodeMain {

	/**
	 * 
	 */
	public RosOdometry() {
		// TODO Auto-generated constructor stub
	}
	
	String rosInMsg="/turtle1/wheelEncodeur/";
	String rosOutMsg="/turtle1/pos/";
	long time=Calendar.getInstance().getTime().getTime();
	  Wheel wheels[]= {
			new Wheel(-0.1, 0.0, Math.PI/2, 0.073/2,220),
			new Wheel(0.1, 0.0, -Math.PI/2, 0.073/2,220),
			};
	  geometry_msgs.Twist location;
	std_msgs.Int32[] wheelEncodeur =null;
			@Override
			public GraphName getDefaultNodeName() {
				return GraphName.of("rosjava/teleop");
			}

		
			
			Int32[] EncoderValue=null;
			@Override
			public void onStart(ConnectedNode connectedNode) {
				final Log log = connectedNode.getLog();
				Subscriber<std_msgs.Int32>[] subscriber =new Subscriber[wheels.length];
				for(int i=0;i<wheels.length;i++)
						subscriber[i] = connectedNode.newSubscriber(rosInMsg+i, std_msgs.Int32._TYPE);
				
				final Publisher<geometry_msgs.Twist> publisher =connectedNode.newPublisher(rosOutMsg,						
						geometry_msgs.Twist._TYPE);
				
				
						for(int i=0;i<wheels.length;i++)
						{
						final int j=i;
				subscriber[i].addMessageListener(new MessageListener<std_msgs.Int32>() {
					int index=j;
					@Override
					public void onNewMessage(std_msgs.Int32 tick) {
						if(EncoderValue==null)
							EncoderValue=new Int32[wheels.length];
						EncoderValue[index]=tick;
						// TODO Auto-generated method stub
						log.info("I heard: \"" + tick.toRawMessage() + "\"");
					}
				});}
						
				// This CancellableLoop will be canceled automatically when the node shuts
				// down.
				connectedNode.executeCancellableLoop(new CancellableLoop() {
					// private int sequenceNumber;
					Odometry2 o;
					@Override
					protected void setup() {
						wheelEncodeur = new std_msgs.Int32[wheels.length] ;
						o=new Odometry2();
						o.init2();	
						location=publisher.newMessage();
						}

					@Override
					protected void loop() throws InterruptedException {
						long time2=Calendar.getInstance().getTime().getTime();
						long dt=time2-time;
						if(EncoderValue!=null)
						for(int i=0;i<wheels.length;i++)
							if(EncoderValue[i]!=null)
						{
								//position
							if (location==null)
								location = publisher.newMessage();
							//todo dtick
							int[]	tickperseconde={EncoderValue[0].getData(),EncoderValue[1].getData()};
								Matrix mWrot = o.TickTowhellRotationSpeed(tickperseconde);
								Matrix l = o.robotLinearSpeed(o.whellLinearSpeed(mWrot));
								location.getAngular().setZ(l.getData()[0][2]);	
								location.getLinear().setX(l.getData()[0][0]);	
								location.getLinear().setY(l.getData()[0][1]);	
								
								//loc+=dtick				
						
							publisher.publish(location);			
							
							
						}	
						time=time2;
						// sequenceNumber++;
						Thread.sleep(100);
					}
				});
			}
}
