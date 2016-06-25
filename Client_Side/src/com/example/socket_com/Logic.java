/*
 * game logic class
 */

package com.example.socket_com;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;


public class Logic {

	public static final int FACE_HIT = 1, UPPER_BODY_HIT = 2, LOWER_BODY_HIT = 3;

	private JavaCameraView mOpenCvCameraView;      //openCV camera
	private Mat mGray, mRgba;                      //input camera frames
	private int rectSize;                          //size of current hit rectangle
	private Rect hitRect;                          //current hit rectangle


	//constructor
	public Logic(JavaCameraView CameraView){

		this.mOpenCvCameraView = CameraView;
	}

	//set mats
	public void setMats(Mat mGray, Mat mRgba){
		this.mGray = mGray;
		this.mRgba = mRgba;
	}

	public int getSizeOfRect(){
		return rectSize;
	}

	//check if the player hit someone and return the area index, if not hit return -1 
	public int isHit(Rect[] facesArrayWhileShoot, Rect[] upperBodyArrayWhileShoot, Rect[] lowerBodyArrayWhileShoot){

		if(checkForUpperBody(upperBodyArrayWhileShoot))
			return UPPER_BODY_HIT;

		if(checkForLowerBody(lowerBodyArrayWhileShoot))
			return LOWER_BODY_HIT;

		if(checkForFace(facesArrayWhileShoot))
			return FACE_HIT;
		/*
		if(checkForLowerBody(lowerBodyArrayWhileShoot)){
			facesArrayWhileShoot = null;
			upperBodyArrayWhileShoot = null;
			lowerBodyArrayWhileShoot = null;

			return LOWER_BODY_HIT;
		}
		 */

		return -1;
	}

	//after the hit check the specific player is injured and return is nick name, if is not defined player return null
	public String specificHit(HashMap<String,List<MatOfPoint>> colors){

		Iterator<Map.Entry<String, List<MatOfPoint>>> it = colors.entrySet().iterator();

		while (it.hasNext()) {

			Map.Entry<String, List<MatOfPoint>> entry = it.next();

			List<MatOfPoint> list = entry.getValue();

			for (ListIterator<MatOfPoint> iter = list.listIterator(); iter.hasNext(); ) {

				MatOfPoint element = iter.next();
				//get the point collection as list
				List<Point> listOfPoints = element.toList();            

				Iterator<Point> iterator = listOfPoints.iterator();         
				while(iterator.hasNext()){
					Point p = iterator.next();
					//enough that one point is in the current hit rectangle
					if(p.inside(hitRect))
						return entry.getKey();
				}
			}
		}

		return null;
	}

	//check if hit at some face in the frame, if yes return true, else return false
	private boolean checkForFace(Rect[] facesArrayWhileShoot){

		if(facesArrayWhileShoot != null){

			Point sightPoint = getSightPoint();

			for (int i = 0; i < facesArrayWhileShoot.length; i++){
				if(sightPoint.inside(facesArrayWhileShoot[i]))
					return true;
			}
		}

		return false;
	}

	//check if hit at some upper body in the frame, if yes return true, else return false
	private boolean checkForUpperBody(Rect[] upperBodyArrayWhileShoot){


		if(upperBodyArrayWhileShoot != null){

			Point sightPoint = getSightPoint();

			for (int i = 0; i < upperBodyArrayWhileShoot.length; i++){
				if(sightPoint.inside(upperBodyArrayWhileShoot[i])){
					rectSize = upperBodyArrayWhileShoot[i].width;
					hitRect = upperBodyArrayWhileShoot[i];
					return true;
				}
			}
		}
		/*if(upperBodyArrayWhileShoot != null){

				Point sightPoint = getSightPoint();

				for (int i = 0; i < upperBodyArrayWhileShoot.length; i++){
					for (int j = 0; j < facesArrayWhileShoot.length; j++){

						if(facesArrayWhileShoot[j].br().inside(upperBodyArrayWhileShoot[i]) && facesArrayWhileShoot[j].tl().inside(upperBodyArrayWhileShoot[i])){
							if(sightPoint.y >= facesArrayWhileShoot[j].br().y && sightPoint.inside(upperBodyArrayWhileShoot[i]))
								return true;
						}
					}
				}
			}*/

		return false;
	}

	//check if hit at some lower body in the frame, if yes return true, else return false
	private boolean checkForLowerBody(Rect[] lowerBodyArrayWhileShoot){

		if(lowerBodyArrayWhileShoot != null){

			Point sightPoint = getSightPoint();

			for (int i = 0; i < lowerBodyArrayWhileShoot.length; i++){
				if(sightPoint.inside(lowerBodyArrayWhileShoot[i])){
					rectSize = lowerBodyArrayWhileShoot[i].width;
					hitRect = lowerBodyArrayWhileShoot[i];
					return true;
				}
			}
		}

		return false;
	}

	//return the weapon sight point
	public Point getSightPoint(){

		return new Point(mOpenCvCameraView.getWidth()/2- getOffset("X"), mOpenCvCameraView.getHeight()/2- getOffset("Y"));
	}

	//return the offset of x and y between the openCV rectangle and the real screen
	//parameter: if xy = X return the x offset, if xy = Y return the y offset, else return -1
	private double getOffset(String xy){

		double w = mRgba.cols();
		double h = mRgba.rows();				

		if(xy.equals("X"))
			return (mOpenCvCameraView.getWidth() - w) / 2;

		else if(xy.equals("Y"))
			return (mOpenCvCameraView.getHeight() - h) / 2;

		else 
			return -1;
	}
}
