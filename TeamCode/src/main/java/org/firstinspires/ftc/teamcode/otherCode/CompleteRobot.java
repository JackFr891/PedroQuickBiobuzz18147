package org.firstinspires.ftc.teamcode.otherCode;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Intake;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher.Shooter;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher.Turret;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Lights;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.FullAutoPositions;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.actuators.NextRGBIndicator;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.drive.DriveCommands;

public class CompleteRobot implements NextRobot {
    private Follower follower;

    //Add in all mechanisms that are in robot
    public final Drivetrain drivetrain = new Drivetrain(); //implement other mechanisms the same way
    public static final Shooter shooter = new Shooter();
    public final Turret turret = new Turret(this::getFollower);
    public final Lights lights = new Lights();
    public final Intake intake = new Intake();

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
                f.setPose(FullAutoPositions.gardenHoming);
            }
        });
    }

    public Follower getFollower(HardwareMap h) {
        if (follower == null && h != null) {
            follower = Constants.create(h);
            follower.setPose(FullAutoPositions.gardenHoming);
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

    public void init(){ // gets  the robot ready to run
        lights.lightBrightness('b',0.75).schedule(); //sets both lights to 75%
        lights.setLight('b', NextRGBIndicator.Color.WHITE).schedule();
        shooter.closeGate().schedule();
    }

    public void standardTelemetry(){ //Calling this function adds this data to the telemetry and writes it to the driver station
        Telemetry.log("Flywheel Ready: ", shooter.shooterInRange());
        Telemetry.log("Turret Ready: ", turret.turretAimed());
        Follower f = getFollower();
        if (f != null && f.pose() != null) {
            Telemetry.log("Robot Position", String.valueOf(f.pose()));
        }
        Telemetry.log("Shooter motor RPM (encoder)", shooter.flywheelSpeed());
        Telemetry.log("Shooter configured RPM (Panels)", MiscVar.shootSpeedRPM);
        Telemetry.log("Shooter target RPM (motor shaft)", shooter.targetSpeedRpm());
        Telemetry.log("Shooter PIDF (P/I/D/V/A)", MiscVar.shootkP + "/" + MiscVar.shootkI + "/" + MiscVar.shootkD + "/" + MiscVar.shootkV + "/" + MiscVar.shootkA);
        Telemetry.log("Shooter motor max RPM (Robot Config)", shooter.motorMaxRpm());
        Telemetry.log("Shooter encoder ticks/rev (Robot Config)", shooter.motorTicksPerRev());
        Telemetry.log("Turret Angle", turret.turret.getEncoderPosition().getMagnitude());
        Telemetry.update();
    }

    public void shutDown(){
        shooter.shooterOff().schedule();
        shooter.closeGate().schedule();
        intake.intake(false).schedule();
        lights.setLight('b', NextRGBIndicator.Color.OFF).schedule();
    }

    public Command flywheelToLight(char index){ //Green if flywheel is close to target, red if it is not
        if (shooter.shooterInRange()){
            return lights.setLight(index, NextRGBIndicator.Color.GREEN);
        } else {
            return lights.setLight(index, NextRGBIndicator.Color.RED);
        }
    }

    public Command turretToLight(char index){ //Green if turret is close to target, red if not
        if (turret.turretAimed()){
            return lights.setLight(index, NextRGBIndicator.Color.GREEN);
        } else {
            return lights.setLight(index, NextRGBIndicator.Color.RED);
        }
    }

    public Command fullShooterToLight(char index) { //green if ready to shoot, orange if its missing 1 thing, red if it is not ready at all
        if (turret.turretAimed() && shooter.shooterInRange()) { // both
            return lights.setLight(index, NextRGBIndicator.Color.GREEN);
        } else if (turret.turretAimed() || shooter.shooterInRange()) { // One but not the other
            return lights.setLight(index, NextRGBIndicator.Color.ORANGE);
        } else { //neither
            return lights.setLight(index, NextRGBIndicator.Color.RED);
        }
    }

    public Command shoot(){ // shoots at full speed
        return sequential(
                intake.intake(true),
                shooter.shooterOn(),
                shooter.openGate(),
                waitMs(1500),
                shooter.closeGate()
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
    }

    public Command shootFlower(){ // tries to shoot into flower
        return sequential(
          turret.AIM(false),
          shooter.shooterFlowerSpeed(),
          waitMs(500),
          intake.intake(true),
          shooter.openGate(),
          waitMs(1500),
          shooter.closeGate(),
          turret.AIM(true),
          shooter.shooterOn()
        );
    }

    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain, shooter, turret, lights, intake);  //add all mechanisms listed above here
    }
}
