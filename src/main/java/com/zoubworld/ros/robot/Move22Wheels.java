package com.zoubworld.ros.robot;

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

/**
 * @author M43507
 * get a speed vx,vy,vtetha in m/s from RosInMsg
 * provide a speed per wheel int rosOutMsg[i]
 */
public class Move22Wheels  extends AbstractNodeMain {

	/**
	 * 
	 */
	public Move22Wheels() {
		// TODO Auto-generated constructor stub
	}
	
	String rosInMsg="/turtle1/cmd_vel";
	String rosOutMsg="/turtle1/wheelSpeed/";
	Wheel wheels[]= {
			new Wheel(-0.1, 0.0, Math.PI/2, 0.073/2,null),
			new Wheel(0.1, 0.0, -Math.PI/2, 0.073/2,null),
			};
			@Override
			public GraphName getDefaultNodeName() {
				return GraphName.of("rosjava/teleop");
			}

		
			
			geometry_msgs.Twist cmd_vel=null;
			@Override
			public void onStart(ConnectedNode connectedNode) {
				final Log log = connectedNode.getLog();
				Subscriber<geometry_msgs.Twist> subscriber = connectedNode.newSubscriber(rosInMsg, geometry_msgs.Twist._TYPE);
				final Publisher<std_msgs.Int32> publisher1 = connectedNode.newPublisher(rosOutMsg+"1",
						std_msgs.Int32._TYPE);
				final Publisher<std_msgs.Int32> publisher2 = connectedNode.newPublisher(rosOutMsg+"2",
						std_msgs.Int32._TYPE);

				subscriber.addMessageListener(new MessageListener<geometry_msgs.Twist>() {

					@Override
					public void onNewMessage(geometry_msgs.Twist cmd_vel2) {
						cmd_vel=cmd_vel2;
						// TODO Auto-generated method stub
						log.info("I heard: \"" + cmd_vel.toRawMessage() + "\"");
						
						

						
					}
				});

				// This CancellableLoop will be canceled automatically when the node shuts
				// down.
				connectedNode.executeCancellableLoop(new CancellableLoop() {
					// private int sequenceNumber;

					@Override
					protected void setup() {
						// sequenceNumber = 0;
					}

					@Override
					protected void loop() throws InterruptedException {
						std_msgs.Int32 wheelSpeed1 = null;
						std_msgs.Int32 wheelSpeed2 = null;
						wheelSpeed1 = publisher1.newMessage();
						wheelSpeed1 = publisher2.newMessage();
						Vector3 v = cmd_vel.getLinear();
						Vector3 a = cmd_vel.getAngular();
						double rpm1=v.getX()/wheels[0].getPerimeter()*Math.sin(wheels[0].getTheta0());
						double rpm2=v.getX()/wheels[1].getPerimeter()*Math.sin(wheels[1].getTheta0());
						rpm1+=v.getY()/wheels[0].getPerimeter()*Math.cos(wheels[0].getTheta0());
						rpm2+=v.getY()/wheels[1].getPerimeter()*Math.cos(wheels[1].getTheta0());
						rpm1+=wheels[0].getDistanceFromCenter()*a.getZ()/wheels[0].getPerimeter();
						rpm2-=wheels[1].getDistanceFromCenter()*a.getZ()/wheels[1].getPerimeter();
						//v.getY()
						
						wheelSpeed1.setData((int)(rpm1*4096));
						wheelSpeed2.setData((int)(rpm2*4096));
						if (wheelSpeed1 == null)
							wheelSpeed1 = publisher1.newMessage();
						// str.setData("Hello world! " + sequenceNumber);
						publisher1.publish(wheelSpeed1);
						if (wheelSpeed2 == null)
							wheelSpeed2 = publisher2.newMessage();
						// str.setData("Hello world! " + sequenceNumber);
						publisher2.publish(wheelSpeed2);
						
						// sequenceNumber++;
						Thread.sleep(100);
					}
				});
			}
}
