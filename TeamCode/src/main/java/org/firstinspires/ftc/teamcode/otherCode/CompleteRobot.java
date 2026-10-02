package org.firstinspires.ftc.teamcode.otherCode;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher.Shooter;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher.Turret;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Lights;
import org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths.Positions;
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
    public final Turret turret = new Turret(follower);
    public final Lights lights = new Lights();




    public void startDrive(Gamepad gamepad){
        DriveCommands.mecanumDrive(
                drivetrain.frontLeft,
                drivetrain.frontRight,
                drivetrain.backLeft,
                drivetrain.backRight,
                gamepad
        ).schedule();
    }   // Creates the Drivetrain and links it to your controls

    public void homeRobot(){
        follower.setPose(Positions.gardenHoming);
    }


    public Follower getFollower() {
        if (follower == null) {
            follower = Constants.create(null); //make it not null later
        }

        return follower;
    } // Might not work until after pedro auto-tune
    public void updateFollower(){
        follower.update();
    }
    public void init(){ // gets  the robot ready to run
        lights.lightBrightness('b',0.75); //sets both lights to 75%
        lights.setLight('b', NextRGBIndicator.Color.WHITE);
        shooter.closeGate();
    }

    public void standardTelemetry(){ //Calling this function adds this data to the telemetry and writes it to the driver station
        Telemetry.log("Flywheel Ready: ",shooter.shooterInRange());
        Telemetry.log("Turret Ready: ", turret.turretAimed());
        Telemetry.log("Robot Position", String.valueOf(follower.pose().x()));
        Telemetry.update();
    }

    public void shutDown(){
        shooter.shooterOff();
        shooter.closeGate();
        lights.setLight('b', NextRGBIndicator.Color.OFF);
        //intake off
    }



    public Command flywheelToLight(char index){ //Green if flywheel is close to target, red if it is not
        if (shooter.shooterInRange()){
            return Commands.instant(()->lights.setLight(index, NextRGBIndicator.Color.GREEN));
        }else {
            return Commands.instant(()->lights.setLight(index, NextRGBIndicator.Color.RED));
        }
    }
    public Command turretToLight(char index){ //Green if turret is close to target, red if not
        if (turret.turretAimed()){
            return Commands.instant(()->lights.setLight(index, NextRGBIndicator.Color.GREEN));
        }else {
            return Commands.instant(()->lights.setLight(index, NextRGBIndicator.Color.RED));
        }
    }
    public Command fullShooterToLight(char index) { //green if ready to shoot, orange if its missing 1 thing, red if it is not ready at all
        if (turret.turretAimed() && shooter.shooterInRange()) { // both
            return Commands.instant(() -> lights.setLight(index, NextRGBIndicator.Color.GREEN));
        } else if (turret.turretAimed() || shooter.shooterInRange()) { // One but not the other
            return Commands.instant(() -> lights.setLight(index, NextRGBIndicator.Color.ORANGE));
        } else { //neither
            return Commands.instant(()->lights.setLight(index, NextRGBIndicator.Color.RED));
        }
    }

    public Command shoot(){ // shoots at full speed
        return sequential(
                null, //Replace with intake turn on
                shooter.shooterOn(),
                shooter.openGate(),
                waitMs(750),
                shooter.closeGate()

        );
    }
    public Command shootFlower(){ // tries to shoot into flower
        return sequential(
          turret.AIM(false),
          shooter.shooterFlowerSpeed(),
          waitMs(500),
          null, //Replace with intake on
          shooter.openGate(),
          waitMs(750),
          shooter.closeGate(),
          turret.AIM(true),
          shooter.shooterOn()
        );
    }

    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain,shooter,turret,lights);  //add all mechanisms listed above here
    }
}
