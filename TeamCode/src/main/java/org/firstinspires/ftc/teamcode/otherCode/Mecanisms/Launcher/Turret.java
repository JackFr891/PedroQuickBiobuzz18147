package org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;

import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MathFunctions;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;
import org.firstinspires.ftc.teamcode.otherCode.robotMap;

import java.util.function.Supplier;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.Units;
import dev.nextftc.units.measuretypes.Angle;

public class Turret implements Mechanism {
    private final Supplier<Follower> followerSupplier;
    public final NextMotor turret = new NextMotor(robotMap.turretMotor);

    //saved Positions
    public Pose activeGoal = MiscVar.midFieldPose;
    public static double targetTurretAng;
    public boolean SOTM;
    private Angle turretAngle = Units.getDegrees(0);
    private double desiredAngle;
    private boolean autoAimEnabled = true;

    public Turret(Supplier<Follower> followerSupplier) {
        this.followerSupplier = followerSupplier;
        turret.getPositionConstants()
                .withP(MiscVar.turretkP)
                .withI(MiscVar.turretkI)
                .withD(MiscVar.turretkD)
                .withS(MiscVar.turretkS);
        turret.setDirection(NextMotor.Direction.REVERSE);
        turret.setThrottle(0);

    }

    public Turret(Follower follower) {
        this(() -> follower);
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
                turret.setThrottle(0);
            });
        }
    }

    public Command AIMcomp(Boolean sotm) {
        return Commands.instant(() -> SOTM = sotm);
    }

    public void turretLoop() {
        if (!autoAimEnabled) return;
        if (followerSupplier == null) return;
        Follower f = followerSupplier.get();
        if (f == null) return;

        Pose botPose = f.pose();
        if (botPose == null) return;

        Velocity wVel = f.velocity();
        if (wVel == null) {
            wVel = new Velocity(0, 0, 0);
        }

        if (activeGoal == null) {
            activeGoal = MiscVar.midFieldPose;
        }

        //gets the position of the turret
        double turretX = botPose.x() + MiscVar.turretOffsetX * Math.cos(botPose.heading()) - MiscVar.turretOffsetY * Math.sin(botPose.heading());
        double turretY = botPose.y() + MiscVar.turretOffsetX * Math.sin(botPose.heading()) + MiscVar.turretOffsetY * Math.cos(botPose.heading());

        double xOffset, yOffset, angOffset;
        if (SOTM) {
            double dDist = Math.hypot(activeGoal.x() - turretX, activeGoal.y() - turretY); // Finds the distance from turret to activeGoal

            xOffset = wVel.vx * dDist * MiscVar.constantSOTM; // Finds how much to offset goal pose in x and y to cancel out velocity
            yOffset = wVel.vy * dDist * MiscVar.constantSOTM;
            angOffset = wVel.omega * MiscVar.constantRotComp; // Finds how much to change angle by to cancel out velocity
        } else {
            xOffset = 0;
            yOffset = 0;
            angOffset = 0;
        }

        //distance from goal to turret plus SOTM correction in each axis
        double dx = activeGoal.x() - turretX + xOffset;
        double dy = activeGoal.y() - turretY + yOffset;

        double goalFieldDeg = Math.toDegrees(Math.atan2(dy, dx));
        double headingDeg = Math.toDegrees(botPose.heading());

        desiredAngle = MathFunctions.normalizeAngle(
                goalFieldDeg - headingDeg + MiscVar.turretMountOffsetDeg + angOffset
        );

        targetTurretAng = MathFunctions.clamp(desiredAngle, MiscVar.TURRET_MIN_DEG, MiscVar.TURRET_MAX_DEG);
        turretAngle = Units.getDegrees(targetTurretAng);
        turret.setPositionSetpoint(turretAngle);
    }

    public boolean turretAimed() {
        Angle currentAngle = turret.getEncoderPosition();
        if (currentAngle == null) return false;
        return Math.abs(currentAngle.getMagnitude() - desiredAngle) <= MiscVar.allowedAngle.getMagnitude();
    }

    @Override
    public void periodic() {
        turretLoop();
    }
}
