package org.firstinspires.ftc.teamcode.otherCode.opModes.Teleop;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.hardware.actuators.NextRGBIndicator;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;


@NextTeleop(name = "Teleop", group = "Auto Drive")
public class AutoDrive extends NextOpMode {
    // should work 4 both, field is rotated not mirrored, origin is always garden corner
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


        bot.fullShooterToLight('l'); //tells the robot to keep light 'l' updated



        Pose pose = bot.getFollower().pose();
        if (pose.y()>72){
            bot.turret.setGoal(MiscVar.leftGoal);
            bot.lights.setLight('r', NextRGBIndicator.Color.VIOLET);
        }else {
            bot.turret.setGoal(MiscVar.rightGoal);
            bot.lights.setLight('r', NextRGBIndicator.Color.WHITE);
        }


    }
    @Override
    public void end(){ // When the stop button is pressed
        bot.shutDown();


    }
}
