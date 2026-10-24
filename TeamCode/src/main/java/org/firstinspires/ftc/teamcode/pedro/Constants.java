package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("LF");
        c.frontRightName.set("RF");
        c.backLeftName.set("LR");
        c.backRightName.set("RR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(0.9063343738946389);
        c.yPodOffset.set(0.25914586435152787);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.4724332007687478);
                Controller secondaryTranslationalForward = Controller.proportional(0.17455144403643472);
                Controller primaryTranslationalLateral = Controller.proportional(0.7822627660467547);
                Controller secondaryTranslationalLateral = Controller.proportional(0.2890251896082012);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.014346458560576338));
                c.brake.set(Controller.proportionalFeedforward(0.012194489776489887));

                c.headingFeedback.set(Controller.proportional(7.938550810494216));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06987686060662297, 0.005115133475373213));

                c.linearBrakeCoefficients.set(Matrix.diag(0.09778997535102825, 0.09311944084531038));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.002037818301206732, 0.0012886258991001752));

                c.maxAchievableForwardVelocity.set(70.24875094573608);
                c.maxAchievableStrafeVelocity.set(54.37973803737809);
                c.naturalForwardDeceleration.set(42.42881965750095);
                c.naturalStrafeDeceleration.set(83.49287346617183);
            }
    );
        public static Follower create(HardwareMap h) {
            return new Follower(
                    new PinpointLocalizer(h,localizerConfig),
                    new Mecanum(h, drivetrainConfig),
                    new Foresight(foresightConfig));

        }
}