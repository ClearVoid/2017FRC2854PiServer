package org.usfirst.frc.team2854.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2854.robot.*;
import edu.wpi.first.wpilibj.Joystick;

public class TeleOp extends Command{
	private static Joystick driveStick;
	public TeleOp(){
		driveStick = Robot.stick[0];
		requires(Robot.driveTrain);
	}
	@Override
	protected void initialize(){
		Robot.driveTrain.setPower(driveStick.getY(),driveStick.getY());
	}
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	
}
