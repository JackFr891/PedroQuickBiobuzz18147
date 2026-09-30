package org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher;

import org.firstinspires.ftc.teamcode.otherCode.robotMap;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.Units;

public class Shooter implements Mechanism {
    public Shooter(){

    }
    public final NextMotor shooter = new NextMotor(robotMap.shooterMotor);
    //Make Shooter Functions below

    public void shooterOff(){
        shooter.setVelocitySetpoint(Units.getRotationsPerMinute(0));
    }
    public void shooterOn(){
        shooter.setVelocitySetpoint(MiscVar.shootSpeed);
    }
}
