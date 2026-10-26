package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;

public class LeftSmallPositions {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    public final Pose start = poseFactory.of(33, 134, 90);
    private final Pose path1 = poseFactory.of(47, 117, 90);
    private final Pose path1Control1 = poseFactory.of(26, 108, 0);
    private final Pose path1Segment1Heading = poseFactory.of(47, 117, 0);
    private final Pose path1Segment2Target = poseFactory.of(47, 135, 0);
    private final Pose point2 = poseFactory.of(47, 126, 90);
    private final Pose point3 = poseFactory.of(16, 115, 0);
    private final Pose point3Control1 = poseFactory.of(46, 113, 0);
    private final Pose point3Segment1Heading = poseFactory.of(16, 115, 90);
    private final Pose point3Segment3Start = poseFactory.of(16, 115, -3.2287);
    private final Pose point3Segment3End = poseFactory.of(16, 115, 0);

    public Path path1() {
        return Paths.curve(start, path1Control1, path1).heading(Interpolator.piecewise().until(0.0424, Interpolator.constant(path1Segment1Heading)).until(1, Interpolator.facingPoint(path1Segment2Target)));
    }

    public Path path2() {
        return Paths.line(path1, point2).constant(point2);
    }

    public Path path3() {
        return Paths.curve(point2, point3Control1, point3).heading(Interpolator.piecewise().until(0.0465, Interpolator.constant(point3Segment1Heading)).until(0.9541, Interpolator.tangent.reverse()).until(1, Interpolator.linear(point3Segment3Start, point3Segment3End)));
    }
}
