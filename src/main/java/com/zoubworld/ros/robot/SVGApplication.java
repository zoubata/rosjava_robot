package com.zoubworld.ros.robot;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
//import org.apache.batik.swing.svg.AbstractJSVGComponent;
import javax.swing.*;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.JSVGComponent;


public class SVGApplication extends JPanel
{
     public SVGApplication(){


    	  JSVGCanvas svg = new JSVGCanvas();
    	 svg.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);

          // location of the SVG File
    	 // svg.setURI("file:/C:/Users/Linda/Desktop/test.svg");
    	 // svg.setURI("file:/C:/Users/M43507/Downloads/Fichier_Celtic-knot-basic.svg");
    	//  File f=new File("C:\\Users\\M43507\\Downloads\\basic.svg");
    	  File f=new File("C:\\Users\\M43507\\Downloads\\robotique\\Eurobot2020_vinyl_plateau_v1.1.svg");
    	  svg.setURI(f.toURI().toString());
          // JPanel panel = new JPanel(); // *** what is this for? ***
          // panel.add(svg);
    	//  svg.setSize(1000, 1000);
          add(svg);
        
     }/*
     public void paintComponent(Graphics g)
     {
    	 super.paintComponent(g);
    	 Graphics2D g2d = (Graphics2D)g;
 	    g2d.setColor(Color.BLUE);
 	    
 	 //   g2d.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
    	 g2d.dispose();
    		// SVGApplication();
    		 g2d = (Graphics2D) g.create();
     }*/
     public static void main(String[] args)
     {
          JFrame frame = new JFrame("SVGView");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setLayout(new BorderLayout());
          frame.setResizable(true);
   //       Renderer renderer = new Renderer(); //this object extends JPanel, which is what i'm adding to the frame

//		renderer.setPreferredSize(new Dimension(WIDTH, HEIGHT)); //want to keep it centered on this width/height because there is important content within there.
  //        frame.add(renderer, BorderLayout.CENTER);
          frame.setSize(1000, 1000);
          frame.getContentPane().add(new SVGApplication());
          frame.pack();
          frame.setVisible(true);
     }
} 