package org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;
import org.firstinspires.ftc.teamcode.otherCode.robotMap;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.Units;
import dev.nextftc.units.measuretypes.Angle;

public class Turret implements Mechanism {
    public final NextMotor turret = new NextMotor(robotMap.turretMotor);

    //saved Positions
    public Pose activeGoal = MiscVar.midFieldPose;
    public static double targetTurretAng;
    public boolean SOTM;
    private Angle turretAngle = Units.getDegrees(0);
    private double desiredAngle;
    private boolean autoAimEnabled = true;

    public Turret() {
        turret.getPositionConstants()
                .withP(MiscVar.turretkP)
                .withI(MiscVar.turretkI)
                .withD(MiscVar.turretkD)
                .withS(MiscVar.turretkS);
        turret.setDirection(NextMotor.Direction.REVERSE);
        turret.setThrottle(0);

    }

    public void setGoal(Pose goal) {
        if (goal != null) {
            activeGoal = goal;
        }
    }

    /** Convert counts at the bare motor encoder into degrees at the turret output. */
    public void configureEncoder() {
        double totalReduction = MiscVar.TURRET_ULTRAPLANETARY_RATIO * MiscVar.TURRET_EXTERNAL_GEAR_RATIO;
        if (MiscVar.TURRET_ENCODER_TICKS_PER_MOTOR_REV > 0 && totalReduction > 0) {
            turret.setAnglePerCount(Units.getDegrees(
                    360.0 / (MiscVar.TURRET_ENCODER_TICKS_PER_MOTOR_REV * totalReduction)
            ));
        }
    }

    public Command AIM(boolean Y) {
        if (Y) {
            return Commands.instant(() -> {
                autoAimEnabled = true;
            });
        } else {
            return Commands.instant(() -> {
                autoAimEnabled = false;
                desiredAngle = 0;
                targetTurretAng = 0;
                turretAngle = Units.getDegrees(0);
                turret.setPositionSetpoint(turretAngle);
            });
        }
    }

    public Command AIMcomp(Boolean sotm) {
        return Commands.instant(() -> SOTM = sotm);
    }

    /** Apply the already-computed turret angle as the motor position setpoint. */
    public void commandAngle(double angleDegrees) {
        desiredAngle = angleDegrees;
        targetTurretAng = angleDegrees;
        turretAngle = Units.getDegrees(angleDegrees);
        turret.setPositionSetpoint(turretAngle);
    }

    public boolean isAutoAimEnabled() {
        return autoAimEnabled;
    }

    @Override
    public void periodic() {
        // CompleteRobot calculates the target angle and calls commandAngle().
    }
}
