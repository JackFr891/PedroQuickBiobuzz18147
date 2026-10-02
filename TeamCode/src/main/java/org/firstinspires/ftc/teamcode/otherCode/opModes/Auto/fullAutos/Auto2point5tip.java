package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.fullAutos;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.Positions;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Commands.AutoCommands;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "2.5 tip Auto", group = "Main Auto")
public class Auto2point5tip extends NextOpMode {
    private final CompleteRobot bot;
    private final Follower follower;
    private final Positions paths;
    private final AutoCommands commands;


    public Auto2point5tip(CompleteRobot robot, Positions paths, AutoCommands commands) {
        super(robot);
        this.bot = robot;
        follower = bot.getFollower();
        this.paths = paths;
        this.commands = commands;


        Scheduler.reset();
    }   //Tells the code to look at the robot setup in CompleteRobot

    @Override
    public void disabledPeriodic(){

    }
    @Override
    public void start(){
        bot.getFollower().setPose(Positions.rightStart);
        schedule(completeAuto()); //tells robot to run program underneath
    }

    @Override
    public void periodic(){
        bot.getFollower().update();
        Scheduler.execute(); // Code to make the Auto run properly

    }
    public Command completeAuto(){
        return sequential( //Sequential means it goes in order down the list
                commands.shootPath(paths.preloadShootToFirstShoot()), //tells it to run this path first and shoot
                //deploy flower collector
                commands.shootPath(paths.shoot1LeftFlower()), //once the first path is done, this one runs after
                commands.shootPath(paths.leftFlowerMiddleFlower()),
                commands.drivePath(paths.middleFlowerShoot2()),
                bot.shoot(),
                //turn off intake, fold up flower collector
                commands.drivePath(paths.toPark())
        );
    }
}
