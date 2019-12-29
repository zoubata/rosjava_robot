package com.zoubworld.ros.Joy;

import org.ros.internal.message.RawMessage;
import org.ros.message.Time;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

import sensor_msgs.Joy;
import std_msgs.Header;

public class JoyPs4 implements IJoyMsgUpdater {

	static public final int BUTTON_OPTION   =         9;//option
	static public final int  BUTTON_STICK_LEFT      =  10;
	static public final int  BUTTON_STICK_RIGHT     =  11;
	static public final int  BUTTON_START           =  12;//ps4
	static public final int  BUTTON_PADDLE        =  13;
	/*
	static public final int  BUTTON_CROSS_UP        =  ;
	static public final int  BUTTON_CROSS_RIGHT     =  ;
	static public final int  BUTTON_CROSS_DOWN      =  ;
	static public final int  BUTTON_CROSS_LEFT      =  ;*/
	static public final int  BUTTON_REAR_LEFT_2     =  6;
	static public final int  BUTTON_REAR_RIGHT_2    =  7;
	static public final int  BUTTON_REAR_LEFT_1     =  4;
	static public final int  BUTTON_REAR_RIGHT_1     = 5;
	static public final int  BUTTON_ACTION_TRIANGLE =  3;
	static public final int  BUTTON_ACTION_CIRCLE   =  2;
	static public final int  BUTTON_ACTION_CROSS    =  1;
	static public final int  BUTTON_ACTION_SQUARE   =  0;
	static public final int  BUTTON_SHARE         =  8;//share

	static public final int  AXIS_STICK_LEFT_LEFTWARDS  =  0;
	static public final int  AXIS_STICK_LEFT_UPWARDS    =  1;
	static public final int  AXIS_STICK_RIGHT_LEFTWARDS =  2;
	static public final int  AXIS_STICK_RIGHT_UPWARDS   =  5;
	static public final int  AXIS_BUTTON_CROSS_UPWARDS       =  10;
/*	static public final int  AXIS_BUTTON_CROSS_RIGHT    =  10;
	static public final int  AXIS_BUTTON_CROSS_DOWN     =  9;*/
	static public final int  AXIS_BUTTON_CROSS_LEFTWARDS      = 9;
	static public final int  AXIS_BUTTON_REAR_LEFT_2    =  3;
	static public final int  AXIS_BUTTON_REAR_RIGHT_2   =  4;
	/*
	static public final int  AXIS_BUTTON_REAR_LEFT_1    =  10;
	static public final int  AXIS_BUTTON_REAR_RIGHT_1   =  11;*//*
	static public final int  AXIS_BUTTON_ACTION_TRIANGLE=  12;
	static public final int  AXIS_BUTTON_ACTION_CIRCLE  =  13;
	static public final int  AXIS_BUTTON_ACTION_CROSS   =  14;
	static public final int  AXIS_BUTTON_ACTION_SQUARE  =  15;*/
	static public final int  AXIS_ACCELEROMETER_LEFT    =  6;
	static public final int  AXIS_ACCELEROMETER_FORWARD  = 7;
	static public final int  AXIS_ACCELEROMETER_UP      =  8;
	static public final int  AXIS_GYRO_XAW              =  13;
	static public final int  AXIS_GYRO_YAW              =  11;
	static public final int  AXIS_GYRO_ZAW              =  12;
	private int joynumber=0;
	private ControllerManager controllers=null;
	/* (non-Javadoc)
	 * @see com.github.rosjava.com_zoubworld_odometry.com_zoubworld_odometry.IJoyMsgUpdater#start(int)
	 */
	@Override
	public void start(int joynumber)
	{
		this.joynumber=joynumber;
		controllers = new ControllerManager();
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
		ControllerState j = getState();
		Time t=Time.fromNano(System.nanoTime());
		
		apply(joy_msg, j);
		Header arg0=joy_msg.getHeader();
			
	
		arg0.setStamp(t);
		joy_msg.setHeader(arg0);
	}
	/** copy currState into joy_msg
	 * */
	void apply(	Joy joy_msg,ControllerState currState)
	{
		if(currState==null)
			return;
		int[] buttons=new int[14];
		if (currState.isConnected)
		{buttons[BUTTON_ACTION_CROSS]=currState.a?1:0;//A-X
		buttons[BUTTON_ACTION_CIRCLE]=currState.b?1:0;//B-O
		buttons[BUTTON_ACTION_TRIANGLE]=currState.y?1:0;//Y-T
		buttons[BUTTON_ACTION_SQUARE]=currState.x?1:0;//X-C
		
		buttons[BUTTON_OPTION]=currState.start?1:0;
		buttons[BUTTON_SHARE]=currState.back?1:0;
		buttons[BUTTON_START]=currState.guide?1:0;
		
		buttons[BUTTON_PADDLE]=0;
		buttons[BUTTON_REAR_LEFT_2]=currState.leftTrigger>0.5?1:0;
		buttons[BUTTON_REAR_RIGHT_2]=currState.rightTrigger>0.5?1:0;
	
		buttons[BUTTON_REAR_LEFT_1]=currState.lb?1:0;
		buttons[BUTTON_REAR_RIGHT_1]=currState.rb?1:0;
		
		buttons[BUTTON_STICK_LEFT]=currState.leftStickClick?1:0;
		buttons[BUTTON_STICK_RIGHT]=currState.rightStickClick?1:0;
		}
		else
			for(int i=0;i<buttons.length;i++)
				buttons[i]=0;
				joy_msg.setButtons(buttons);
		float[] axes= new float[14];
if(currState.isConnected)
	{axes[AXIS_STICK_LEFT_LEFTWARDS]=currState.leftStickX;
		axes[AXIS_STICK_LEFT_UPWARDS]=currState.leftStickY;
		axes[AXIS_STICK_RIGHT_LEFTWARDS]=currState.rightStickX;
		axes[AXIS_STICK_RIGHT_UPWARDS]=currState.rightStickY;
		
		axes[AXIS_BUTTON_REAR_LEFT_2]=currState.leftTrigger;
		axes[AXIS_BUTTON_REAR_RIGHT_2]=currState.rightTrigger;
		
		axes[AXIS_BUTTON_CROSS_LEFTWARDS]=(float) (currState.dpadUp?1.0:(currState.dpadDown?-1.0:0));
		axes[AXIS_BUTTON_CROSS_UPWARDS]=(float) (currState.dpadRight?1.0:(currState.dpadLeft?-1.0:0));
		
		axes[AXIS_GYRO_XAW]=(float) 0.0;
		axes[AXIS_GYRO_YAW]=(float) 0.0;
		axes[AXIS_GYRO_ZAW]=(float) 0.0;
		axes[AXIS_ACCELEROMETER_LEFT]=(float) 0.0;
		axes[AXIS_ACCELEROMETER_FORWARD]=(float) 0.0;
		axes[AXIS_ACCELEROMETER_UP]=(float) 0.0;
		
		for(int i=0;i<axes.length;i++)//remove zero not well done
		axes[i]=Math.abs(axes[i])>0.05?axes[i]:0;
	}
	else
		for(int i=0;i<buttons.length;i++)
			axes[i]=0;

		joy_msg.setAxes(axes);
	}
}
