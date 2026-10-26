package org.firstinspires.ftc.teamcode.otherCode;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Intake;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher.AngleChange;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher.Shooter;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher.Turret;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Lights;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.Positions;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MathFunctions;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.actuators.NextRGBIndicator;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.drive.DriveCommands;

public class CompleteRobot implements NextRobot {
    private Follower follower;
    /** Distance from the turret pivot to the currently selected target, in field units. */
    private static double targetDistance;
    private boolean turretCanReachCalculatedAngle;

    //Add in all mechanisms that are in robot
    public final Drivetrain drivetrain = new Drivetrain(); //implement other mechanisms the same way
    public static final Shooter shooter = new Shooter();
    public final Turret turret = new Turret();
    public final Lights lights = new Lights();
    public final Intake intake = new Intake();
    public final AngleChange hood = new AngleChange();

    public void startDrive(Gamepad gamepad){
        DriveCommands.mecanumDrive(
                drivetrain.frontLeft,
                drivetrain.frontRight,
                drivetrain.backLeft,
                drivetrain.backRight,
                gamepad
        ).schedule();
    }   // Creates the Drivetrain and links it to your controls

    /** Reapply configured motor directions after all hardware wrappers are initialized. */
    public void configureMotorDirections() {
        drivetrain.frontLeft.setDirection(dev.nextftc.hardware.actuators.NextMotor.Direction.REVERSE);
        intake.intake.setDirection(dev.nextftc.hardware.actuators.NextMotor.Direction.REVERSE);
        shooter.shooter.setDirection(dev.nextftc.hardware.actuators.NextMotor.Direction.REVERSE);
        turret.turret.setDirection(dev.nextftc.hardware.actuators.NextMotor.Direction.REVERSE);
    }

    public Command homeRobot(){
        return Commands.instant(()->{
            Follower f = getFollower();
            if (f != null) {
                f.setPose(Positions.gardenHoming);
            }
        });
    }

    public Follower getFollower(HardwareMap h) {
        if (follower == null && h != null) {
            follower = Constants.create(h);
            follower.setPose(Positions.gardenHoming);
        }
        return follower;
    }

    public Follower getFollower() {
        return follower;
    }

    public void updateFollower(){
        if (follower != null) {
            follower.update();
        }
    }

    public void initNormal(){ // gets  the robot ready to run
        lights.lightBrightness('b',0.75).schedule(); //sets both lights to 75%
        lights.setLight('b', NextRGBIndicator.Color.WHITE).schedule();
        shooter.closeGate().schedule();
        shooter.configureEncoder(hardwareMap);
        turret.configureEncoder();
        configureMotorDirections();
        if (follower != null) {
            follower.stop();
        }
    }
    public void shutDown(){
        shooter.shooterOff().schedule();
        shooter.closeGate().schedule();
        hood.hoodDown();
        intake.intake(false).schedule();
        lights.setLight('b', NextRGBIndicator.Color.OFF).schedule();
    }

    public Command shootFull(){ // shoots and aims and does hood angle
        return sequential(
                turret.AIM(true),
                shootSimple(),
                turret.AIM(false)
        );
    }
    public Command shootSimple(){ //just shoots back, still does hood angle
        return sequential(
                hood.hoodUpDistance(),
                shooter.shooterOn(),
                waitMs(100),
                intake.outtake(true), //Makes sure there isn't any pressure on the gate
                shooter.openGate(),
                intake.intake(true),
                waitMs(MiscVar.shootTimeMs),
                shooter.closeGate(),
                hood.hoodDown()
        );

    }

    public void fullAiming(Pose pose){
        if (pose == null) return;
        if (pose.y() > 96) {
            turret.setGoal(MiscVar.leftGoal); //Aims at driver's left goal if the robot is within the back 2 tiles
            lights.setLight('r', NextRGBIndicator.Color.VIOLET).schedule(); // The color can be changed, just picked random ones
        } else if (pose.y() < 48) {
            turret.setGoal(MiscVar.rightGoal); //Aims at driver's right goal if the robot is within the front 2 tiles
            lights.setLight('r', NextRGBIndicator.Color.WHITE).schedule(); // The color can be changed, just picked random ones
        } else {
            turret.setGoal(MiscVar.midFieldPose); //Aims at center of the field if in the center 2 tiles, makes the transition smoother between sides
            lights.setLight('r', NextRGBIndicator.Color.RED).schedule(); // The color can be changed, just picked random ones
        }

        updateAim(pose);
    }

    /** Calculate adjusted target geometry, SOTM lead, and the final turret motor angle. */
    public void updateAim(Pose pose) {
        if (pose == null || !turret.isAutoAimEnabled()) return;
        Pose goal = turret.activeGoal;
        if (goal == null) return;

        // Rotate the turret's robot-relative offset into field coordinates.
        double turretX = pose.x() + MiscVar.turretOffsetX * Math.cos(pose.heading())
                - MiscVar.turretOffsetY * Math.sin(pose.heading());
        double turretY = pose.y() + MiscVar.turretOffsetX * Math.sin(pose.heading())
                + MiscVar.turretOffsetY * Math.cos(pose.heading());

        double targetX = goal.x();
        double targetY = goal.y();
        double angularOffset = 0;
        if (turret.SOTM) {
            Follower f = getFollower();
            Velocity velocity = f == null ? null : f.velocity();
            if (velocity != null) {
                double unadjustedDistance = Math.hypot(targetX - turretX, targetY - turretY);
                targetX += velocity.vx * unadjustedDistance * MiscVar.constantSOTM;
                targetY += velocity.vy * unadjustedDistance * MiscVar.constantSOTM;
                angularOffset = velocity.omega * MiscVar.constantRotComp;
            }
        }

        calculateTargetDistance(targetX, targetY, turretX, turretY);
        double goalFieldDeg = Math.toDegrees(Math.atan2(targetY - turretY, targetX - turretX));
        double headingDeg = Math.toDegrees(pose.heading());
        double desiredAngle = MathFunctions.normalizeAngle(
                goalFieldDeg - headingDeg + MiscVar.turretMountOffsetDeg + angularOffset
        );
        turretCanReachCalculatedAngle = desiredAngle >= MiscVar.TURRET_MIN_DEG
                && desiredAngle <= MiscVar.TURRET_MAX_DEG;
        updateTurretLimitLight('l');
        double motorAngle = MathFunctions.clamp(desiredAngle, MiscVar.TURRET_MIN_DEG, MiscVar.TURRET_MAX_DEG);
        turret.commandAngle(motorAngle);
    }

    /** Set light 'l' green when the calculated angle is within the turret's configured travel. */
    public void updateTurretLimitLight(char light) {
        lights.setLight(light, turretCanReachCalculatedAngle
                ? NextRGBIndicator.Color.GREEN
                : NextRGBIndicator.Color.RED).schedule();
    }

    /**
     * Update the distance to a target using its adjusted field x/y and the adjusted turret x/y.
     * All coordinates must use the same field units.
     */
    public void calculateTargetDistance(double targetX, double targetY, double turretX, double turretY) {
        targetDistance = Math.hypot(targetX - turretX, targetY - turretY);
    }

    /** Distance from the turret pivot to the target selected by {@link #fullAiming(Pose)}. */
    public static double getTargetDistance() {
        return targetDistance;
    }

    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain, shooter, turret, hood, lights, intake);  //add all mechanisms listed above here
    }
    public void standardTelemetry(){ //Calling this function adds this data to the telemetry and writes it to the driver station
        Telemetry.log("Flywheel Ready: ", shooter.shooterInRange());
        Follower f = getFollower();
        if (f != null && f.pose() != null) {
            Telemetry.log("Robot Position", String.valueOf(f.pose()));
        }
        Telemetry.log("Target Distance", targetDistance);
        Telemetry.log("Shooter motor RPM (encoder)", shooter.flywheelSpeed());
        Telemetry.log("Shooter configured RPM (Panels)", MiscVar.shootSpeedRPM);
        Telemetry.log("Shooter target RPM (motor shaft)", shooter.targetSpeedRpm());
        Telemetry.log("Shooter PIDF (P/I/D/V/A)", MiscVar.shootkP + "/" + MiscVar.shootkI + "/" + MiscVar.shootkD + "/" + MiscVar.shootkV + "/" + MiscVar.shootkA);
        Telemetry.log("Shooter motor max RPM (Robot Config)", shooter.motorMaxRpm());
        Telemetry.log("Shooter encoder ticks/rev (Robot Config)", shooter.motorTicksPerRev());
        Telemetry.log("Turret Angle", turret.turret.getEncoderPosition().getMagnitude());
        Telemetry.update();
    }
}
