package com.zoubworld.ros.Joy;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import sensor_msgs.Joy;

public class JoyPs3 implements IJoyMsgUpdater {

	static public final int BUTTON_SELECT   =         0;
	static public final int  BUTTON_STICK_LEFT      =  1;
	static public final int  BUTTON_STICK_RIGHT     =  2;
	static public final int  BUTTON_START           =  3;
	static public final int  BUTTON_CROSS_UP        =  4;
	static public final int  BUTTON_CROSS_RIGHT     =  5;
	static public final int  BUTTON_CROSS_DOWN      =  6;
	static public final int  BUTTON_CROSS_LEFT      =  7;
	static public final int  BUTTON_REAR_LEFT_2     =  8;
	static public final int  BUTTON_REAR_RIGHT_2    =  9;
	static public final int  BUTTON_REAR_LEFT_1     =  10;
	static public final int BUTTON_REAR_RIGHT_1     = 11;
	static public final int  BUTTON_ACTION_TRIANGLE =  12;
	static public final int  BUTTON_ACTION_CIRCLE   =  13;
	static public final int  BUTTON_ACTION_CROSS    =  14;
	static public final int  BUTTON_ACTION_SQUARE   =  15;
	static public final int  BUTTON_PAIRING         =  16;

	static public final int  AXIS_STICK_LEFT_LEFTWARDS  =  0;
	static public final int  AXIS_STICK_LEFT_UPWARDS    =  1;
	static public final int  AXIS_STICK_RIGHT_LEFTWARDS =  2;
	static public final int  AXIS_STICK_RIGHT_UPWARDS   =  3;
	static public final int  AXIS_BUTTON_CROSS_UP       =  4;
	static public final int  AXIS_BUTTON_CROSS_RIGHT    =  5;
	static public final int  AXIS_BUTTON_CROSS_DOWN     =  6;
	static public final int AXIS_BUTTON_CROSS_LEFT      = 7;
	static public final int  AXIS_BUTTON_REAR_LEFT_2    =  8;
	static public final int  AXIS_BUTTON_REAR_RIGHT_2   =  9;
	static public final int  AXIS_BUTTON_REAR_LEFT_1    =  10;
	static public final int  AXIS_BUTTON_REAR_RIGHT_1   =  11;
	static public final int  AXIS_BUTTON_ACTION_TRIANGLE=  12;
	static public final int  AXIS_BUTTON_ACTION_CIRCLE  =  13;
	static public final int  AXIS_BUTTON_ACTION_CROSS   =  14;
	static public final int  AXIS_BUTTON_ACTION_SQUARE  =  15;
	static public final int  AXIS_ACCELEROMETER_LEFT    =  16;
	static public final int  AXIS_ACCELEROMETER_FORWARD  = 17;
	static public final int  AXIS_ACCELEROMETER_UP      =  18;
	static public final int  AXIS_GYRO_YAW              =  19;
	private int joynumber=0;
	private ControllerManager controllers=null;
	/* (non-Javadoc)
	 * @see com.github.rosjava.com_zoubworld_odometry.com_zoubworld_odometry.IJoyMsgUpdater#start(int)
	 */
	@Override
	public void start(int joynumber)
	{
		this.joynumber=joynumber;
		ControllerManager controllers = new ControllerManager();
		controllers.initSDLGamepad();
	}
	/* (non-Javadoc)
	 * @see com.github.rosjava.com_zoubworld_odometry.com_zoubworld_odometry.IJoyMsgUpdater#stop()
	 */
	@Override
	public void stop()
	{

		controllers.quitSDLGamepad();
		
	}

	public ControllerState getState()
	{
		if(controllers==null)
			return null;
		return controllers.getState(joynumber);
	}
	/* (non-Javadoc)
	 * @see com.github.rosjava.com_zoubworld_odometry.com_zoubworld_odometry.IJoyMsgUpdater#updateJoy(sensor_msgs.Joy)
	 */
	@Override
	public void updateJoyMsg(Joy joy_msg)
	{
		apply(joy_msg, getState());
	}
	/** copy currState into joy_msg
	 * */
	void apply(	Joy joy_msg,ControllerState currState)
	{
		if(currState==null)
			return;
		int[] buttons=new int[14];
		buttons[BUTTON_ACTION_CROSS]=currState.a?0:1;//A-X
		buttons[BUTTON_ACTION_CIRCLE]=currState.b?0:1;//B-O
		buttons[BUTTON_ACTION_TRIANGLE]=currState.y?0:1;//Y-T
		buttons[BUTTON_ACTION_SQUARE]=currState.x?0:1;//X-C
		
		buttons[BUTTON_START]=currState.start?0:1;
		buttons[BUTTON_PAIRING]=currState.back?0:1;
		buttons[BUTTON_SELECT]=currState.guide?0:1;
		
	//	buttons[BUTTON_PADDLE]=0;
		buttons[BUTTON_REAR_LEFT_2]=currState.leftTrigger>0.5?1:0;
		buttons[BUTTON_REAR_RIGHT_2]=currState.rightTrigger>0.5?1:0;
	
		buttons[BUTTON_REAR_LEFT_1]=currState.lb?0:1;
		buttons[BUTTON_REAR_RIGHT_1]=currState.rb?0:1;
		
		buttons[BUTTON_STICK_LEFT]=currState.leftStickClick?0:1;
		buttons[BUTTON_STICK_RIGHT]=currState.rightStickClick?0:1;
		joy_msg.setButtons(buttons);
		float[] axes= new float[14];
		axes[AXIS_STICK_LEFT_LEFTWARDS]=currState.leftStickX;
		axes[AXIS_STICK_LEFT_UPWARDS]=currState.leftStickY;
		axes[AXIS_STICK_RIGHT_LEFTWARDS]=currState.rightStickX;
		axes[AXIS_STICK_RIGHT_UPWARDS]=currState.rightStickY;
		
		axes[AXIS_BUTTON_REAR_LEFT_2]=currState.leftTrigger;
		axes[AXIS_BUTTON_REAR_RIGHT_2]=currState.rightTrigger;
		
	//	axes[AXIS_BUTTON_CROSS_LEFTWARDS]=(float) (currState.dpadUp?(currState.dpadDown?0:-1.0):1.0);
	//	axes[AXIS_BUTTON_CROSS_UPWARDS]=(float) (currState.dpadRight?(currState.dpadLeft?0:-1.0):1.0);
		
	//	axes[AXIS_GYRO_XAW]=(float) 0.0;
		axes[AXIS_GYRO_YAW]=(float) 0.0;
	//	axes[AXIS_GYRO_ZAW]=(float) 0.0;
		axes[AXIS_ACCELEROMETER_LEFT]=(float) 0.0;
		axes[AXIS_ACCELEROMETER_FORWARD]=(float) 0.0;
		axes[AXIS_ACCELEROMETER_UP]=(float) 0.0;
			
		joy_msg.setAxes(axes);
	}

}
