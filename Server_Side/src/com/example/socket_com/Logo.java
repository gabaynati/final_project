package com.example.socket_com;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Logo extends Component {
	private Image img;
	public Logo(){
		this.img=new ImageIcon("Images/logo.jpg").getImage();
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
	public Image getLogo(){
		return this.img;
	}
}
