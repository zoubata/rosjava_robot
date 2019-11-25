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

package com.zoubworld.ros.Joy;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

/**
 * A simple {@link Publisher} {@link NodeMain}.
 */
public class JoyPublisher extends AbstractNodeMain {
   	private IJoyMsgUpdater joy=new JoyPs4();
	private int portNumber=0;

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("rosjava/Joy");
  }
  /**
 * @return the joy
 */
public IJoyMsgUpdater getJoy() {
	return joy;
}

/**
 * @param joy the joy to set
 * setJoy(new JoyPS4)
 * setPort(0);//jy0
 * setRefreshRateMs(10);//10MS
 */
public void setJoy(IJoyMsgUpdater joy) {
	this.joy = joy;
}

/**
 * @return the portNumber
 */
public int getPortNumber() {
	return portNumber;
}

/**
 * @param portNumber the portNumber to set
 */
public void setPortNumber(int portNumber) {
	this.portNumber = portNumber;
}

  @Override
  public void onStart(final ConnectedNode connectedNode) {
    final Publisher<sensor_msgs.Joy> publisher =
        connectedNode.newPublisher("/joy", sensor_msgs.Joy._TYPE);
    setJoy(new JoyPs4());
    setRefreshRateMs(100);
    setPortNumber(0);
    joy.start(portNumber);
    // This CancellableLoop will be canceled automatically when the node shuts
    // down.
    connectedNode.executeCancellableLoop(new CancellableLoop() {
 //     private int sequenceNumber;
       @Override
      protected void setup() {
     
        
        
      }


	@Override
      protected void loop() throws InterruptedException {
    	sensor_msgs.Joy joy_msg = publisher.newMessage();
        //str.setData("Hello world! " + sequenceNumber);
        joy.updateJoyMsg(joy_msg);
    	publisher.publish(joy_msg);
      
       
		Thread.sleep(RefreshRateMs);
      }
    });
  }
  int RefreshRateMs=10;
/**
 * @return the refreshRateMs
 */
public int getRefreshRateMs() {
	return RefreshRateMs;
}

/**
 * @param refreshRateMs the refreshRateMs to set
 */
public void setRefreshRateMs(int refreshRateMs) {
	RefreshRateMs = refreshRateMs;
}
}
