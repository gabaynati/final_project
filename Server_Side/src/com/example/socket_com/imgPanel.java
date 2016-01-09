package com.example.socket_com;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class imgPanel extends JPanel {
	private Image img;
	public imgPanel(String url){
		this.img=new ImageIcon(url).getImage();
		//this.setPreferredSize(new Dimension(822, 937));
	}
	public void paintComponent(Graphics g) {
		//g.drawString("H&S", 40, 40);
	//	g.drawImage(img, 0, 0, null);
	}
	public Image getLogo(){
		return this.img;
	}
	
}
