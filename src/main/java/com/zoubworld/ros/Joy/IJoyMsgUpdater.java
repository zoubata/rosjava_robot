package com.zoubworld.ros.Joy;

import sensor_msgs.Joy;

public interface IJoyMsgUpdater {

	void start(int joynumber);

	void stop();

	void updateJoyMsg(Joy joy_msg);

}