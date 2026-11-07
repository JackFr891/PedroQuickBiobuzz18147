package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Commands;

import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.MainAutoPositions;

public class AutoCommands {
    private final Follower f;
    private final MainAutoPositions paths;
    private final CompleteRobot robot;

    public AutoCommands(Follower follower, MainAutoPositions paths, CompleteRobot robot){
        this.f = follower;
        this.paths = paths;
        this.robot = robot;
    }

    public AutoCommands(Follower follower, MainAutoPositions paths){
        this(follower, paths, null);
    }

    public CommandBuilder drivePath(Path path){ //Just follows the path
        return drivePath(path, 1.0);
    }

    /**
     * Follows a path at the requested fraction of full drive speed.
     *
     * @param speed a value from 0.0 to 1.0, where 1.0 is 100% speed
     */
    public CommandBuilder drivePath(Path path, double speed){
        if (speed < 0.0 || speed > 1.0) {
            throw new IllegalArgumentException("Drive speed must be between 0.0 and 1.0");
        }

        Foresight foresight = (Foresight) f.algorithm();
        return follow(f, path.with(foresight.config.maxPathSpeed.at(speed)));
    }

    public CommandBuilder shootPath(Path path){ //shoots and moves
        if (robot != null) {
            return parallel(follow(f,path), robot.shootFull());
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
