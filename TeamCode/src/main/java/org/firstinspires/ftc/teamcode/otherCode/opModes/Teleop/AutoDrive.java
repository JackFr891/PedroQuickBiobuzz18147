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
        bot.init(); // runs init command in the Complete robot class

        CommandGamepad gp1 = new CommandGamepad(gamepad1);
        CommandGamepad gp2 = new CommandGamepad(gamepad2);

        bot.startDrive(gamepad1);

        gp2.dpadUp().onTrue(bot.turret.AIMcomp(true));// Commands are tied to controllers like this
        gp2.dpadDown().onTrue(bot.turret.AIMcomp(false));
        gp1.leftTrigger().isOver(0.3).toggleOnTrue(null); // make turn on intake once command is made
        gp2.a().onTrue(null).onFalse(null); // make deploy flower descore mech when pressed, pull up when released





    }
    @Override
    public void periodic(){ // While the code is running
//        completeRobot.getFollower().update(); // updates follower, giving us current robot pose


        bot.shootToLight('l'); // Tells the bot to set light 'l' to the shooter
        bot.turretToLight('r'); // sets light 'r' to the turret


    }
    @Override
    public void end(){ // When the stop button is pressed

    }
}
