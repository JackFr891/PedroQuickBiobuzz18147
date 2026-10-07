package org.firstinspires.ftc.teamcode.otherCode.Mecanisms;


import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.otherCode.robotMap;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.Units;

public class Intake implements Mechanism {
    public final NextMotor intake = new NextMotor(robotMap.intakeMotor);
    public Command intake(boolean on){
        if (on) {
            return Commands.instant(()->intake.setVelocitySetpoint(MiscVar.intakeSpeed));
        } else {
            return Commands.instant(()->intake.setVelocitySetpoint(Units.getRotationsPerMinute(0)));
        }
    }
    public Command outtake(boolean on){
        if (on) {
            return Commands.instant(()->intake.setVelocitySetpoint(MiscVar.outtakeSpeed));
        } else{
            return Commands.instant(()->intake.setVelocitySetpoint(Units.getRotationsPerMinute(0)));
        }
    }
}
