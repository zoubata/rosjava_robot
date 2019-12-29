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

package com.zoubworld.ros.lidar;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import com.zoubworld.geometry.Unit;

import sensor_msgs.*;
/**
 * A simple {@link Subscriber} {@link NodeMain}.
 */
public class LidarListener extends AbstractNodeMain {
	private float anguleMin;
	private float anguleMax;
	private float dAngle;
	private float[] range;
  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("rosjava/lidar/SvgListener");
  }
  @Override
  public void onStart(ConnectedNode connectedNode) {
    final Log log = connectedNode.getLog();
    Subscriber<sensor_msgs.LaserScan> subscriber = connectedNode.newSubscriber("/r1/laser/scan", sensor_msgs.LaserScan._TYPE);
    subscriber.addMessageListener(new MessageListener<sensor_msgs.LaserScan>() {
     

	

	@Override
	public void onNewMessage(sensor_msgs.LaserScan message) {
		// TODO Auto-generated method stub
	//	log.info("I heard: \"" + message.getData() + "\"");
		anguleMin=message.getAngleMin();
		anguleMax=message.getAngleMax();
		dAngle=message.getAngleIncrement();
		range=message.getRanges();
		
	}
    });
  }
	public String toSvg2(double xm, double ym, double tetha) {
		String s = "<g id=\"" + this.getClass() + "\">\n";
		int index = 0;
		if (anguleMin != 0 && anguleMax != 0 && dAngle != 0)
			for (double a = anguleMin; a < anguleMax; a += dAngle)
				if (range.length > index && range[(index)] != 0) {
					double x0 = xm;
					double y0 = ym;
					double x1 = xm + range[(index)] * Math.cos(a + tetha);
					double y1 = ym + range[(index)] * Math.sin(a + tetha);
					index++;
					s += "\t<line x1=\"" + Unit.MtoMm(x0) + "mm\" y1=\"" + Unit.MtoMm(y0) + "mm\" x2=\""
							+ Unit.MtoMm(x1) + "mm\" y2=\"" + Unit.MtoMm(y1)
							+ "mm\" style=\"stroke:rgb(255,0,0);stroke-width:2\" /> " + "\r\n";
				} // ("\r\n// angle="+Unit.toDegre(a)+" degre")+
				else
					index++;
		s += "</g>\n" + "";
		return s;
	}
}
