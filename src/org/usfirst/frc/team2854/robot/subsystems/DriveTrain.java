package org.usfirst.frc.team2854.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
public class DriveTrain extends Subsystem{
	private static int driveTrainType;
	public DriveTrain(int driveTrainType){
		this.driveTrainType = driveTrainType;
	}
	
	public void initDefaultCommand(){
		
	}

}
