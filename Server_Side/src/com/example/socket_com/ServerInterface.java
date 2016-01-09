package com.example.socket_com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class ServerInterface extends JPanel {
	private imgPanel logo;
	private JLabel team1Icon,team2Icon,serverMessages;

	
	private JTable team1Players;
	private JTable team2Players;
	private Image img;
	public ServerInterface(){
		
//		team1Label=new JLabel("Team1 players");
//		team2Label=new JLabel("Team2 players");
//		team1Label.setFont (new Font("", Font.BOLD,30));
//		serverMessages.setFont (new Font("Courier", Font.BOLD,30));
//		serverMessages.setForeground(Color.RED);
//		team2Label.setForeground(Color.BLACK);
		
		team1Icon=new JLabel(new ImageIcon("Images/team1.jpg"));
		team2Icon=new JLabel(new ImageIcon("Images/team2.jpg"));
		team1Icon.setForeground(Color.LIGHT_GRAY);
		team2Icon.setForeground(Color.LIGHT_GRAY);
		
		
		img=new ImageIcon("Images/logo.jpg").getImage();
		serverMessages=new JLabel();
		serverMessages.setFont (new Font("Courier", Font.BOLD,10));
		serverMessages.setForeground(Color.DARK_GRAY);
		update();
		
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.drawImage(img,0,0,null);

	}

	public JTable getActivePlayers(Game game,int team_index){
		JTable tab = null;
		Vector<String> columnNames = new Vector<String>();
		columnNames.addElement("nickName");
		columnNames.addElement("IP Address");


		Vector<Vector> rowData = new Vector<Vector>();
		Vector<Player> team;
		if(team_index==1)
			team=game.getTeam1Players();
		else
			team=game.getTeam2Players();

		///	
		Vector<String> firstRow = new Vector<String>();
		firstRow.addElement("Nick Name");
		firstRow.addElement("Socket Address");
		rowData.addElement(firstRow);
		
		for(int i=0;i<team.size();i++){
			Vector<String> row = new Vector<String>();
			row.addElement(team.elementAt(i).getNickName().toString());
			row.addElement(team.elementAt(i).getSocket().getRemoteSocketAddress().toString());
			rowData.addElement(row);
		}

		tab=new JTable(rowData,columnNames);

		return tab;

	}
	
	public String getServerMessages(Vector<String> serverLogs){
		String str="<html>";
		for(int i=0;i<serverLogs.size();i++)
			str+="<br>"+serverLogs.elementAt(i)+"</br>";
		str+="</html>";
		return str;
	}
	
	
	
	
	public void update(){
		this.removeAll();
	
		
		/*
		
		JPanel serverLogPanel=new JPanel();
		serverLogPanel.setLayout(new BorderLayout());
		serverMessages.setText(getServerMessages(Main.serverLogs));
		serverLogPanel.add(serverMessages,BorderLayout.CENTER);
		logo.add(serverLogPanel,BorderLayout.CENTER);
		
		
		
		team1Players=getActivePlayers(Main.game, 1);
		team2Players=getActivePlayers(Main.game, 2);
		
		
		
		JPanel team1Panel=new JPanel();
		team1Panel.setLayout(new BorderLayout());
		team1Panel.add(team1Icon,BorderLayout.NORTH);
		team1Panel.add(team1Players,BorderLayout.CENTER);
		logo.add(team1Panel,BorderLayout.WEST);
		
		
		
		
		JPanel team2Panel=new JPanel();
		team2Panel.setLayout(new BorderLayout());
		team2Panel.add(team2Icon,BorderLayout.NORTH);
		team2Panel.add(team2Players,BorderLayout.CENTER);
		logo.add(team2Panel,BorderLayout.EAST);
		
		*/
		
		
		
		
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JPanel p1=new JPanel();
		JPanel p2=new JPanel();
		JPanel p3=new JPanel();
		p1.setOpaque(false);
		p2.setOpaque(false);
		p3.setOpaque(false);
		
		
		
		//adding the server log
		serverMessages.setText(getServerMessages(Main.serverLogs));
		c.gridx=1;
		c.gridy=0;
		p1.add(serverMessages);
		this.add(p1,c);
		
		
		
		
		//adding the teams
		team1Players=getActivePlayers(Main.game, 1);
		team2Players=getActivePlayers(Main.game, 2);
		//team1
		c.gridx=0;
		c.gridy=0;
		p2.setLayout(new GridBagLayout());
		p2.add(team1Icon,c);
		c.gridx=0;
		c.gridy=1;
		c.fill=GridBagConstraints.BOTH;
		p2.add(team1Players,c);
		c.gridx=0;
		c.gridy=0;
		this.add(p2,c);
		
		
		//team2
		c.gridx=0;
		c.gridy=0;
		p3.setLayout(new GridBagLayout());
		p3.add(team2Icon,c);
		c.gridx=0;
		c.gridy=1;
		c.fill=GridBagConstraints.BOTH;
		p3.add(team2Players,c);
		c.gridx=2;
		c.gridy=0;
		this.add(p3,c);
		
		
		//this.add(logo);
		repaint();
		revalidate();
	}

}
