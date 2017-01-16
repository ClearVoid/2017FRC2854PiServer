package org.usfirst.frc.team2854.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2854.robot.Robot;

import org.usfirst.frc.team2854.robot.subsystems.*;

public class CenterRobot extends Command{
	private static int driveTrainType;
	private static DriveTrain driveTrain;
	public CenterRobot(int driveTrainType){
		this.driveTrainType = driveTrainType;
		requires(Robot.hSlide);
		requires(Robot.tankDrive);
		switch(driveTrainType){
		case 0: requires(Robot.hSlide);driveTrain = Robot.hSlide;break;
		case 1: requires(Robot.tankDrive);driveTrain = Robot.tankDrive;break;
		}
	}
	private static void rotate(int theta, int omega,int driveTrainType){
		//rad and rad/seconds
		
	}
	protected void initialize() {
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return false;
    }
    
    protected void end() {
    }

    protected void interrupted() {
    	System.out.println("ERROR: CenterRobot INTERUPTED");
    }

}
