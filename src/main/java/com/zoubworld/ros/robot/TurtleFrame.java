/**
 * 
 */
package com.zoubworld.ros.robot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.apache.batik.swing.JSVGCanvas;

/**
 * @author M43507
 *
 */
public class TurtleFrame extends JPanel implements ActionListener {

	JFrame frame;
	Timer tm=new Timer(5,this);
//	int x=0,velx=1;
	
	IRobot robot;
	/**
	 * @return the robot
	 */
	public IRobot getRobot() {
		return robot;
	}
	/**
	 * @param robot the robot to set
	 */
	public void setRobot(IRobot robot) {
		this.robot = robot;
	}
	/**
	 * 
	 */
	public TurtleFrame() {
		// TODO Auto-generated constructor stub
	}
 public void paintComponent(Graphics g)
 {
	 super.paintComponent(g);
	 Graphics2D g2d = (Graphics2D)g;
	    g2d.setColor(Color.BLUE);
//	 g.setColor(Color.BLUE);
	//g.fillPolygon(xPoints, yPoints, nPoints);
	 int x=0,y=0;
	 try {
		 x=(int)(getRobot().getX()*1);
	 
	  y=(int)(getRobot().getY()*1);
	 }
	 catch(NullPointerException e)
	 {
		 e.printStackTrace();
	 }
//	g.fillRect(x, y, 100, 100);
	/*  Rectangle rect2 = new Rectangle(x, y, 50, 50);

	    g2d.rotate((getRobot().getTheta()));
	    g2d.draw(rect2);
	    g2d.fill(rect2);*/
	/* Rectangle r = new Rectangle(x, y, 50, 50);
	 Path2D.Double path = new Path2D.Double();
	 path.append(r, false);

	 AffineTransform t = new AffineTransform();
	 t.rotate((getRobot().getTheta()));
	 path.transform(t);
	 g2d.draw(path);*/
	 Rectangle rect = new Rectangle(x, y, 50, 50);
	 Polygon poly = new Polygon(new int[] { 0+x, 100+x, 0 +x}, new int[] { 0+y, 50+y, 100+y }, 3);
	 g2d.rotate((getRobot().getTheta()), rect.x + rect.width/2, rect.y + rect.height/2);
	// g2d.draw(rect);g2d.fill(rect);
	 g2d.draw(poly);	 
	 g2d.fill(poly);
	 g2d.setColor(Color.RED);
	 poly = new Polygon(new int[] { 80+x, 100+x, 80 +x}, new int[] { 40+y, 50+y, 60+y }, 3);
	 g2d.draw(poly);	 
	 g2d.fill(poly);
	 
	 g2d.dispose();
	// SVGApplication();
	 g2d = (Graphics2D) g.create();
	tm.start();
 }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("main start");
		TurtleFrame go= new TurtleFrame();
		System.out.println("main create object");
		
		go.fireUpScreen();
		System.out.println("main reach end");
		

	}
	public void fireUpScreen() {
		frame= new JFrame();
		frame.setVisible(true);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
	/*	if (x>500 || x<0)
			velx=-velx;
		
		x+=velx;*/
		this.repaint();
	}
	 public void SVGApplication(){
	      JSVGCanvas svg = new JSVGCanvas();
	      // location of the SVG File
	      svg.setURI("file:/C:/Users/M43507/Downloads/Fichier_Celtic-knot-basic.svg");
	      // JPanel panel = new JPanel(); // *** what is this for? ***
	      // panel.add(svg);
	      
	      add(svg);
	 }
}
