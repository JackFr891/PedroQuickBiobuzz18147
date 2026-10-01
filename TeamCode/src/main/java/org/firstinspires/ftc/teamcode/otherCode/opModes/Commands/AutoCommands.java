package org.firstinspires.ftc.teamcode.otherCode.opModes.Commands;


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
    public CommandBuilder drivePath(Path path){
        return follow(f, path);
    }
    public CommandBuilder shootPath(Path path){
        return parallel(follow(f,path), robot.shoot());
    }

}
