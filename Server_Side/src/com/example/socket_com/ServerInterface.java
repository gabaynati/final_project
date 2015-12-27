package com.example.socket_com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.net.Inet4Address;
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
	private JLabel team1Icon,team2Icon;
	
	
	private JTable team1Players;
	private JTable team2Players;
	private LayoutManager layout;
	public ServerInterface(){
		
//		team1Label=new JLabel("Team1 players");
//		team2Label=new JLabel("Team2 players");
//		team1Label.setFont (new Font("", Font.BOLD,30));
//		team2Label.setFont (new Font("Courier", Font.BOLD,30));
//		team1Label.setForeground(Color.BLACK);
//		team2Label.setForeground(Color.BLACK);
		
		team1Icon=new JLabel(new ImageIcon("Images/team1.jpg"));
		team2Icon=new JLabel(new ImageIcon("Images/team2.jpg"));
		logo=new imgPanel("Images/logo.jpg");
		layout=new BorderLayout();
		update();
		
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		//logo.paintComponent(g);

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

		for(int i=0;i<team.size();i++){
			Vector<String> row = new Vector<String>();
			row.addElement(team.elementAt(i).getNickName().toString());
			row.addElement(team.elementAt(i).getSocket().getRemoteSocketAddress().toString());
			rowData.addElement(row);
		}

		tab=new JTable(rowData,columnNames);

		return tab;

	}
	
	public void update(){
		this.removeAll();
	
		this.setLayout(layout);

		this.add(logo,BorderLayout.CENTER);
		
		
		team1Players=getActivePlayers(Main.game, 1);
		team2Players=getActivePlayers(Main.game, 2);
		
		
		
		JPanel team1Panel=new JPanel();
		team1Panel.add(team1Icon,BorderLayout.PAGE_START);
		team1Panel.add(team1Players,BorderLayout.CENTER);
		this.add(team1Panel,BorderLayout.LINE_START);
		
		
		
		
		JPanel team2Panel=new JPanel();
		team2Panel.add(team2Icon,BorderLayout.PAGE_START);
		team2Panel.add(team2Players,BorderLayout.CENTER);
		this.add(team2Panel,BorderLayout.LINE_END);
		
		
		
		repaint();
		revalidate();
	}

}
