package com.zoubworld.ros;



import com.zoubworld.geometry.Circle;
import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Segment;
import com.zoubworld.java.math.Matrix;

import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.java.svg.SvgRender;
import com.zoubworld.java.utils.svg.ArcCircle;
import com.zoubworld.robot.Wheel;
import com.zoubworld.ros.robot.IRobot;
import com.zoubworld.utils.JavaUtils;

public class Odometry2 implements ItoSvg{

	public static void main(String[] args) {
		Odometry2 v = new Odometry2();
		v.init3();
		System.out.println(v.m);
		System.out.println(v.mi);

		System.out.println(v.m.multiply(v.mi));
		System.out.println(v.WheelsToSvg(null));
		SvgRender r=new SvgRender();
		r.setpMin(new Point(-1.5,-1));//m=1300,1/3->330
		r.setpMax(new Point(1.5,1));
		r.addObject(v);
		Point p=new Point(0,0);
		r.addObject(p);
		Circle c=new Circle(p,0.1);
		r.addObject(c);
	
		Segment s;
		s=new Segment(-3.0,0.0,3.0,0.0);
		r.addObject(s);
		s=new Segment(0.0,-3.0,0.0,3.0);
		r.addObject(s);
/*
		s=new Segment(-3.0,-.10,3.0,-.10);
		r.addObject(s);
		s=new Segment(-3.0,.10,3.0,.10);
		r.addObject(s);
		s=new Segment(-3.0,-.20,3.0,-.20);
		r.addObject(s);
		s=new Segment(-3.0,.20,3.0,.20);
		r.addObject(s);
		
		

		s=new Segment(-.10,-3.0,-.10,3.0);
		r.addObject(s);
		s=new Segment(.10,-3.0,.10,3.0);
		r.addObject(s);
		s=new Segment(-.20,-3.0,-.20,3.0);
		r.addObject(s);
		s=new Segment(.20,-3.0,.20,3.0);
		r.addObject(s);
		
		
		s=new Segment(-2.0,-2.0,2.0,2.0);
		r.addObject(s);
		s=new Segment(2.0,-2.0,-2.0,2.0);
		r.addObject(s);
		*/
		JavaUtils.saveAs("C:\\Temp\\svg\\try.svg", r.toSvg());
	}
	public Matrix getMv(double vx,double vy,double va)
	{
		Matrix mr=new Matrix(3);
		return mr;
		
	}
	
	
	public String robotToSvg(Matrix robotLinearSpeed)
	{
		String s="";
		double l=0.11;//compute from well location
	Point p=new Point(0,0);
	Circle c=new Circle(p,l);
	s+=c.toSvg();
	double x=robotLinearSpeed.getData()[0][0];
	double y=robotLinearSpeed.getData()[0][1];
	double a=robotLinearSpeed.getData()[0][2];
	Point pv=new Point(x,y);
//	l=math.sqrt(x*x+y*y);
	com.zoubworld.geometry.Vector v=new com.zoubworld.geometry.Vector(p,pv);
	s+=v.toSvg();
	ArcCircle ac=new ArcCircle(p,l,0,a*50);
	s+=ac.toSVG()+"\n";
	return s;
	}
	public String WheelsToSvg(Matrix whellLinearSpeed)
	{
		String s="";
		for(int i=0;i<WheelLocation.getData().length;i++)
		{
			double x=WheelLocation.getData()[i][0];
			double y=WheelLocation.getData()[i][1];
			double a=WheelLocation.getData()[i][2];
			double l=WheelSize.getData()[0][i];
			if(whellLinearSpeed!=null)
				l=whellLinearSpeed.getData()[0][i];
			System.out.println("x="+x+", y="+y+", a="+a);
			Point p=new Point(x,y);
			com.zoubworld.geometry.Vector v=new com.zoubworld.geometry.Vector(p,a,l);
			s+=v.toSvg()+"\n";
			Circle c=new Circle(p,0.03);
			s+=c.toSvg();
		}
		return s;
	}
	static int NbWheel=3;
	/**
	 * W1:x,y,theta W2:x,y,theta W3:x,y,theta x,y, location of wheel versus the
	 * center of robot(at 0,0) theta, the angle of the wheel direction with axe x
	 */
	static Matrix WheelLocation = null;
	static Matrix WheelSize = new Matrix(NbWheel,1);
	void init3() {
		NbWheel=3;
		WheelLocation = new Matrix(3,NbWheel);
		 m = new Matrix(3);
		/** convertion matrix robot speed to wheel speed */
		 mi = new Matrix(3);
		Double L = 0.110;// distance between the center of robot and the wheel
		Double aad[][] = { { Math.cos(-Math.PI / 3) * L, Math.sin(-Math.PI / 3) * L, Math.PI / 6 },
				{ Math.cos(Math.PI / 3) * L, Math.sin(Math.PI / 3) * L, Math.PI * 5 / 6 },
				{ Math.cos(Math.PI) * L, Math.sin(Math.PI) * L, -Math.PI / 2 } };
		WheelLocation.setData(aad);

		WheelSize.init(1);
		WheelSize.multiply(0.063);

		Double aad1[][] = { { Math.sin(-Math.PI / 3), Math.cos(-Math.PI / 3), L },
				{ Math.sin(Math.PI / 3), Math.cos(Math.PI / 3), L }, { Math.sin(Math.PI), Math.cos(Math.PI), L } };
		m.setData(aad1);

		Double aad2[][] = { { Math.sin(-Math.PI / 3), Math.sin(Math.PI / 3), L }, { 1.0 / 3, 1.0 / 3, -2.0 / 3 },
				{ 1.0 / 3 / L, 1.0 / 3 / L, -2.0 / 3 / L } };
		mi.setData(aad2);
		
	}
	void init2() {
		NbWheel=2;
		WheelLocation = new Matrix(3,NbWheel);
		/** convertion matrix wheel speed to robot speed */
		 m = new Matrix(3);
		/** convertion matrix robot speed to wheel speed */
		 mi = new Matrix(3);
		Double L = 0.110;// distance between the center of robot and the wheel
		//x,y,alpha
		Double aad[][] = { { Math.cos(-Math.PI / 2) * L, Math.sin(-Math.PI / 2) * L, Math.PI / 2 },
				{ Math.cos(Math.PI / 2) * L, Math.sin(Math.PI / 2) * L, -Math.PI * 2 }
				};
		WheelLocation.setData(aad);

		WheelSize.init(1);
		WheelSize.multiply(0.063);

		Double aad1[][] = { { Math.sin(-Math.PI / 2), Math.cos(-Math.PI / 2), L },
				{ Math.sin(Math.PI / 2), Math.cos(Math.PI / 2), L }};
		m.setData(aad1);

		Double aad2[][] = { { Math.sin(-Math.PI / 2), Math.sin(Math.PI / 2) }, { 0.0,0.0 },
				{ 1.0 / 2 / L, 1.0 / 2 / L } };
		mi.setData(aad2);
		
	}

	/** convertion matrix wheel speed to robot speed */
	Matrix m = null;
	/** convertion matrix robot speed to wheel speed */
	Matrix mi = null;

	/**
	 * robotLinearSpeed={vx,vy,vtetha} 
	 * whellLinearSpeed2={vleft,vrigth,vback}
	 */
	Matrix whellLinearSpeed2(Matrix robotLinearSpeed) {
		return m.multiply(robotLinearSpeed);
	}
	
	/**
	 * robotLinearSpeed={vx,vy,vtetha} 
	 * whellLinearSpeed2={vleft,vrigth,vback}
	 */
	Matrix robotLinearSpeed(Matrix whellLinearSpeed) {
		return mi.multiply(whellLinearSpeed);
		//return whellLinearSpeed.multiply(mi);
	}

	/**
	 * whellRotationSpeed={rpmleft,rpmrigth,rpmback} in round per minute
	 * whellLinearSpeed={vleft,vrigth,vback} in m/minutes
	 */
	Matrix whellLinearSpeed(Matrix whellRotationSpeed) {
		Matrix WheelSize = new 	Matrix(3,1);
		WheelSize.init(1);
		WheelSize.multiply(1 / 0.063);
		return whellRotationSpeed.multiply(WheelSize.toDiagonalMatrix());
	}
	
	/**
	 * whellRotationSpeed={rpmleft,rpmrigth,rpmback} in round per minute
	 * whellLinearSpeed={vleft,vrigth,vback} in m/minutes
	 */
	Matrix whellRotationSpeed(Matrix whellLinearSpeed) {
		return WheelSize.toDiagonalMatrix().invertCoef().multiply(whellLinearSpeed);

	}
	Matrix TickTowhellRotationSpeed(int tickperseconde[])
	    {
		Wheel wheels[]= {
				new Wheel(-0.1, 0.0, Math.PI/2, 0.073/2,220),
				new Wheel(0.1, 0.0, -Math.PI/2, 0.073/2,220),
				};
		Matrix m=new Matrix(NbWheel,1);
		for(int i=0;i<NbWheel;i++)
		m.getData()[0][i]=(double) tickperseconde[i]/(double) wheels[i].getTickPerRotation();//v1
		
		return m;
		
	    }
	@Override
	public String toSvg() {
		
		String s="";
		s+="<g id=\""+this.getClass().getName()+"\">\r\n\t";
		s =s+ (WheelsToSvg(null)+robotToSvg(WheelSize)).replaceAll("\r\n", "\r\n\t");
		s+="\r\n</g>\r\n";
		return s;
	}

}
