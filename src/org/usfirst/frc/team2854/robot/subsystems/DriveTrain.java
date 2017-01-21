package org.usfirst.frc.team2854.robot.subsystems;


import org.usfirst.frc.team2854.editLib.RobotDrive;
import org.usfirst.frc.team2854.robot.Robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.PIDSourceType;
//import edu.wpi.first.wpilibj.RobotDrive;

public class DriveTrain extends Subsystem{
	private static final int driveCimCount = 4;
	private SpeedController[] driveCim = new SpeedController[driveCimCount];//0 = fl, 1 = fr, 2 = bl, 4, br; Even numbers left, odd right; int/2 is front or back
	private static final int driveEncoderCount = 2;
	private Encoder[] driveEncoder = new Encoder[driveEncoderCount];
	private static final int gyroPort = 2;
	private AnalogGyro gyro;
	protected RobotDrive driveTrain;
	public DriveTrain(float speedConstants){
		for(int i =0;i<driveCimCount;i++){driveCim[i] = new Victor(i);}
		for(int i = 0; i < driveCimCount;i++){LiveWindow.addActuator("DriveTrain", String.valueOf(i), (Victor) driveCim[i]);}
		
		driveTrain = new RobotDrive(driveCim,driveCimCount);
		driveTrain.setSafetyEnabled(true);
		driveTrain.setExpiration(0.1);
		driveTrain.setSensitivity(0.5);
		driveTrain.setMaxOutput(1.0);
		
		
		gyro = new AnalogGyro((gyroPort));
		if (Robot.isReal()) {
			gyro.setSensitivity(0.007); // TODO: Handle more gracefully?
		}
		LiveWindow.addSensor("DriveTrain", "Gyro", gyro);
		
	}
	public void initDefaultCommand(){
	}
	public float[] deltaDrive(double leftPower, double rightPower,float deltaT){
		driveTrain.setArrayMotorOutputs(leftPower, rightPower);
		float[] deltaD = new float[2];
		return deltaD;
		
	}
}
