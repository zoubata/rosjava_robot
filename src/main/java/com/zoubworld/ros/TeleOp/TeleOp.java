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
	String rosInMsg="/joy";
	String rosOutMsg="/turtle1/cmd_vel";
	String rosOutMsg2="/turtle1/camp";
	std_msgs.String team;
	String rosOutMsg3="/turtle1/mode";
	std_msgs.String mode;
	@Override
	public GraphName getDefaultNodeName() {
		return GraphName.of("rosjava/teleop");
	}

	JoyPs4 joy;// define as PS4

	geometry_msgs.Twist cmd_vel = null;

	@Override
	public void onStart(ConnectedNode connectedNode) {
		final Log log = connectedNode.getLog();
		Subscriber<sensor_msgs.Joy> subscriber = connectedNode.newSubscriber(rosInMsg, sensor_msgs.Joy._TYPE);

		final Publisher<geometry_msgs.Twist> publisher = connectedNode.newPublisher(rosOutMsg,
				geometry_msgs.Twist._TYPE);
		final Publisher<std_msgs.String> publisher2 = connectedNode.newPublisher(rosOutMsg2,
				std_msgs.String._TYPE);
		final Publisher<std_msgs.String> publisher3 = connectedNode.newPublisher(rosOutMsg3,
				std_msgs.String._TYPE);

		subscriber.addMessageListener(new MessageListener<sensor_msgs.Joy>() {
			Joy joy_previousmsg;
			@Override
			public void onNewMessage(Joy joy_msg) {
				if(joy_previousmsg==null)
					joy_previousmsg=joy_msg;
				
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



				if (joy_msg.getButtons()[JoyPs4.BUTTON_OPTION]!=joy_previousmsg.getButtons()[JoyPs4.BUTTON_OPTION])
					if (joy_msg.getButtons()[JoyPs4.BUTTON_OPTION]==1)
						if (team.getData().equals("Yellow"))
							team.setData("Blue");
						else
							team.setData("Yellow");

				if (joy_msg.getButtons()[JoyPs4.BUTTON_START]!=joy_previousmsg.getButtons()[JoyPs4.BUTTON_START])
					if (joy_msg.getButtons()[JoyPs4.BUTTON_START]==1)
						if (mode.getData().equals("auto"))
							mode.setData("manual");
						else
							mode.setData("auto");

				joy_previousmsg=joy_msg;
			}
		});

		// This CancellableLoop will be canceled automatically when the node shuts
		// down.
		connectedNode.executeCancellableLoop(new CancellableLoop() {
			// private int sequenceNumber;

			@Override
			protected void setup() {

				if (cmd_vel == null)
					cmd_vel = publisher.newMessage();
				if (team == null)
					team = publisher2.newMessage();
				team.setData("Blue");
				if (mode == null)
					mode = publisher3.newMessage();
				mode.setData("manual");
			}

			@Override
			protected void loop() throws InterruptedException {

				// str.setData("Hello world! " + sequenceNumber);
				if (!mode.getData().equals("auto"))
				{
				publisher.publish(cmd_vel);
				
				}
				publisher2.publish(team);
				publisher3.publish(mode);

				// sequenceNumber++;
				Thread.sleep(100);
			}
		});
	}
}
