/*
 * Copyright (C) 2014 pvalleau.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.rosjava.com_zoubworld_odometry.com_zoubworld_odometry;

import java.awt.color.CMMException;

import org.apache.commons.logging.Log;
import org.ros.concurrent.CancellableLoop;
import org.ros.internal.message.RawMessage;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

import com.zoubworld.ros.Joy.JoyPs4;

import geometry_msgs.Vector3;
import sensor_msgs.*;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 */
public class ListenerJoy extends AbstractNodeMain {

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
		final Publisher<geometry_msgs.Twist> publisher = connectedNode.newPublisher("/cmd_vel",
				geometry_msgs.Twist._TYPE);

		subscriber.addMessageListener(new MessageListener<sensor_msgs.Joy>() {

			@Override
			public void onNewMessage(Joy joy_msg) {
				
				// TODO Auto-generated method stub
				log.info("I heard: \"" + joy_msg.toRawMessage() + "\"");
				cmd_vel = publisher.newMessage();
				Vector3 v = cmd_vel.getLinear();
				cmd_vel.setLinear(v);
				Vector3 a = cmd_vel.getAngular();
				cmd_vel.setAngular(v);
				v.setX(joy_msg.getAxes()[joy.AXIS_STICK_LEFT_UPWARDS]
						+ joy_msg.getAxes()[joy.AXIS_STICK_RIGHT_UPWARDS]);
				v.setY(joy_msg.getAxes()[joy.AXIS_STICK_LEFT_LEFTWARDS]);
				a.setZ(joy_msg.getAxes()[joy.AXIS_STICK_RIGHT_LEFTWARDS]);
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
				Thread.sleep(1000);
			}
		});

	}
}
