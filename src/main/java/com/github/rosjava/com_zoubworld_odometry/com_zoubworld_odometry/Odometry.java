package com.github.rosjava.com_zoubworld_odometry.com_zoubworld_odometry;

import java.util.ArrayList;
import java.util.List;

import org.ros.internal.message.RawMessage;

import geometry_msgs.Pose;
import geometry_msgs.PoseStamped;
import geometry_msgs.Twist;
import geometry_msgs.TwistStamped;
import geometry_msgs.Vector3;
import std_msgs.Float32;
import std_msgs.Header;
import std_msgs.Int32;
/**
 * we support,
 * z=0, wheel place orthogonaly to the center of robot, center at (0,0,0)
 * */
public class Odometry {
	Twist velocities;
	Vector3 accelerations;
	List<TwistStamped> Memorymove=new ArrayList();
	List<PoseStamped> Memorypose=new ArrayList();
	Pose WheelLocation[]=new  Pose[3];	
	/**
	 * increment in meter =encodertick/totalTick*Rwheel*2*PI
	 * */
	public Twist getTwist(Float32 wheelmove[])
	{
		 Twist t = new Twist() {
			
			@Override
			public RawMessage toRawMessage() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setLinear(Vector3 arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setAngular(Vector3 arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Vector3 getLinear() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Vector3 getAngular() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		double taz=0.0,tx=0.0,ty=0.0;
		for(int i=0;i<wheelmove.length;i++)
		{
		double wx = WheelLocation[i].getPosition().getX();
		double wy = WheelLocation[i].getPosition().getY();
		double wz = WheelLocation[i].getPosition().getZ();
		double az = WheelLocation[i].getOrientation().getZ();
		double waz=Math.atan(wy/wx)+Math.PI/2;//theorical best orientation of wheel
		double N = Math.sqrt(wx*wx+wy*wy+wz*wz);
		double a=Math.atan(wheelmove[i].getData()*Math.cos(az-waz)/2/N)*2;
		taz+=a;//twist in rotation
		tx+=wheelmove[i].getData()*Math.cos(az);
		ty+=wheelmove[i].getData()*Math.sin(az);
		}
		return taz,tx,ty;
		}
/**
 * twist is the movement of the robot.
 * */
	public void updatePosition(TwistStamped twist,PoseStamped pose )
	{
		Memorymove.add(twist);
		Memorypose.add(pose);
		
		pose.getPose().getPosition().getX();
		twist.getTwist().getLinear().getX();
		
		
	}
	
	
}





















