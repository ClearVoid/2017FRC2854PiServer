package org.usfirst.frc.team2854.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class DriveTrain extends Subsystem{
	private static int driveTrainType;
	private SpeedController frontLeft,frontRight,backLeft,backRight;
	public DriveTrain(int driveTrainType){
		this.driveTrainType = driveTrainType;
	}
	private static void rotate(){
		
	}
	public void initDefaultCommand(){
		
	}

}
