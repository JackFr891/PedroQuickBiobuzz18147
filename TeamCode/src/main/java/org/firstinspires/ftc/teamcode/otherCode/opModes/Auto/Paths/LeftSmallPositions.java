package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;

public class LeftSmallPositions {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public final Pose start = poseFactory.of(33, 134, 90);
    private final Pose cornerIntakeshoot1 = poseFactory.of(48, 113, 90);
    private final Pose shoot1LeftFlower = poseFactory.of(48, 126, 90);
    private final Pose point3 = poseFactory.of(16, 115, 0);
    private final Pose point3Control1 = poseFactory.of(46, 113, 0);
    private final Pose point3Segment1Heading = poseFactory.of(16, 115, 90);
    private final Pose point3Segment3Start = poseFactory.of(16, 115, -3.2287);
    private final Pose point3Segment3End = poseFactory.of(16, 115, 0);

    public Path leftStartToFirstShoot() {
        return Paths.path(Paths.line(start, cornerIntakeshoot1).linear(start, cornerIntakeshoot1));
    }
    public Path shoot1LeftFlower() {
        return Paths.line(cornerIntakeshoot1, shoot1LeftFlower).constant(shoot1LeftFlower);
    }
    public Path leftFlowerToPark() {
        return Paths.curve(shoot1LeftFlower, point3Control1, point3).heading(Interpolator.piecewise().until(0.0465, Interpolator.constant(point3Segment1Heading)).until(0.9541, Interpolator.tangent.reverse()).until(1, Interpolator.linear(point3Segment3Start, point3Segment3End)));
    }
}
