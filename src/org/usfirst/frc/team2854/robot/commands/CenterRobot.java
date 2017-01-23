package org.usfirst.frc.team2854.robot.commands;

import java.math.*;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team2854.robot.Robot;
import org.usfirst.frc.team2854.robot.subsystems.*;

public class CenterRobot extends Command{
	private static int driveTrainType;
	private static DriveTrain driveTrain;
	private static float targetTheta;
	public CenterRobot(float targetTheta,float omega){
		//driveTrain = Robot.driveTrain;
		requires(Robot.driveTrain);
		this.targetTheta = targetTheta;
	}
	private static boolean checkTheta(float threshold){
		float currentTheta;
		double S0 = Robot.driveTrain.getDistance(Robot.driveTrain.encoder[0]);
		double S1 = Robot.driveTrain.getDistance(Robot.driveTrain.encoder[1]);
		int direction = S0 > S1  ? 1 : -1;
		float magTheta = (float) ((S0>S1 ? S0:S1)/driveTrain.width);
		currentTheta = direction * magTheta;
		if(Math.abs(targetTheta-currentTheta) < threshold)return true;else return false;
	}
	@Override
	protected void initialize() {
		
    }
	@Override
    protected void execute() {
    }

    protected boolean isFinished() {
        return false;
    }
    
    protected void end() {
    }

    protected void interrupted() {
    	System.out.println("ERROR: CenterRobot INTERRUPTED");
    }

}
