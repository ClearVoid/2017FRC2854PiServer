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
	private static final int driveCimCount = 4;
	private SpeedController[] driveCim = new SpeedController[driveCimCount];//0 = fl, 1 = fr, 2 = bl, 4, br; Even numbers left, odd right; int/2 is front or back
	private static final int driveEncoderCount;
	private Encoder[] driveEncoder = new Encoder[driveEncoderCount];
	private RobotDrive drive;
	
	public DriveTrain(int driveTrainType){
		this.driveTrainType = driveTrainType;
		for(int i =0;i<driveCimCount;i++){driveCim[i] = new Victor(i);}
		for(int i = 0; i < driveCimCount;i++){LiveWindow.addActuator("DriveTrain", String.valueOf(i), (Victor) driveCim[i]);}
		drive = new RobotDrive();
		
	}
	private void rotate(){
		
	}
	public void initDefaultCommand(){
		
	}

}
