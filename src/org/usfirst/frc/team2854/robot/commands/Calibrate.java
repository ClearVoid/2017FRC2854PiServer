package org.usfirst.frc.team2854.robot.commands;

public class Calibrate {
	public Calibrate(){}
	private static float[] limit(float[] input){
		float ratio;
		if(input[0] > input[1]){
			ratio = input[1]/input[0];
			input[0] = 1;
			input[1] = ratio;
		}
		else{
			ratio = input[0]/input[1];
			input[1]= 1;
			input[0] = ratio;
		}
		return input;
	}
	public float[] run(){
		float[] power = new float[2];
		return power;
	}
}
