package org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.robotMap;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class AngleChange implements Mechanism {
    public final NextServo leftHood = new NextServo(robotMap.hoodAngleLeftServo);
    public final NextServo rightHood = new NextServo(robotMap.hoodAngleRightServo);
    public double hoodAngle = 0.0;
    public AngleChange(){
        rightHood.setDirection(NextMotor.Direction.REVERSE); // right side servo is the opposite way, so it needs to be reversed
    }
    @Override
    public void periodic(){
        rightHood.setPosition(leftHood.getPosition());
    } // sets the right side hood servo to the same as left side
    public Command hoodDown(){
        return Commands.instant(()->leftHood.setPosition(0));
    }
    public Command hoodUpDistance(){
        return Commands.instant(()->leftHood.setPosition(hoodAngle));
    }

    public void distanceToHood(){
        double d = CompleteRobot.getTargetDistance();
        double adjustedDistance = 0.0;// Find adjusted position
        hoodAngle = Math.max(MiscVar.hoodMin,Math.min(adjustedDistance,MiscVar.hoodMax));
    }

}
