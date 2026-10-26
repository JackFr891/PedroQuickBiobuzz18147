package org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.math.Pose;

import dev.nextftc.units.Units;
import dev.nextftc.units.measuretypes.Angle;
import dev.nextftc.units.measuretypes.AngularVelocity;
@Configurable
public class MiscVar {
    //Holds all fixed values that we want to be able to change using panels outside of PedroPathing

    // Keep tunable RPM values primitive so Panels exposes them as editable numeric fields.
    public static volatile double shootSpeedRPM = 160;
    public static volatile double flowerShootSpeedRPM = 60;
    public static double shootTimeMs = 1000;
    public static double shootSpeedStep = 5;

    public static AngularVelocity shootThreshhold = Units.getRotationsPerMinute(5);
    public static Pose rightGoal = new Pose(58,59); //from Driver's view
    public static Pose leftGoal = new Pose(58,82); // ^^^^^
    public static Pose midFieldPose = new Pose(72,72);
    public static final Pose gardenHoming = new Pose(7,9,90); //Robot with the back in the corner facing along alliance wall
    public static final Pose leftCorner = new Pose(7,132,270);
    public static final Pose humanPlayerBox = new Pose(9,111,0);
    public static double TURRET_MIN_DEG = -150;
    public static double TURRET_MAX_DEG = 150;
    public static double constantSOTM = 0.5;
    public static double constantRotComp = 0.4;
    public static final double TURRET_ENCODER_TICKS_PER_MOTOR_REV = 28.0; // REV HD Hex encoder, at the motor
    public static double TURRET_ULTRAPLANETARY_RATIO = 68.0 / 13.0;
    public static double TURRET_EXTERNAL_GEAR_RATIO = 4.0;
    public static double turretOffsetX = -3.5;
    public static double turretOffsetY = 0;
    public static double turretMountOffsetDeg = -180.0; // turret's zero-angle axis faces the rear of the robot
    public static final double gateOpen = 0;
    public static final double gateClose = 1;
    public static volatile double shootkP = 0.005;
    public static volatile double shootkI = 0;
    public static volatile double shootkD = 0.00005;
    public static volatile double shootkV = 0.003875;
    public static volatile double shootkA = 0.005;
    public static double turretkP = 0.01;
    public static double turretkI = 0;
    public static double turretkD = 0.0005;
    public static double turretkS = 0.001;
    public static double intakeSpeed = 1;
    public static double outtakeSpeed = -.75;
    public static double flowerArmUp = 1;
    public static double flowerArmDown = 0;
    public static double hoodMin = 0.0;
    public static double hoodMax = 1.0;


}
