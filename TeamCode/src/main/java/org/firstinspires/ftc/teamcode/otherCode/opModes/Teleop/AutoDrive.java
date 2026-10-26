package org.firstinspires.ftc.teamcode.otherCode.opModes.Teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

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
        bot.initNormal(); // runs init command in the Complete robot class
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
        gp1.dpadRight()
                .onTrue(bot.homeRobot(MiscVar.gardenHoming)); //Sets bot position to Garden corner facing along alliance wall
        gp1.dpadLeft()
                .onTrue(bot.homeRobot(MiscVar.leftCorner)); //Sets bot position to bottom left corner facing along alliance wall
        gp1.dpadLeft()
                .onTrue(bot.homeRobot(MiscVar.humanPlayerBox)); //Sets bot position to left edge on left edge of human player box

        //gamepad 2
        gp2.dpadUp()
                        .toggleOnTrue(bot.shooter.adjustShooterSpeed('u'));//bumps up shoot speed
        gp2.dpadDown()
                .toggleOnTrue(bot.shooter.adjustShooterSpeed('d'));//bumps down shoot speed


        gp2.rightBumper()
                .onTrue(bot.intake.flowerArmDeploy(true)).onFalse(bot.intake.flowerArmDeploy(false)); // make deploy flower descore mech when pressed, pull up when released


        gp2.a()
                .onTrue(bot.shootFull()); // When 'a' is pressed, aim and shoot
        gp2.y()
                .onTrue(bot.shootSimple()); // when 'y' is pressed, just shoot, don't aim






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


        bot.standardTelemetry();


    }
    @Override
    public void end(){ // When the stop button is pressed
        bot.shutDown();
        bot.getFollower().stop();


    }
}
