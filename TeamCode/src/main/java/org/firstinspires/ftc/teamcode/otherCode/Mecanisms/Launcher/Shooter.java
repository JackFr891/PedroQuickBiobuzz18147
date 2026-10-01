package org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.otherCode.robotMap;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.Units;

public class Shooter implements Mechanism {
    public Shooter(){

    }
    public final NextMotor shooter = new NextMotor(robotMap.shooterMotor);
    public final NextServo gate = new NextServo(robotMap.gateServo);
    //Make Shooter Functions below
    public static final double gateOpen = 1;
    public static final double gateClose = 0;


    public Command openGate(){
        return Commands.instant(()->gate.setPosition(gateOpen));
    }
    public Command closeGate(){
        return Commands.instant(()->gate.setPosition(gateClose));
    }




    public Command shooterOff(){
        return Commands.instant(()->shooter.setVelocitySetpoint(Units.getRotationsPerMinute(0)));
    }
    public Command shooterOn(){
        return Commands.instant(()->shooter.setVelocitySetpoint(MiscVar.shootSpeed));
    }
    public Command shooterFlowerSpeed(){
        return Commands.instant(()->shooter.setVelocitySetpoint(MiscVar.flowerShootSpeed));
    }
}
