package org.firstinspires.ftc.teamcode.otherCode;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher.Shooter;
import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.drive.DriveCommands;

public class CompleteRobot implements NextRobot {
    private Follower follower;

    //Add in all mecanisms that are in robot
    public final Drivetrain drivetrain = new Drivetrain(); //implement other mecanisms the same way
    public static final Shooter shooter = new Shooter();
    public final Turret turret = new Turret(follower);




    public void startDrive(Gamepad gamepad){
        DriveCommands.mecanumDrive(
                drivetrain.frontLeft,
                drivetrain.frontRight,
                drivetrain.backLeft,
                drivetrain.backRight,
                gamepad
        ).schedule();
    }   // Creates the Drivetrain and links it to your controls


    public Follower getFollower() {
        if (follower == null) {
            follower = Constants.create(null); //make it not null later
        }

        return follower;
    } // Might not work until after pedro auto-tune
    public void updateFollower(){
        follower.update();
    }
    public void init(){

    }
    public Command shoot(){
        return sequential(
                //make sure intake is on
                shooter.openGate(),
                waitMs(750),
                shooter.closeGate()

        );
    }
    public Command shootFlower(){
        return sequential(
          turret.AIM(false),
          shooter.shooterFlowerSpeed(),
          waitMs(500),
          shoot(),
          turret.AIM(true),
          shooter.shooterOn()
        );
    }

    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain,shooter,turret);  //add all mecanisms listed above here
    }
}
