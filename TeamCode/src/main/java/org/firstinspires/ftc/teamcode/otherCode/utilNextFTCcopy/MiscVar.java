package org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.math.Pose;

import dev.nextftc.units.Units;
import dev.nextftc.units.measuretypes.Angle;
import dev.nextftc.units.measuretypes.AngularVelocity;
@Configurable
public class MiscVar {
    //Holds all fixed values that we want to be able to change using panels outside of PedroPathing

    public static AngularVelocity shootSpeed = Units.getRotationsPerMinute(2000);
    public static AngularVelocity flowerShootSpeed = Units.getRotationsPerMinute(100);
    public static AngularVelocity shootThreshhold = Units.getRotationsPerMinute(100);
    public static Angle allowedAngle = Units.getDegrees(3);
    public static Pose rightGoal = new Pose(58,55); //from Driver's view
    public static Pose leftGoal = new Pose(58,86); // ^^^^^
    public static Pose midFieldPose = new Pose(72,72);
    public static double TURRET_MIN_DEG = -135;
    public static double TURRET_MAX_DEG = 135;
    public static double constantSOTM = 0.5;
    public static double constantRotComp = 0.4;
    public static double TURRET_RATIO = 68*4/13; //68/13 is motor gearbox, 4/1 is the actual gears
    public static double turretOffsetX = -3.5;
    public static double turretOffsetY = 0;
    public static final double gateOpen = 1;
    public static final double gateClose = 0;
    public static double shootkP = 0.01;
    public static double shootkI = 0;
    public static double shootkD = 0.005;
    public static double shootkV = 0.01;
    public static double shootkA = 0.005;
    public static double turretkP = 0.01;
    public static double turretkI = 0;
    public static double turretkD = 0.005;
    public static double turretkS = 0.01;

}
