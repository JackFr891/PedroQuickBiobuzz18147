package org.firstinspires.ftc.teamcode.otherCode.opModes.Teleop;

import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;


@NextTeleop(name = "Teleop", group = "Auto Drive")
public class AutoDrive extends NextOpMode {
    private final CompleteRobot bot;


    public AutoDrive(CompleteRobot robot) {
        super(robot);
        this.bot = robot;

        Scheduler.reset();
    }   //Tells the code to look at the robot setup in CompleteRobot

    @Override
    public void disabledPeriodic(){//While the code is initialized but not fully running

    }


    @Override
    public void start(){ //When code is started
        //Define Controls here
        //completeRobot.init().schedule();  //Only use if we have a standard initialization function setup in CompleteRobot

        CommandGamepad gp1 = new CommandGamepad(gamepad1);
        CommandGamepad gp2 = new CommandGamepad(gamepad2);

        bot.startDrive(gamepad1);

        gp2.dpadUp().onTrue(bot.turret.AIMcomp(true));// Commands are tied to controllers like this
        gp2.dpadDown().onTrue(bot.turret.AIMcomp(false));





    }
    @Override
    public void periodic(){ // While the code is running
//        completeRobot.getFollower().update(); // updates follower, giving us current robot pose


    }
    @Override
    public void end(){ // When the stop button is pressed

    }
}
