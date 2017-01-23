package org.usfirst.frc.team2854.robot.commands;

import org.usfirst.frc.team2854.robot.Robot;
import org.usfirst.frc.team2854.robot.subsystems.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.AnalogGyro;

public class Calibrate extends Command{
	private static AnalogGyro gyro;
	private static DriveTrain drive;
	
	public Calibrate(){
		requires(Robot.driveTrain);
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
	private static void deltaDrive(){
		
	}
	public float[] run(){
		float[] power = new float[2];
		return power;
	}
	
	protected void initialize(){
		gyro = Robot.gyro;
		drive = Robot.driveTrain;
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
