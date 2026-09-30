package org.firstinspires.ftc.teamcode.otherCode;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Drivetrain;
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



    //Intake code from NextFTC documentation
//    private final Intake intake = new Intake();
//    public Intake getIntake() {
//        return intake;
//    }



    public void startDrive(Gamepad gamepad){
        DriveCommands.mecanumDrive(
                drivetrain.frontLeft,
                drivetrain.frontRight,
                drivetrain.backLeft,
                drivetrain.backRight,
                gamepad
        ).schedule();
    }   // Creates the Drivetrain and links it to your controls

//    public Follower getFollower() {
//        if (follower == null) {
//            follower = Constants.create(RobotController.hardwareMap());
//        }
//
//        return follower;
//    } // Might not work until after pedro auto-tune

    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain);  //add all mecanisms listed above here
    }
}
