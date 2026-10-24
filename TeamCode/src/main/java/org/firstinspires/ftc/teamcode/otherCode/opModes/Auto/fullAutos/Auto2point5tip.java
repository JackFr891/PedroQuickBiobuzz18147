package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.fullAutos;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.DownRedAutoPositions;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.FullAutoPositions;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Commands.AutoCommands;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "2.5 tip Auto", group = "Main Auto")
public class Auto2point5tip extends NextOpMode {
    private final CompleteRobot bot;
    private final Follower follower;
    private final FullAutoPositions paths;
    private final AutoCommands commands;


    public Auto2point5tip(CompleteRobot robot) {
        super(robot);
        this.bot = robot;
        follower = bot.getFollower(hardwareMap);
        bot.turret.configureEncoder();
        this.paths = new FullAutoPositions();
        this.commands = new AutoCommands(follower, paths, bot);
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
        bot.turret.setGoal(MiscVar.rightGoal);
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
                bot.intake.flowerArmDeploy(false),
                commands.shootPath(paths.preloadShootToFirstShoot()), //tells it to run this path first and shoot, goes all the way to being ready 4 2nd shoot
                bot.intake.flowerArmDeploy(true),
                commands.shootPath(paths.shoot1LeftFlower()), //once the first path is done, this one runs after
                commands.shootPath(paths.leftFlowerMiddleFlower()),
                commands.drivePath(paths.middleFlowerShoot2()),
                bot.shoot(),
                bot.intake.intake(false),
                bot.intake.flowerArmDeploy(false),
                commands.drivePath(paths.toPark())
        );
    }
}
