/**
 * 
 */
package com.zoubworld.ros;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pierre valleau
 *
 */
public class GraphViz {
	class NodeInfo
	{	
		String Name;
		Set<MsgInfo> Publishers;
		Set<MsgInfo> Subscribers;
		Set<MsgInfo> Services;
		/**
		 * @return the name
		 */
		public String getName() {
			return Name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			Name = name.trim();
		}
		/**
		 * @return the publishers
		 */
		public Set<MsgInfo> getPublishers() {
			if (Publishers==null)
				Publishers=new HashSet();
			return Publishers;
		}
		/**
		 * @param publishers the publishers to set
		 */
		public void setPublishers(Set<MsgInfo> publishers) {
			Publishers = publishers;
		}
		/**
		 * @return the subscribers
		 */
		public Set<MsgInfo> getSubscribers() {
			if (Subscribers==null)
				Subscribers=new HashSet();
			return Subscribers;
		}
		/**
		 * @param subscribers the subscribers to set
		 */
		public void setSubscribers(Set<MsgInfo> subscribers) {
			Subscribers = subscribers;
		}
		/**
		 * @return the services
		 */
		public Set<MsgInfo> getServices() {
			if (Services==null)
				Services=new HashSet();
			return Services;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "NodeInfo [Name=" + Name + "]";
		}
		/**
		 * @param services the services to set
		 */
		public void setServices(Set<MsgInfo> services) {
			Services = services;
		}
		String Connection;
		public void setConnection(String trim) {
			Connection=trim.trim();
			
		}
		/**
		 * @return the connection
		 */
		public String getConnection() {
			return Connection;
		}
		
	}
	class MsgInfo
	{
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "MsgInfo [Name=" + Name + ", Type=" + Type + ", direction=" + direction + ", transport=" + transport
					+ "]";
		}
		String Name;
		String Type;
		String direction;
		String transport;
		Set<NodeInfo> Publishers;
		Set<NodeInfo> Subscribers;
		/**
		 * @return the name
		 */
		public String getName() {
			return Name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			Name = name.trim();
		}
		/**
		 * @return the type
		 */
		public String getType() {
			return Type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			Type = type.trim();
		}
		/**
		 * @return the direction
		 */
		public String getDirection() {
			return direction;
		}
		/**
		 * @param direction the direction to set
		 */
		public void setDirection(String direction) {
			this.direction = direction.trim();
		}
		/**
		 * @return the transport
		 */
		public String getTransport() {
			return transport;
		}
		/**
		 * @param transport the transport to set
		 */
		public void setTransport(String transport) {
			this.transport = transport.trim();
		}
		/**
		 * @return the publishers
		 */
		public Set<NodeInfo> getPublishers() {
			if (Publishers==null)
				Publishers=new HashSet();
			return Publishers;
		}
		/**
		 * @param publishers the publishers to set
		 */
		public void setPublishers(Set<NodeInfo> publishers) {
			Publishers = publishers;
		}
		/**
		 * @return the subscribers
		 */
		public Set<NodeInfo> getSubscribers() {
			if (Subscribers==null)
				Subscribers=new HashSet();
			return Subscribers;
		}
		/**
		 * @param subscribers the subscribers to set
		 */
		public void setSubscribers(Set<NodeInfo> subscribers) {
			Subscribers = subscribers;
		}
		public void parse(String trim) {
			String[] tab = trim.trim().split("\\s");
			setName(tab[0]);
			if(tab.length>1)
				setType(tab[1].replace("[", "").replace("]", ""));
			else
				setType("service");
		}
		
	}
	Set<NodeInfo> nodes=new HashSet();
	Set<MsgInfo> msgs=new HashSet();
	public MsgInfo getMsg(String name)
	{
		for(MsgInfo n:msgs)
			if (n.getName().equals(name.trim()))
				return n;
		return null;
	}
	public NodeInfo getNode(String name)
	{
		for(NodeInfo n:nodes)
			if (n.getName().equals(name.trim()))
				return n;
		return null;
	}
	public void parseNodeInfos(String data)
	{
	for(String s:data.split("----------------------------------------"))
		if(!s.trim().equals(""))
		parseNodeInfo(s);
	}
	public String parseNodeList(String data)
	{
		String s="";
		for(String n:data.split("\n"))
			s+="rosnode info "+n.trim()+">>data.txt\r\n";
		return s;
	}
	public void parseNodeInfo(String data)
	{
		String tab[]=data.split("Node|Publications:|Subscriptions:|Services:|contacting|Connections:");
		NodeInfo ni= new NodeInfo();
		ni.setName(tab[1].replace("[", "").replace("]", ""));
		if( getNode(ni.getName())!=null)
			ni=	getNode(ni.getName());
		else
			nodes.add(ni);
		if(!tab[2].trim().equals("None"))
		for(String msg:tab[2].split("\n"))
			if(!msg.trim().equals(""))
		{
			
			MsgInfo mi=new MsgInfo();
			
			mi.parse(msg.replace("*", "").trim());
			if( getMsg(mi.getName())!=null)
				mi=	getMsg(mi.getName());
			else
				msgs.add(mi);
			ni.getPublishers().add(mi);mi.getPublishers().add(ni);			
		}
		if(!tab[3].trim().equals("None"))
		for(String msg:tab[3].split("\n"))
			if(!msg.trim().equals(""))
		{
			
			MsgInfo mi=new MsgInfo();
			
			mi.parse(msg.replace("*", "").trim());
			if( getMsg(mi.getName())!=null)
				mi=	getMsg(mi.getName());
			else
				msgs.add(mi);
			ni.getSubscribers().add(mi);mi.getSubscribers().add(ni);			
		}
		if(!tab[4].trim().equals("None"))
			for(String msg:tab[4].split("\n"))
				if(!msg.trim().equals(""))
			{
				
				MsgInfo mi=new MsgInfo();
				
				mi.parse(msg.replace("*", "").trim());
				if( getMsg(mi.getName())!=null)
					mi=	getMsg(mi.getName());
				else
					msgs.add(mi);
				ni.getServices().add(mi);//mi.gets().add(ni);			
			}
		if(!tab[5].trim().equals("None"))
			for(String msg:tab[5].split("\\s"))
				if(msg.trim().contains("http"))
					ni.setConnection(msg.trim());
		
	}
	/**
	 * 
	 */
	public GraphViz() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s= "digraph data_relationships {\r\n" ; 
	
	for(NodeInfo node:nodes)
		s+=/*"  \""+node.getName()+"\"\r\n" ; */
	"  \""+node.getName()+"\" [shape=Mrecord, label=\"{ Node : "+node.getName()
	+ ((node.getConnection()==null)?"":(" | "+node.getConnection() ))
	+node.getPublishers().stream().map(n->n.getName()).collect(Collectors.toList()).toString()
	.replaceAll(","," \\\\n ")
	.replace("["," | ")
	.replace("]"," ")
	
	+((node.getServices().size()>0)?node.getServices().stream().map(n->n.getName()).collect(Collectors.toList()).toString()
	.replaceAll(","," \\\\n ")
	.replace("["," | ")
	.replace("]",""):" ")
	
	+" \\l}\"]\r\n";
				/*
				"  \"org-mode\"\r\n" + 
				"  \"org-exp-blocks\"\r\n" + 
				"  \"dot\"\r\n" + 
				"  \"ditaa\"\r\n" + 
				"  \"HTML\" [shape=Mrecord, label=\"{HTML|publish on the web\\l}\"]\r\n" + 
				"  \"LaTeX\" [shape=Mrecord, label=\"{LaTeX|publish in PDF\\l}\"]\r\n" + 
				*/
	
//	for(NodeInfo node:nodes)
		for(MsgInfo msg:msgs)
			for(NodeInfo node:msg.getPublishers())
				for(NodeInfo nodeTo:msg.getSubscribers())
		s+="  \""+node.getName()+"\" -> \""+nodeTo.getName()+"\"   [label=\""
			+msg.getType()+": \\n "+msg.getName()+/*"|"+msg.getTransport()+*/"\"];\r\n" ;

	s+=/*		"  \"org-mode\" -> \"org-exp-blocks\"\r\n" + 
				"  \"dot\" -> \"org-mode\"\r\n" + 
				"  \"ditaa\" -> \"org-mode\"\r\n" + 
				"  \"org-exp-blocks\" -> \"HTML\"\r\n" + 
				"  \"org-exp-blocks\" -> LaTeX\r\n" + */
				" }";
	return s;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GraphViz gv= new GraphViz();
		System.out.println("export ROS_HOSTNAME=10.42.0.1\r\n" + 
		"export ROS_MASTER_URI=http://$ROS_HOSTNAME:11311");
		System.out.println("rosnode list");//execute
		String nodelist="/rosjava/Joy\r\n" + 
				"/rosjava/listener\r\n" + 
				"/rosjava/talker\r\n" + 
				"/rosjava/teleop\r\n" + 
				"/rosout\r\n" + 
				"/turtlesim";
		nodelist="/r1/hardware_interface\r\n" + 
				"/r1/joint_state_publisher\r\n" + 
				"/r1/joy_xbox\r\n" + 
				"/r1/mpu6050\r\n" + 
				"/r1/r1_amcl\r\n" + 
				"/r1/r1_behavior_base_node\r\n" + 
				"/r1/r1_behavior_tree\r\n" + 
				"/r1/r1_imu_filter_madgwick\r\n" + 
				"/r1/r1_laser_scan_filter\r\n" + 
				"/r1/r1_localization_map\r\n" + 
				"/r1/r1_localization_odom\r\n" + 
				"/r1/r1_map_server\r\n" + 
				"/r1/r1_move_base\r\n" + 
				"/r1/r1_obstacle_map_server\r\n" + 
				"/r1/r1_snap_map\r\n" + 
				"/r1/r1_teleop\r\n" + 
				"/r1/robot_state_publisher\r\n" + 
				"/r1/rosserial_message_info\r\n" + 
				"/r1/rosserial_server_motor\r\n" + 
				"/r1/rosserial_server_pince\r\n" + 
				"/r1/ydlidar\r\n" + 
				"/rosout";
		System.out.println(gv.parseNodeList(nodelist));//execute
		String data="--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosjava/Joy]\r\n" + 
				"Publications:\r\n" + 
				" * /joy [sensor_msgs/Joy]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services: None\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://127.0.0.1:52620/ ...\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosjava/talker]\r\n" + 
				"Publications:\r\n" + 
				" * /chatter [std_msgs/String]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services: None\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://127.0.0.1:58933/ ...\r\n" + 
				"Pid: 104188\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /chatter\r\n" + 
				"    * to: /rosjava/teleop\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosjava/teleop]\r\n" + 
				"Publications:\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /chatter [std_msgs/String]\r\n" + 
				"\r\n" + 
				"Services: None\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://127.0.0.1:58911/ ...\r\n" + 
				"Pid: 132732\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /chatter\r\n" + 
				"    * to: /rosjava/talker (http://127.0.0.1:58933/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosout]\r\n" + 
				"Publications:\r\n" + 
				" * /rosout_agg [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /rosout/get_loggers\r\n" + 
				" * /rosout/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://NAN-LT-M43507B:52549/ ...\r\n" + 
				"Pid: 647\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosjava/Joy (http://127.0.0.1:52620/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosjava/teleop (http://127.0.0.1:58911/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosjava/talker (http://127.0.0.1:58933/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"";
		data="--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosjava/Joy]\r\n" + 
				"Publications:\r\n" + 
				" * /joy [sensor_msgs/Joy]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services: None\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://127.0.0.1:60005/ ...\r\n" + 
				"Pid: 132164\r\n" + 
				"Connections:\r\n" + 
				" * topic: /joy\r\n" + 
				"    * to: /rosjava/teleop\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosjava/listener]\r\n" + 
				"Publications:\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /chatter [std_msgs/String]\r\n" + 
				"\r\n" + 
				"Services: None\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://127.0.0.1:59985/ ...\r\n" + 
				"Pid: 86576\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /chatter\r\n" + 
				"    * to: /rosjava/talker (http://127.0.0.1:59972/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosjava/talker]\r\n" + 
				"Publications:\r\n" + 
				" * /chatter [std_msgs/String]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services: None\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://127.0.0.1:59972/ ...\r\n" + 
				"Pid: 75088\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /chatter\r\n" + 
				"    * to: /rosjava/listener\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosjava/teleop]\r\n" + 
				"Publications:\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				" * /turtle1/cmd_vel [geometry_msgs/Twist]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /joy [sensor_msgs/Joy]\r\n" + 
				"\r\n" + 
				"Services: None\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://127.0.0.1:60020/ ...\r\n" + 
				"Pid: 118884\r\n" + 
				"Connections:\r\n" + 
				" * topic: /turtle1/cmd_vel\r\n" + 
				"    * to: /turtlesim\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /joy\r\n" + 
				"    * to: /rosjava/Joy (http://127.0.0.1:60005/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosout]\r\n" + 
				"Publications:\r\n" + 
				" * /rosout_agg [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /rosout/get_loggers\r\n" + 
				" * /rosout/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://NAN-LT-M43507B:52549/ ...\r\n" + 
				"Pid: 647\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /turtlesim (http://NAN-LT-M43507B:59923/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosjava/talker (http://127.0.0.1:59972/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosjava/listener (http://127.0.0.1:59985/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosjava/Joy (http://127.0.0.1:60005/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosjava/teleop (http://127.0.0.1:60020/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/turtlesim]\r\n" + 
				"Publications:\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				" * /turtle1/color_sensor [turtlesim/Color]\r\n" + 
				" * /turtle1/pose [turtlesim/Pose]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /turtle1/cmd_vel [geometry_msgs/Twist]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /clear\r\n" + 
				" * /kill\r\n" + 
				" * /reset\r\n" + 
				" * /spawn\r\n" + 
				" * /turtle1/set_pen\r\n" + 
				" * /turtle1/teleport_absolute\r\n" + 
				" * /turtle1/teleport_relative\r\n" + 
				" * /turtlesim/get_loggers\r\n" + 
				" * /turtlesim/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://NAN-LT-M43507B:59923/ ...\r\n" + 
				"Pid: 1246\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /turtle1/cmd_vel\r\n" + 
				"    * to: /rosjava/teleop (http://127.0.0.1:60020/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"";
		data="\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/hardware_interface]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/back_wheel_to_base_state [control_msgs/JointControllerState]\r\n" + 
				" * /r1/hardware_interface/parameter_descriptions [dynamic_reconfigure/ConfigDescription]\r\n" + 
				" * /r1/hardware_interface/parameter_updates [dynamic_reconfigure/Config]\r\n" + 
				" * /r1/joint_states [sensor_msgs/JointState]\r\n" + 
				" * /r1/left_wheel_to_base_state [control_msgs/JointControllerState]\r\n" + 
				" * /r1/mobile_base_controller/cmd_vel_out [geometry_msgs/TwistStamped]\r\n" + 
				" * /r1/mobile_base_controller/odom [nav_msgs/Odometry]\r\n" + 
				" * /r1/pwm/back [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/left [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/right [std_msgs/Int16]\r\n" + 
				" * /r1/right_wheel_to_base_state [control_msgs/JointControllerState]\r\n" + 
				" * /r1/speed/back [std_msgs/Int32]\r\n" + 
				" * /r1/speed/left [std_msgs/Int32]\r\n" + 
				" * /r1/speed/right [std_msgs/Int32]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/encoder/back [std_msgs/Int16]\r\n" + 
				" * /r1/encoder/back/speed [std_msgs/Int32]\r\n" + 
				" * /r1/encoder/left [std_msgs/Int16]\r\n" + 
				" * /r1/encoder/left/speed [std_msgs/Int32]\r\n" + 
				" * /r1/encoder/right [std_msgs/Int16]\r\n" + 
				" * /r1/encoder/right/speed [std_msgs/Int32]\r\n" + 
				" * /r1/mobile_base_controller/cmd_vel [geometry_msgs/Twist]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/controller_manager/list_controller_types\r\n" + 
				" * /r1/controller_manager/list_controllers\r\n" + 
				" * /r1/controller_manager/load_controller\r\n" + 
				" * /r1/controller_manager/reload_controller_libraries\r\n" + 
				" * /r1/controller_manager/switch_controller\r\n" + 
				" * /r1/controller_manager/unload_controller\r\n" + 
				" * /r1/hardware_interface/get_loggers\r\n" + 
				" * /r1/hardware_interface/set_logger_level\r\n" + 
				" * /r1/hardware_interface/set_parameters\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:35741/ ...\r\n" + 
				"Pid: 1623\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/right\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/speed/right\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/left\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/speed/left\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/back\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/speed/back\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/joint_states\r\n" + 
				"    * to: /r1/robot_state_publisher\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/mobile_base_controller/odom\r\n" + 
				"    * to: /r1/r1_localization_odom\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/mobile_base_controller/odom\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_behavior_tree\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_move_base\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_snap_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/right\r\n" + 
				"    * to: /r1/rosserial_server_motor (http://10.42.0.1:34559/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/right/speed\r\n" + 
				"    * to: /r1/rosserial_server_motor (http://10.42.0.1:34559/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/left\r\n" + 
				"    * to: /r1/rosserial_server_motor (http://10.42.0.1:34559/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/left/speed\r\n" + 
				"    * to: /r1/rosserial_server_motor (http://10.42.0.1:34559/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/back\r\n" + 
				"    * to: /r1/rosserial_server_motor (http://10.42.0.1:34559/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/back/speed\r\n" + 
				"    * to: /r1/rosserial_server_motor (http://10.42.0.1:34559/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/mobile_base_controller/cmd_vel\r\n" + 
				"    * to: /r1/r1_behavior_base_node (http://10.42.0.1:42107/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/joint_state_publisher]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/joint_states [sensor_msgs/JointState]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/joint_state_publisher/get_loggers\r\n" + 
				" * /r1/joint_state_publisher/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:36649/ ...\r\n" + 
				"Pid: 1611\r\n" + 
				"Connections:\r\n" + 
				" * topic: /r1/joint_states\r\n" + 
				"    * to: /r1/robot_state_publisher\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/joy_xbox]\r\n" + 
				"Publications:\r\n" + 
				" * /diagnostics [diagnostic_msgs/DiagnosticArray]\r\n" + 
				" * /r1/joy [sensor_msgs/Joy]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/joy_xbox/get_loggers\r\n" + 
				" * /r1/joy_xbox/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:44229/ ...\r\n" + 
				"Pid: 1715\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/joy\r\n" + 
				"    * to: /r1/r1_teleop\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/mpu6050]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/imu/data_raw [sensor_msgs/Imu]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/mpu6050/get_bias\r\n" + 
				" * /r1/mpu6050/get_loggers\r\n" + 
				" * /r1/mpu6050/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:43351/ ...\r\n" + 
				"Pid: 1627\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/imu/data_raw\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_amcl]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/amcl_pose [geometry_msgs/PoseWithCovarianceStamped]\r\n" + 
				" * /r1/particlecloud [geometry_msgs/PoseArray]\r\n" + 
				" * /r1/r1_amcl/parameter_descriptions [dynamic_reconfigure/ConfigDescription]\r\n" + 
				" * /r1/r1_amcl/parameter_updates [dynamic_reconfigure/Config]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/initialpose [geometry_msgs/PoseWithCovarianceStamped]\r\n" + 
				" * /r1/laser/scan_filtered [sensor_msgs/LaserScan]\r\n" + 
				" * /r1/map [nav_msgs/OccupancyGrid]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				" * /tf_static [tf2_msgs/TFMessage]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/global_localization\r\n" + 
				" * /r1/r1_amcl/get_loggers\r\n" + 
				" * /r1/r1_amcl/set_logger_level\r\n" + 
				" * /r1/r1_amcl/set_parameters\r\n" + 
				" * /r1/request_nomotion_update\r\n" + 
				" * /r1/set_map\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:43585/ ...\r\n" + 
				"Pid: 1682\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: INTRAPROCESS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_behavior_tree\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_move_base\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_snap_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/amcl_pose\r\n" + 
				"    * to: /r1/r1_behavior_tree\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/amcl_pose\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl (http://10.42.0.1:43585/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: INTRAPROCESS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick (http://10.42.0.1:39791/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map (http://10.42.0.1:39499/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom (http://10.42.0.1:34909/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/laser/scan_filtered\r\n" + 
				"    * to: /r1/r1_laser_scan_filter (http://10.42.0.1:38453/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/initialpose\r\n" + 
				"    * to: /r1/r1_snap_map (http://10.42.0.1:45867/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/initialpose\r\n" + 
				"    * to: /r1/r1_behavior_base_node (http://10.42.0.1:42107/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/map\r\n" + 
				"    * to: /r1/r1_map_server (http://10.42.0.1:38489/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_behavior_base_node]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/initialpose [geometry_msgs/PoseWithCovarianceStamped]\r\n" + 
				" * /r1/mobile_base_controller/cmd_vel [geometry_msgs/Twist]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/auto_cmd_vel [geometry_msgs/Twist]\r\n" + 
				" * /r1/mode_auto [std_msgs/Bool]\r\n" + 
				" * /r1/r1_teleop/cmd_vel [geometry_msgs/Twist]\r\n" + 
				" * /r1/start [unknown type]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_behavior_base_node/get_loggers\r\n" + 
				" * /r1/r1_behavior_base_node/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:42107/ ...\r\n" + 
				"Pid: 1651\r\n" + 
				"Connections:\r\n" + 
				" * topic: /r1/mobile_base_controller/cmd_vel\r\n" + 
				"    * to: /r1/hardware_interface\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/initialpose\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/auto_cmd_vel\r\n" + 
				"    * to: /r1/r1_move_base (http://10.42.0.1:36991/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/mode_auto\r\n" + 
				"    * to: /r1/r1_teleop (http://10.42.0.1:41231/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/r1_teleop/cmd_vel\r\n" + 
				"    * to: /r1/r1_teleop (http://10.42.0.1:41231/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_behavior_tree]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/lcd/line1 [std_msgs/String]\r\n" + 
				" * /r1/move_base/cancel [actionlib_msgs/GoalID]\r\n" + 
				" * /r1/move_base/goal [move_base_msgs/MoveBaseActionGoal]\r\n" + 
				" * /r1/pwm/pompe [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/vanne1 [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/vanne2 [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/vanne3 [std_msgs/Int16]\r\n" + 
				" * /r1/servo/E [std_msgs/UInt16]\r\n" + 
				" * /r1/servo/F [std_msgs/UInt16]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/amcl_pose [geometry_msgs/PoseWithCovarianceStamped]\r\n" + 
				" * /r1/auto_cmd_vel [geometry_msgs/Twist]\r\n" + 
				" * /r1/move_base/feedback [unknown type]\r\n" + 
				" * /r1/move_base/result [unknown type]\r\n" + 
				" * /r1/move_base/status [unknown type]\r\n" + 
				" * /r1/pilo/switches [std_msgs/UInt32]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				" * /tf_static [tf2_msgs/TFMessage]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_behavior_tree/get_loggers\r\n" + 
				" * /r1/r1_behavior_tree/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:37319/ ...\r\n" + 
				"Pid: 1659\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne1\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne3\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne2\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/pompe\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pilo/switches\r\n" + 
				"    * to: /r1/rosserial_server_pince (http://10.42.0.1:37971/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/amcl_pose\r\n" + 
				"    * to: /r1/r1_amcl (http://10.42.0.1:43585/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/auto_cmd_vel\r\n" + 
				"    * to: /r1/r1_move_base (http://10.42.0.1:36991/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick (http://10.42.0.1:39791/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map (http://10.42.0.1:39499/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom (http://10.42.0.1:34909/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl (http://10.42.0.1:43585/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_imu_filter_madgwick]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/imu/data [sensor_msgs/Imu]\r\n" + 
				" * /r1/r1_imu_filter_madgwick/parameter_descriptions [dynamic_reconfigure/ConfigDescription]\r\n" + 
				" * /r1/r1_imu_filter_madgwick/parameter_updates [dynamic_reconfigure/Config]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/imu/data_raw [sensor_msgs/Imu]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_imu_filter_madgwick/get_loggers\r\n" + 
				" * /r1/r1_imu_filter_madgwick/set_logger_level\r\n" + 
				" * /r1/r1_imu_filter_madgwick/set_parameters\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:39791/ ...\r\n" + 
				"Pid: 1667\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_behavior_tree\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_move_base\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_snap_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/imu/data\r\n" + 
				"    * to: /r1/r1_localization_odom\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/imu/data\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/imu/data_raw\r\n" + 
				"    * to: /r1/mpu6050 (http://10.42.0.1:43351/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_laser_scan_filter]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/laser/scan_filtered [sensor_msgs/LaserScan]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/laser/scan [sensor_msgs/LaserScan]\r\n" + 
				" * /r1/r1_laser_scan_filter/use_angles2 [unknown type]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_laser_scan_filter/get_loggers\r\n" + 
				" * /r1/r1_laser_scan_filter/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:38453/ ...\r\n" + 
				"Pid: 1634\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/laser/scan_filtered\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/laser/scan_filtered\r\n" + 
				"    * to: /r1/r1_snap_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/laser/scan\r\n" + 
				"    * to: /r1/ydlidar (http://10.42.0.1:33915/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_localization_map]\r\n" + 
				"Publications:\r\n" + 
				" * /diagnostics [diagnostic_msgs/DiagnosticArray]\r\n" + 
				" * /r1/odom_filtered_map [nav_msgs/Odometry]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/amcl_pose [geometry_msgs/PoseWithCovarianceStamped]\r\n" + 
				" * /r1/imu/data [sensor_msgs/Imu]\r\n" + 
				" * /r1/mobile_base_controller/odom [nav_msgs/Odometry]\r\n" + 
				" * /r1/set_pose [unknown type]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				" * /tf_static [tf2_msgs/TFMessage]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_localization_map/enable\r\n" + 
				" * /r1/r1_localization_map/get_loggers\r\n" + 
				" * /r1/r1_localization_map/set_logger_level\r\n" + 
				" * /r1/r1_localization_map/toggle\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:39499/ ...\r\n" + 
				"Pid: 1677\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: INTRAPROCESS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_behavior_tree\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_move_base\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_snap_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map (http://10.42.0.1:39499/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: INTRAPROCESS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick (http://10.42.0.1:39791/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom (http://10.42.0.1:34909/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl (http://10.42.0.1:43585/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/mobile_base_controller/odom\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/amcl_pose\r\n" + 
				"    * to: /r1/r1_amcl (http://10.42.0.1:43585/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/imu/data\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick (http://10.42.0.1:39791/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_localization_odom]\r\n" + 
				"Publications:\r\n" + 
				" * /diagnostics [diagnostic_msgs/DiagnosticArray]\r\n" + 
				" * /r1/odometry/filtered [nav_msgs/Odometry]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/imu/data [sensor_msgs/Imu]\r\n" + 
				" * /r1/mobile_base_controller/odom [nav_msgs/Odometry]\r\n" + 
				" * /r1/set_pose [unknown type]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				" * /tf_static [tf2_msgs/TFMessage]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_localization_odom/enable\r\n" + 
				" * /r1/r1_localization_odom/get_loggers\r\n" + 
				" * /r1/r1_localization_odom/set_logger_level\r\n" + 
				" * /r1/r1_localization_odom/toggle\r\n" + 
				" * /r1/set_pose\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:34909/ ...\r\n" + 
				"Pid: 1672\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: INTRAPROCESS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_behavior_tree\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_move_base\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_snap_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom (http://10.42.0.1:34909/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: INTRAPROCESS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick (http://10.42.0.1:39791/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map (http://10.42.0.1:39499/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl (http://10.42.0.1:43585/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/mobile_base_controller/odom\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/imu/data\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick (http://10.42.0.1:39791/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_map_server]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/map [nav_msgs/OccupancyGrid]\r\n" + 
				" * /r1/map_metadata [nav_msgs/MapMetaData]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_map_server/get_loggers\r\n" + 
				" * /r1/r1_map_server/set_logger_level\r\n" + 
				" * /r1/static_map\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:38489/ ...\r\n" + 
				"Pid: 1663\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/map\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/map\r\n" + 
				"    * to: /r1/r1_snap_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_move_base]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/auto_cmd_vel [geometry_msgs/Twist]\r\n" + 
				" * /r1/move_base/goal [move_base_msgs/MoveBaseActionGoal]\r\n" + 
				" * /r1/r1_move_base/current_goal [geometry_msgs/PoseStamped]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/move_base_simple/goal [unknown type]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				" * /tf_static [tf2_msgs/TFMessage]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_move_base/get_loggers\r\n" + 
				" * /r1/r1_move_base/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:36991/ ...\r\n" + 
				"Pid: 1709\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/auto_cmd_vel\r\n" + 
				"    * to: /r1/r1_behavior_tree\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/auto_cmd_vel\r\n" + 
				"    * to: /r1/r1_behavior_base_node\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick (http://10.42.0.1:39791/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map (http://10.42.0.1:39499/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom (http://10.42.0.1:34909/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl (http://10.42.0.1:43585/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_obstacle_map_server]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/map_static_obstacle [nav_msgs/OccupancyGrid]\r\n" + 
				" * /r1/map_static_obstacle_metadata [nav_msgs/MapMetaData]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/map_static_obstacle_service\r\n" + 
				" * /r1/r1_obstacle_map_server/get_loggers\r\n" + 
				" * /r1/r1_obstacle_map_server/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:43953/ ...\r\n" + 
				"Pid: 1703\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_snap_map]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/initialpose [geometry_msgs/PoseWithCovarianceStamped]\r\n" + 
				" * /r1/r1_snap_map/acml_monitoring [sd_localization/SnapMapMonitor]\r\n" + 
				" * /r1/r1_snap_map/parameter_descriptions [dynamic_reconfigure/ConfigDescription]\r\n" + 
				" * /r1/r1_snap_map/parameter_updates [dynamic_reconfigure/Config]\r\n" + 
				" * /r1/r1_snap_map/scan_points [sensor_msgs/PointCloud2]\r\n" + 
				" * /r1/r1_snap_map/scan_points_transformed [sensor_msgs/PointCloud2]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/laser/scan_filtered [sensor_msgs/LaserScan]\r\n" + 
				" * /r1/map [nav_msgs/OccupancyGrid]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				" * /tf_static [tf2_msgs/TFMessage]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_snap_map/get_loggers\r\n" + 
				" * /r1/r1_snap_map/set_logger_level\r\n" + 
				" * /r1/r1_snap_map/set_parameters\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:45867/ ...\r\n" + 
				"Pid: 1696\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/initialpose\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick (http://10.42.0.1:39791/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map (http://10.42.0.1:39499/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom (http://10.42.0.1:34909/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl (http://10.42.0.1:43585/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/map\r\n" + 
				"    * to: /r1/r1_map_server (http://10.42.0.1:38489/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/laser/scan_filtered\r\n" + 
				"    * to: /r1/r1_laser_scan_filter (http://10.42.0.1:38453/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/r1_teleop]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/mode_auto [std_msgs/Bool]\r\n" + 
				" * /r1/pwm/pompe [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/vanne1 [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/vanne2 [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/vanne3 [std_msgs/Int16]\r\n" + 
				" * /r1/r1_teleop/cmd_vel [geometry_msgs/Twist]\r\n" + 
				" * /r1/r1_teleop/parameter_descriptions [dynamic_reconfigure/ConfigDescription]\r\n" + 
				" * /r1/r1_teleop/parameter_updates [dynamic_reconfigure/Config]\r\n" + 
				" * /r1/servo/E [std_msgs/UInt16]\r\n" + 
				" * /r1/servo/F [std_msgs/UInt16]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/joy [sensor_msgs/Joy]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/r1_teleop/get_loggers\r\n" + 
				" * /r1/r1_teleop/set_logger_level\r\n" + 
				" * /r1/r1_teleop/set_parameters\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:41231/ ...\r\n" + 
				"Pid: 1718\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/r1_teleop/cmd_vel\r\n" + 
				"    * to: /r1/r1_behavior_base_node\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/mode_auto\r\n" + 
				"    * to: /r1/r1_behavior_base_node\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/pompe\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne2\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne3\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne1\r\n" + 
				"    * to: /r1/rosserial_server_motor\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/joy\r\n" + 
				"    * to: /r1/joy_xbox (http://10.42.0.1:44229/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/robot_state_publisher]\r\n" + 
				"Publications:\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				" * /tf [tf/tfMessage]\r\n" + 
				" * /tf_static [tf2_msgs/TFMessage]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/joint_states [sensor_msgs/JointState]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/robot_state_publisher/get_loggers\r\n" + 
				" * /r1/robot_state_publisher/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:34461/ ...\r\n" + 
				"Pid: 1648\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_behavior_tree\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_odom\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_move_base\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf\r\n" + 
				"    * to: /r1/r1_snap_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/r1_behavior_tree\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/r1_localization_odom\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/r1_localization_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/r1_move_base\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/r1_amcl\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /tf_static\r\n" + 
				"    * to: /r1/r1_snap_map\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/joint_states\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/joint_states\r\n" + 
				"    * to: /r1/joint_state_publisher (http://10.42.0.1:36649/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/rosserial_message_info]\r\n" + 
				"Publications:\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/message_info\r\n" + 
				" * /r1/rosserial_message_info/get_loggers\r\n" + 
				" * /r1/rosserial_message_info/set_logger_level\r\n" + 
				" * /r1/service_info\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:41657/ ...\r\n" + 
				"Pid: 1614\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/rosserial_server_motor]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/captor/debug/1 [std_msgs/Int16]\r\n" + 
				" * /r1/captor/debug/2 [std_msgs/Int16]\r\n" + 
				" * /r1/captor/debug/3 [std_msgs/Int16]\r\n" + 
				" * /r1/captor/time_loop [std_msgs/Int32]\r\n" + 
				" * /r1/captor/usart/rx [std_msgs/Int16]\r\n" + 
				" * /r1/captor/usart/tx [std_msgs/Int16]\r\n" + 
				" * /r1/encoder/back [std_msgs/Int16]\r\n" + 
				" * /r1/encoder/back/speed [std_msgs/Int32]\r\n" + 
				" * /r1/encoder/left [std_msgs/Int16]\r\n" + 
				" * /r1/encoder/left/speed [std_msgs/Int32]\r\n" + 
				" * /r1/encoder/right [std_msgs/Int16]\r\n" + 
				" * /r1/encoder/right/speed [std_msgs/Int32]\r\n" + 
				" * /r1/sensor/0 [std_msgs/Int16]\r\n" + 
				" * /r1/sensor/1 [std_msgs/Int16]\r\n" + 
				" * /r1/sensor/2 [std_msgs/Int16]\r\n" + 
				" * /r1/sensor/3 [std_msgs/Int16]\r\n" + 
				" * /r1/sensor/4 [std_msgs/Int16]\r\n" + 
				" * /r1/sensor/5 [std_msgs/Int16]\r\n" + 
				" * /r1/sensor/6 [std_msgs/Int16]\r\n" + 
				" * /r1/sensor/7 [std_msgs/Int16]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/pwm/back [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/left [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/pompe [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/right [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/vanne1 [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/vanne2 [std_msgs/Int16]\r\n" + 
				" * /r1/pwm/vanne3 [std_msgs/Int16]\r\n" + 
				" * /r1/speed/back [std_msgs/Int32]\r\n" + 
				" * /r1/speed/left [std_msgs/Int32]\r\n" + 
				" * /r1/speed/pompe [unknown type]\r\n" + 
				" * /r1/speed/right [std_msgs/Int32]\r\n" + 
				" * /r1/speed/vanne1 [unknown type]\r\n" + 
				" * /r1/speed/vanne2 [unknown type]\r\n" + 
				" * /r1/speed/vanne3 [unknown type]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/rosserial_server_motor/get_loggers\r\n" + 
				" * /r1/rosserial_server_motor/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:34559/ ...\r\n" + 
				"Pid: 1612\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /rosout\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/right\r\n" + 
				"    * to: /r1/hardware_interface\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/right/speed\r\n" + 
				"    * to: /r1/hardware_interface\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/left\r\n" + 
				"    * to: /r1/hardware_interface\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/left/speed\r\n" + 
				"    * to: /r1/hardware_interface\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/back\r\n" + 
				"    * to: /r1/hardware_interface\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/encoder/back/speed\r\n" + 
				"    * to: /r1/hardware_interface\r\n" + 
				"    * direction: outbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/back\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/speed/back\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/right\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/speed/right\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/left\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/speed/left\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/pompe\r\n" + 
				"    * to: /r1/r1_teleop (http://10.42.0.1:41231/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/pompe\r\n" + 
				"    * to: /r1/r1_behavior_tree (http://10.42.0.1:37319/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne1\r\n" + 
				"    * to: /r1/r1_teleop (http://10.42.0.1:41231/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne1\r\n" + 
				"    * to: /r1/r1_behavior_tree (http://10.42.0.1:37319/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne2\r\n" + 
				"    * to: /r1/r1_teleop (http://10.42.0.1:41231/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne2\r\n" + 
				"    * to: /r1/r1_behavior_tree (http://10.42.0.1:37319/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne3\r\n" + 
				"    * to: /r1/r1_teleop (http://10.42.0.1:41231/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /r1/pwm/vanne3\r\n" + 
				"    * to: /r1/r1_behavior_tree (http://10.42.0.1:37319/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/rosserial_server_pince]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/debug/1 [std_msgs/Int16]\r\n" + 
				" * /r1/debug/2 [std_msgs/Int16]\r\n" + 
				" * /r1/pilo/VbatmV [std_msgs/Int16]\r\n" + 
				" * /r1/pilo/switches [std_msgs/UInt32]\r\n" + 
				" * /r1/r1/pilo/time [std_msgs/Int32]\r\n" + 
				" * /r1/usart/rx [std_msgs/Int16]\r\n" + 
				" * /r1/usart/tx [std_msgs/Int16]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /r1/lcd/line1 [std_msgs/String]\r\n" + 
				" * /r1/lcd/line2 [unknown type]\r\n" + 
				" * /r1/motor2/0 [unknown type]\r\n" + 
				" * /r1/motor2/2 [unknown type]\r\n" + 
				" * /r1/motor2/4 [unknown type]\r\n" + 
				" * /r1/motor2/6 [unknown type]\r\n" + 
				" * /r1/motor2/8 [unknown type]\r\n" + 
				" * /r1/motor2/A [unknown type]\r\n" + 
				" * /r1/motor2/C [unknown type]\r\n" + 
				" * /r1/motor2/E [unknown type]\r\n" + 
				" * /r1/servo/C [unknown type]\r\n" + 
				" * /r1/servo/D [unknown type]\r\n" + 
				" * /r1/servo/E [std_msgs/UInt16]\r\n" + 
				" * /r1/servo/F [std_msgs/UInt16]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/rosserial_server_pince/get_loggers\r\n" + 
				" * /r1/rosserial_server_pince/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:37971/ ...\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/r1/ydlidar]\r\n" + 
				"Publications:\r\n" + 
				" * /r1/laser/scan [sensor_msgs/LaserScan]\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions: None\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /r1/ydlidar/get_loggers\r\n" + 
				" * /r1/ydlidar/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:33915/ ...\r\n" + 
				"--------------------------------------------------------------------------------\r\n" + 
				"Node [/rosout]\r\n" + 
				"Publications:\r\n" + 
				" * /rosout_agg [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Subscriptions:\r\n" + 
				" * /rosout [rosgraph_msgs/Log]\r\n" + 
				"\r\n" + 
				"Services:\r\n" + 
				" * /rosout/get_loggers\r\n" + 
				" * /rosout/set_logger_level\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"contacting node http://10.42.0.1:34801/ ...\r\n" + 
				"Pid: 1602\r\n" + 
				"Connections:\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/rosserial_server_pince (http://10.42.0.1:37971/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/rosserial_server_motor (http://10.42.0.1:34559/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/mpu6050 (http://10.42.0.1:43351/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/hardware_interface (http://10.42.0.1:35741/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/ydlidar (http://10.42.0.1:33915/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_laser_scan_filter (http://10.42.0.1:38453/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/robot_state_publisher (http://10.42.0.1:34461/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_behavior_tree (http://10.42.0.1:37319/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_imu_filter_madgwick (http://10.42.0.1:39791/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_map_server (http://10.42.0.1:38489/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_localization_map (http://10.42.0.1:39499/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_localization_odom (http://10.42.0.1:34909/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_amcl (http://10.42.0.1:43585/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_obstacle_map_server (http://10.42.0.1:43953/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/joy_xbox (http://10.42.0.1:44229/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_move_base (http://10.42.0.1:36991/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_teleop (http://10.42.0.1:41231/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/rosserial_message_info (http://10.42.0.1:41657/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_snap_map (http://10.42.0.1:45867/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/joint_state_publisher (http://10.42.0.1:36649/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				" * topic: /rosout\r\n" + 
				"    * to: /r1/r1_behavior_base_node (http://10.42.0.1:42107/)\r\n" + 
				"    * direction: inbound\r\n" + 
				"    * transport: TCPROS\r\n" + 
				"";
		
gv.parseNodeInfos(data);
System.out.println(gv.toString());
//JavaUtils.saveAs("c:\temp\test.dot",gv.toString());
System.out.println(" dot -Tsvg test.dot -o test.svg");//execute
	}

}
