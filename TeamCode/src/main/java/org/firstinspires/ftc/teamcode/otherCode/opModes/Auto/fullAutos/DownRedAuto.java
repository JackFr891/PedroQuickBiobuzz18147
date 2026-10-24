package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.fullAutos;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.DownRedAutoPositions;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Commands.AutoCommands;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.FullAutoPositions;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Down Red Auto", group = "Main Auto")
public class DownRedAuto extends NextOpMode {
    private final CompleteRobot bot;
    private final Follower follower;
    private final DownRedAutoPositions paths;
    private final AutoCommands commands;

    public DownRedAuto(CompleteRobot robot) {
        super(robot);
        this.bot = robot;
        follower = bot.getFollower(hardwareMap);
        bot.turret.configureEncoder();
        this.paths = new DownRedAutoPositions();
        this.commands = new AutoCommands(follower, new FullAutoPositions(), bot);
        Scheduler.reset();
    }   //Tells the code to look at the robot setup in CompleteRobot

    @Override
    public void disabledPeriodic(){

    }
    @Override
    public void start(){
        if (bot.getFollower() != null) {
            bot.getFollower().setPose(DownRedAutoPositions.start);
        }
        bot.turret.setGoal(MiscVar.leftGoal);
        schedule(completeAuto()); //tells robot to run program underneath
    }
    @Override
    public void periodic(){
        if (bot.getFollower() != null) {
            bot.getFollower().update();
            Pose pose = bot.getFollower().pose();
            if (pose != null) {
                bot.fullAiming(pose);
            }
        }
        Scheduler.execute(); // Code to make the Auto run properly
    }
    public Command completeAuto(){
        return sequential( //Sequential means it goes in order down the list
                bot.shoot(),
                commands.drivePath(paths.startToPreFlower()),
                bot.intake.flowerArmDeploy(true),
                commands.drivePath(paths.preFlowerToFlower()),
                waitMs(500),
                commands.drivePath(paths.flowerToStart()),
                bot.intake.flowerArmDeploy(false),
                bot.shoot(),
                commands.drivePath(paths.startToPark()),
                bot.intake.intake(false)
        );
    }
}

