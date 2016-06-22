package com.example.socket_com;

import java.io.Serializable;

public class RGB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double red, green,blue;
	public RGB(double red, double green, double blue){
		this.setRed(red);
		this.setBlue(blue);
		this.setGreen(green);
	}
	//	public int getColorInt(){ 
	//		return Color.
	//	}
	public double getGreen() {
		return green;
	}
	public void setGreen(double green) {
		this.green = green;
	}
	public double getBlue() {
		return blue;
	}
	public void setBlue(double blue) {
		this.blue = blue;
	}
	public double getRed() {
		return red;
	}
	public void setRed(double red) {
		this.red = red;
	}
	@Override
	public String toString(){
		String str="";
		str="red: "+this.red+"\n"+"blue: "+this.blue+"\n"+"green: "+this.green+"\n";
		return str;
	}
}
