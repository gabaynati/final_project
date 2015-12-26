package com.example.socket_com;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class ServerInterface extends JPanel {
private Logo logo;
JLabel logoLabel;
JTable team1Players;
JTable team2Players;
public ServerInterface(){

	logo=new Logo();	
	this.setLayout(new BorderLayout());
	team1Players=getActivePlayers(Main.game, 1);
	team2Players=getActivePlayers(Main.game, 2);
	this.add(team1Players, BorderLayout.WEST);
	this.add(team2Players, BorderLayout.EAST);
	repaint();
}

public void paintComponent(Graphics g) {
    super.paintComponents(g);
    logo.paintComponent(g);
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
    row.addElement(team.elementAt(i).getAddress().toString());
    rowData.addElement(row);
    }
    
    tab=new JTable(rowData,columnNames);
	return tab;
	
}


}
