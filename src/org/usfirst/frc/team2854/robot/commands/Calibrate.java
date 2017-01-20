package org.usfirst.frc.team2854.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2854.robot.Robot;
import edu.wpi.first.wpilibj.AnalogGyro;

public class Calibrate extends Command{
	public Calibrate(){
		requires(Robot.tankDrive);
	}
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
	protected void initialize(){
	}
	protected void execute(){
		
	}
	protected boolean isFinished(){
		return false;
	}
	protected void end(){
		
	}
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}
}
