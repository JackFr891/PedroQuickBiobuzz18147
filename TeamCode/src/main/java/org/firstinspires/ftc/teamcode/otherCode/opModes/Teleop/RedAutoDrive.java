package org.firstinspires.ftc.teamcode.otherCode.opModes.Teleop;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.List;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;


@NextTeleop(name = "Red Teleop", group = "Auto Drive")
public class RedAutoDrive extends NextOpMode {
    private final CompleteRobot completeRobot;

    public RedAutoDrive(CompleteRobot completeRobot) {
        super(completeRobot);
        this.completeRobot = completeRobot;

        Scheduler.reset();
    }   //Tells the code to look at the robot setup in CompleteRobot

    @Override
    public void disabledPeriodic(){//While the code is initialized but not fully running

    }


    @Override
    public void start(){ //When code is started
        //Define Controls here
        //completeRobot.init().schedule();  *Only use if we have a standard initialization function setup in CompleteRobot

        completeRobot.startDrive(gamepad1);

    }
    @Override
    public void periodic(){ // While the code is running
        completeRobot.getFollower().update(); // updates follower, giving us current robot pose


    }
    @Override
    public void end(){ // When the stop button is pressed

    }
}
