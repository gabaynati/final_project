package com.example.socket_com;

import java.io.Serializable;

public class RGB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int red, green,blue;
	public RGB(int red, int green, int blue){
		this.setRed(red);
		this.setBlue(blue);
		this.setGreen(green);
	}
	//	public int getColorInt(){ 
	//		return Color.
	//	}
	public int getGreen() {
		return green;
	}
	public void setGreen(int green) {
		this.green = green;
	}
	public int getBlue() {
		return blue;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}
	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}
}
