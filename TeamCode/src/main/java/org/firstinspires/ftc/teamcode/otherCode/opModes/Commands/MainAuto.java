package org.firstinspires.ftc.teamcode.otherCode.opModes.Commands;

import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.Positions;

public class MainAuto {
    private CompleteRobot bot;
    private final Follower follower;
    private final Positions paths;
    private final AutoCommands commands;
    public MainAuto(CompleteRobot bot, Positions paths, AutoCommands commands){
        this.bot = bot;
        follower = bot.getFollower();
        this.paths = paths;
        this.commands = commands;
    }
}
