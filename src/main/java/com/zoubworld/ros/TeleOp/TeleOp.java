/**
 * 
 */
package com.zoubworld.ros.TeleOp;

import org.apache.commons.logging.Log;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import com.zoubworld.ros.Joy.JoyPs4;

import geometry_msgs.Vector3;
import sensor_msgs.Joy;

/**
 * @author M43507
 *
 */
public class TeleOp  extends AbstractNodeMain {

	/**
	 * 
	 */
	public TeleOp() {
		// TODO Auto-generated constructor stub
	}
	

			@Override
			public GraphName getDefaultNodeName() {
				return GraphName.of("rosjava/teleop");
			}

			JoyPs4 joy;// define as PS4

			geometry_msgs.Twist cmd_vel = null;

			@Override
			public void onStart(ConnectedNode connectedNode) {
				final Log log = connectedNode.getLog();
				Subscriber<sensor_msgs.Joy> subscriber = connectedNode.newSubscriber("/joy", sensor_msgs.Joy._TYPE);
				final Publisher<geometry_msgs.Twist> publisher = connectedNode.newPublisher("/turtle1/cmd_vel",
						geometry_msgs.Twist._TYPE);

				subscriber.addMessageListener(new MessageListener<sensor_msgs.Joy>() {

					@Override
					public void onNewMessage(Joy joy_msg) {
						
						// TODO Auto-generated method stub
						log.info("I heard: \"" + joy_msg.toRawMessage() + "\"");
						cmd_vel = publisher.newMessage();
						Vector3 v = cmd_vel.getLinear();
						Vector3 a = cmd_vel.getAngular();
						v.setX(joy_msg.getAxes()[joy.AXIS_STICK_LEFT_UPWARDS]
								+ joy_msg.getAxes()[joy.AXIS_STICK_RIGHT_UPWARDS]);
						v.setY(joy_msg.getAxes()[joy.AXIS_STICK_LEFT_LEFTWARDS]);
						a.setZ(joy_msg.getAxes()[joy.AXIS_STICK_RIGHT_LEFTWARDS]);
						cmd_vel.setLinear(v);
						cmd_vel.setAngular(a);
						
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
						if (cmd_vel == null)
							cmd_vel = publisher.newMessage();
						// str.setData("Hello world! " + sequenceNumber);
						publisher.publish(cmd_vel);
						// sequenceNumber++;
						Thread.sleep(100);
					}
				});

			}	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
