package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.fullAutos;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Commands.AutoCommands;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.Positions;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.TestPositions;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Test Auto", group = "Main Auto")
public class TestAuto extends NextOpMode {
    private static final PoseFactory poseFactory = PoseFactory.degrees();
    private static final Pose aimPoint = poseFactory.of(0, -48, 0);

    private final CompleteRobot bot;
    private final Follower follower;
    private final TestPositions paths;
    private final AutoCommands commands;
    public TestAuto(CompleteRobot robot) {
        super(robot);
        this.bot = robot;
        follower = bot.getFollower(hardwareMap);
        bot.turret.configureEncoder();
        this.paths = new TestPositions();
        // AutoCommands.drivePath only uses the follower; its Positions argument is unused.
        this.commands = new AutoCommands(follower, new Positions(), bot);
        Scheduler.reset();
    }   //Tells the code to look at the robot setup in CompleteRobot


    public Command AutoTest(){
        return sequential(
                commands.drivePath(paths.testPathConstantGo()),
                commands.drivePath(paths.testPathConstantBack()),
                commands.drivePath(paths.testPathLinearGo()),
                commands.drivePath(paths.testPathLinearBack()),
                commands.drivePath(paths.testPathPiecewiseGo()),
                commands.drivePath(paths.testPathPiecewiseBack()),
                Commands.instant(() -> {
                    if (follower != null) {
                        follower.stop();
                    }
                })
        );
    }

    @Override
    public void start() {
        if (follower != null) {
            follower.setPose(TestPositions.startPose);
        }
        bot.turret.setGoal(aimPoint);
        bot.turret.AIM(true).schedule();

        // This test is for pathing and turret aiming only.
        bot.intake.intake(false).schedule();

        schedule(AutoTest());
    }

    @Override
    public void periodic() {
        if (follower != null) {
            follower.update();
        }
    }

    @Override
    public void end() {
        if (follower != null) {
            follower.stop();
        }
    }
}
