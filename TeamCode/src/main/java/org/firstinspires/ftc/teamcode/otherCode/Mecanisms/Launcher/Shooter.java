package org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.otherCode.robotMap;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.Units;
import dev.nextftc.units.measuretypes.AngularVelocity;

public class Shooter implements Mechanism {
    private enum SpeedMode { OFF, NORMAL, FLOWER }

    public final NextMotor shooter = new NextMotor(robotMap.shooterMotor);
    public final NextServo gate = new NextServo(robotMap.gateServo);
    //Make Shooter Functions below
    private AngularVelocity targetSpeed = Units.getRotationsPerMinute(0);
    private double motorTicksPerRev = Double.NaN;
    private double motorMaxRpm = Double.NaN;
    private double lastShootKp = Double.NaN;
    private double lastShootKi = Double.NaN;
    private double lastShootKd = Double.NaN;
    private double lastShootKv = Double.NaN;
    private double lastShootKa = Double.NaN;
    private double lastShootSpeedRpm = Double.NaN;
    private double lastFlowerSpeedRpm = Double.NaN;
    private SpeedMode speedMode = SpeedMode.OFF;

    public Shooter(){
        shooter.setDirection(NextMotor.Direction.REVERSE);

    }

    /** Configure encoder units from the motor type selected in the Robot Configuration. */
    public void configureEncoder(HardwareMap hardwareMap) {
        DcMotorEx configuredMotor = hardwareMap.get(DcMotorEx.class, robotMap.shooterMotor);
        motorTicksPerRev = configuredMotor.getMotorType().getTicksPerRev();
        motorMaxRpm = configuredMotor.getMotorType().getMaxRPM();
        if (motorTicksPerRev > 0) {
            shooter.setAnglePerCount(Units.getDegrees(360.0 / motorTicksPerRev));
        }
    }



    public Command openGate(){
        return Commands.instant(()->gate.setPosition(MiscVar.gateOpen));
    }
    public Command closeGate(){
        return Commands.instant(()->gate.setPosition(MiscVar.gateClose));
    }




    public Command shooterOff(){
        return Commands.instant(() -> {
            speedMode = SpeedMode.OFF;
            setSpeed(Units.getRotationsPerMinute(0));
        });
    }
    public Command shooterOn(){
        return Commands.instant(this::startFlywheel);
    }
    public Command shooterFlowerSpeed(){
        return Commands.instant(this::startFlowerSpeed);
    }

    /** Apply the normal flywheel setpoint immediately, without waiting for command scheduling. */
    public void startFlywheel() {
        speedMode = SpeedMode.NORMAL;
        setSpeed(Units.getRotationsPerMinute(MiscVar.shootSpeedRPM));
    }

    private void startFlowerSpeed() {
        speedMode = SpeedMode.FLOWER;
        setSpeed(Units.getRotationsPerMinute(MiscVar.flowerShootSpeedRPM));
    }

    private void setSpeed(AngularVelocity speed) {
        targetSpeed = speed;
        shooter.setVelocitySetpoint(speed);
    }

    /** Reapply live Panels values while the selected flywheel preset is active. */
    @Override
    public void periodic() {
        refreshPanelTuning();
    }

    /** Explicit entry point for OpModes to apply the current Panels values every loop. */
    public void refreshPanelTuning() {
        if (Double.compare(lastShootKp, MiscVar.shootkP) != 0
                || Double.compare(lastShootKi, MiscVar.shootkI) != 0
                || Double.compare(lastShootKd, MiscVar.shootkD) != 0
                || Double.compare(lastShootKv, MiscVar.shootkV) != 0
                || Double.compare(lastShootKa, MiscVar.shootkA) != 0) {
            shooter.getVelocityConstants()
                    .withP(MiscVar.shootkP)
                    .withI(MiscVar.shootkI)
                    .withD(MiscVar.shootkD)
                    .withV(MiscVar.shootkV)
                    .withA(MiscVar.shootkA);
            lastShootKp = MiscVar.shootkP;
            lastShootKi = MiscVar.shootkI;
            lastShootKd = MiscVar.shootkD;
            lastShootKv = MiscVar.shootkV;
            lastShootKa = MiscVar.shootkA;
        }

        if (Double.compare(lastShootSpeedRpm, MiscVar.shootSpeedRPM) != 0) {
            lastShootSpeedRpm = MiscVar.shootSpeedRPM;
            if (speedMode == SpeedMode.NORMAL) {
                setSpeed(Units.getRotationsPerMinute(MiscVar.shootSpeedRPM));
            }
        }
        if (Double.compare(lastFlowerSpeedRpm, MiscVar.flowerShootSpeedRPM) != 0) {
            lastFlowerSpeedRpm = MiscVar.flowerShootSpeedRPM;
            if (speedMode == SpeedMode.FLOWER) {
                setSpeed(Units.getRotationsPerMinute(MiscVar.flowerShootSpeedRPM));
            }
        }
    }
    public boolean shooterInRange(){
        double speedCurrentRpm = shooter.getEncoderVelocity().into(Units.RotationsPerMinute);
        double targetSpeedRpm = targetSpeed.into(Units.RotationsPerMinute);
        return Math.abs(speedCurrentRpm - targetSpeedRpm) <= MiscVar.shootThreshhold.into(Units.RotationsPerMinute);
    }
    public double flywheelSpeed(){
        return shooter.getEncoderVelocity().into(Units.RotationsPerMinute);
    }

    /** Target and encoder speed are motor-shaft RPM based on the Robot Configuration motor type. */
    public double targetSpeedRpm() {
        return targetSpeed.into(Units.RotationsPerMinute);
    }

    public double motorTicksPerRev() {
        return motorTicksPerRev;
    }

    public double motorMaxRpm() {
        return motorMaxRpm;
    }
}
