package com.zoubworld.ros.Joy;

import sensor_msgs.Joy;
/**
 * this class manage the translation from jamepad.jar to ros.sensor_msgs.Joy
 * for the Xbox joy
 * it offer also some define x to play with 
 * sensor_msgs.Joy.getAxes()[x]
 * sensor_msgs.Joy.getButtons()[x]
*/
public class JoyXbox  implements IJoyMsgUpdater {

	@Override
	public void start(int joynumber) {
		// TODO Auto-generated method stub
		
				
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateJoyMsg(Joy joy_msg) {
		// TODO Auto-generated method stub
		
	}

}
