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
import dev.nextftc.units.measuretypes.AngularVelocity;

public class Shooter implements Mechanism {
    public Shooter(){
        shooter.getVelocityConstants()
                .withP(MiscVar.shootkP)
                .withI(MiscVar.shootkI)
                .withD(MiscVar.shootkD)
                .withV(MiscVar.shootkV)
                .withA(MiscVar.shootkA);

    }
    public final NextMotor shooter = new NextMotor(robotMap.shooterMotor);
    public final NextServo gate = new NextServo(robotMap.gateServo);
    //Make Shooter Functions below
    public static final double gateOpen = 1;
    public static final double gateClose = 0;
    private AngularVelocity targetSpeed = Units.getRotationsPerMinute(0);



    public Command openGate(){
        return Commands.instant(()->gate.setPosition(gateOpen));
    }
    public Command closeGate(){
        return Commands.instant(()->gate.setPosition(gateClose));
    }




    public Command shooterOff(){
        targetSpeed = Units.getRotationsPerMinute(0);
        return Commands.instant(()->shooter.setVelocitySetpoint(Units.getRotationsPerMinute(0)));
    }
    public Command shooterOn(){
        targetSpeed = MiscVar.shootSpeed;
        return Commands.instant(()->shooter.setVelocitySetpoint(MiscVar.shootSpeed));
    }
    public Command shooterFlowerSpeed(){
        targetSpeed = MiscVar.flowerShootSpeed;
        return Commands.instant(()->shooter.setVelocitySetpoint(MiscVar.flowerShootSpeed));
    }
    public boolean shooterInRange(){
        AngularVelocity speedCurrent = shooter.getEncoderVelocity();
        return (Math.abs(speedCurrent.getMagnitude() - targetSpeed.getMagnitude()) <= MiscVar.shootThreshhold.getMagnitude());
    }
}
