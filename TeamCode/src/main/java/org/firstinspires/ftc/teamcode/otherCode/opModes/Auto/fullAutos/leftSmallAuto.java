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
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Commands.AutoCommands;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.LeftSmallPositions;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.MainAutoPositions;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.TestPositions;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Left Auto", group = "Main Auto")
public class leftSmallAuto extends NextOpMode {
    private final CompleteRobot bot;
    private final Follower follower;
    private final LeftSmallPositions paths;
    private final AutoCommands commands;
    public leftSmallAuto(CompleteRobot robot) {
        super(robot);
        this.bot = robot;
        follower = bot.getFollower(hardwareMap);
        bot.turret.configureEncoder();
        this.paths = new LeftSmallPositions();
        // AutoCommands.drivePath only uses the follower; its Positions argument is unused.
        this.commands = new AutoCommands(follower, new MainAutoPositions(), bot);
        Scheduler.reset();
    }   //Tells the code to look at the robot setup in CompleteRobot
    public Command autoCommand(){
        return sequential(
                waitMs(20000), //wait 20 seconds to let teammate shoot
                commands.shootPath(paths.path1()),
                bot.intake.flowerArmDeploy(true),
                commands.intakePath(paths.path2()),
                waitMs(500),
                bot.shootFull(),
                commands.drivePath(paths.path3()),
                bot.intake.flowerArmDeploy(false)
        );
    }

    @Override
    public void disabledPeriodic(){
        telemetry.addData("starting Pose", paths.start); // tells driver where to put bot
        telemetry.addLine("Starts on left wall facing other alliance with back of bot on first tile line");
        telemetry.update();

    }

    @Override
    public void start() {
        if (follower != null) {
            follower.setPose(paths.start);
        }
        schedule(autoCommand());
    }

    @Override
    public void periodic() {
        if (follower != null) {
            follower.update();
            Pose pose = follower.pose();
            if (pose != null) {
                bot.updateAim(pose);
            }
        }
        bot.standardTelemetry();
    }

    @Override
    public void end() {
        if (follower != null) {
            follower.stop();
        }
    }
}
