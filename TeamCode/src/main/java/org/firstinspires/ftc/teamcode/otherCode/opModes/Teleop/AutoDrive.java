package org.firstinspires.ftc.teamcode.otherCode.opModes.Teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;

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
        bot.getFollower(hardwareMap);
        bot.shooter.configureEncoder(hardwareMap);
        bot.turret.configureEncoder();
        bot.configureMotorDirections();
        bot.init(); // runs init command in the Complete robot class
        bot.shooter.startFlywheel(); // set the flywheel speed immediately when teleop starts


        CommandGamepad gp1 = new CommandGamepad(gamepad1);
        CommandGamepad gp2 = new CommandGamepad(gamepad2);

        bot.startDrive(gamepad1);


        //gamepad 1
        gp1.rightTrigger().isOver(0.3)
                .toggleOnTrue(bot.intake.intake(true))   //make turn intake on
                .toggleOnFalse(bot.intake.intake(false)); //make turn intake off

        gp1.leftTrigger().isOver(.3)
                .toggleOnTrue(bot.intake.outtake(true))   //make turn intake purge on
                .toggleOnFalse(bot.intake.outtake(false)); //make turn intake purge off
        gp1.leftStickButton().and(gp1.rightStickButton())
                .onTrue(bot.homeRobot())
        ;

        //gamepad 2
        gp2.dpadUp().onTrue(bot.turret.AIMcomp(true));// Commands are tied to controllers like this
        gp2.dpadDown().onTrue(bot.turret.AIMcomp(false)); //turns Shoot on the Move on and off, in case its bugging


        gp2.rightBumper().onTrue(bot.intake.flowerArmDeploy(true)).onFalse(bot.intake.flowerArmDeploy(false)); // make deploy flower descore mech when pressed, pull up when released


        gp2.y().onTrue(bot.shootFlower()); // when 'y' is pressed, shoot into the flower
        gp2.a().onTrue(bot.shoot()); // When 'a' is pressed, shoot wherever the turret is currently aimed






    }
    @Override
    public void periodic(){ // While the code is running
        Follower f = bot.getFollower();
        if (f != null) {
            f.update(); // updates follower, giving us current robot pose
            Pose pose = f.pose();
            if (pose != null) {
                bot.fullAiming(pose);
            }
        }
        bot.fullShooterToLight('l').schedule(); //tells the robot to keep light 'l' updated
        bot.standardTelemetry();


    }
    @Override
    public void end(){ // When the stop button is pressed
        bot.shutDown();


    }
}
