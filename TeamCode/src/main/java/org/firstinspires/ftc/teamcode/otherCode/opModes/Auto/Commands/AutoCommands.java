package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Commands;


import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.Positions;

public class AutoCommands {
    private final Follower f;
    private final Positions paths;
    private final CompleteRobot robot = new CompleteRobot();

    public AutoCommands(Follower follower, Positions paths){
        this.f = follower;
        this.paths = paths;
    }
    public CommandBuilder drivePath(Path path){ //Just follows the path
        return follow(f, path);
    }
    public CommandBuilder shootPath(Path path){ //shoots at the start of the move
        return parallel(follow(f,path), robot.shoot()); //Parallel means it tells the robot to do both at once
    }
    public CommandBuilder intakePath(Path path){ //Turns on the intake and drives
        return parallel(follow(f,path),robot.intake.intake(true));
    }

}
