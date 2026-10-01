package org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy;

import dev.nextftc.units.Units;
import dev.nextftc.units.measuretypes.Angle;
import dev.nextftc.units.measuretypes.AngularVelocity;

public class MiscVar {
    public static AngularVelocity shootSpeed = Units.getRotationsPerMinute(2000);
    public static AngularVelocity flowerShootSpeed = Units.getRotationsPerMinute(100);
    public static AngularVelocity shootThreshhold = Units.getRotationsPerMinute(100);
    public static Angle allowedAngle = Units.getDegrees(3);
    public static double constantSOTM = 0.5;
    public static double constantRotComp = 0.4;
    public static double TURRET_RATIO = 9;
    public static double shootkP = 0.01;
    public static double shootkI = 0;
    public static double shootkD = 0.005;
    public static double shootkV = 0.01;
    public static double shootkA = 0.005;
}
