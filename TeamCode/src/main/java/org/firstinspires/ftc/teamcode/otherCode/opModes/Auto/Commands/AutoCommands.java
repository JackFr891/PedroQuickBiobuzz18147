package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Commands;

import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.Positions;

public class AutoCommands {
    private final Follower f;
    private final Positions paths;
    private final CompleteRobot robot;

    public AutoCommands(Follower follower, Positions paths, CompleteRobot robot){
        this.f = follower;
        this.paths = paths;
        this.robot = robot;
    }

    public AutoCommands(Follower follower, Positions paths){
        this(follower, paths, null);
    }

    public CommandBuilder drivePath(Path path){ //Just follows the path
        return follow(f, path);
    }

    public CommandBuilder shootPath(Path path){ //shoots at the start of the move
        if (robot != null) {
            return parallel(follow(f,path), robot.shoot());
        }
        return follow(f, path);
    }

    public CommandBuilder intakePath(Path path){ //Turns on the intake and drives
        if (robot != null) {
            return parallel(follow(f,path), robot.intake.intake(true));
        }
        return follow(f, path);
    }
}
