
package org.usfirst.frc.team2854.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2854.robot.Robot;

/**
 *
 */
public class ExampleCommand extends Command {

    public ExampleCommand() {
        // Use requires() here to declare subsystem dependencies
        //requires(Robot.exampleSubsystem);
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
	protected void interrupted(){
		System.out.println("ERROR: INTERRUPTED");
	}
}
