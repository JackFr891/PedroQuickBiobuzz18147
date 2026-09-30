package org.firstinspires.ftc.teamcode.Robot.Mecanisms;

import org.firstinspires.ftc.teamcode.Robot.robotMap;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.sensors.NextPinpoint;
import dev.nextftc.robot.Mechanism;

public class Drivetrain implements Mechanism {
    public Drivetrain(){
//        frontLeft.setDirection(NextMotor.Direction.REVERSE);
//        backLeft.setDirection(NextMotor.Direction.REVERSE);
//        frontRight.setDirection(NextMotor.Direction.REVERSE);
//        backRight.setDirection(NextMotor.Direction.REVERSE);
    }
    public final NextMotor frontLeft = new NextMotor(robotMap.leftFrontMotor);
    public final NextMotor backLeft = new NextMotor(robotMap.leftRearMotor);
    public final NextMotor frontRight = new NextMotor(robotMap.rightFrontMotor);
    public final NextMotor backRight = new NextMotor(robotMap.rightRearMotor);
    public final NextPinpoint pinpoint = new NextPinpoint(robotMap.pinPoint);
}
