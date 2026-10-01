package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;

public class Positions {
    private static final PoseFactory poseFactory = PoseFactory.degrees();

    public static final Pose rightStart = poseFactory.of(7, 24, 270);
    private final Pose shootToCornerIntakeStart = poseFactory.of(7, 24, 270);
    private final Pose shootToCornerIntake = poseFactory.of(7, 10, 270);
    private final Pose cornerIntakeshoot1 = poseFactory.of(47, 118, 90);
    private final Pose cornerIntakeshoot1Control1 = poseFactory.of(57, 20, 0);
    private final Pose cornerIntakeshoot1Control2 = poseFactory.of(11, 95, 0);
    private final Pose cornerIntakeshoot1Segment1Heading = poseFactory.of(47, 118, 270);
    private final Pose cornerIntakeshoot1Segment2Start = poseFactory.of(47, 118, -90);
    private final Pose cornerIntakeshoot1Segment2End = poseFactory.of(47, 118, 90);
    private final Pose cornerIntakeshoot1Segment3Heading = poseFactory.of(47, 118, 90);
    private final Pose shoot1LeftFlower = poseFactory.of(47, 128, 90);
    private final Pose leftFlowerMiddleFlower = poseFactory.of(14, 47, 180);
    private final Pose leftFlowerMiddleFlowerControl1 = poseFactory.of(50, 100, 0);
    private final Pose leftFlowerMiddleFlowerControl2 = poseFactory.of(3, 115, 0);
    private final Pose leftFlowerMiddleFlowerControl3 = poseFactory.of(59, 44, 0);
    private final Pose leftFlowerMiddleFlowerSegment1Start = poseFactory.of(14, 47, 90);
    private final Pose leftFlowerMiddleFlowerSegment1End = poseFactory.of(14, 47, 90);
    private final Pose leftFlowerMiddleFlowerSegment2Start = poseFactory.of(14, 47, 90);
    private final Pose leftFlowerMiddleFlowerSegment2End = poseFactory.of(14, 47, 180);
    private final Pose leftFlowerMiddleFlowerSegment3Heading = poseFactory.of(14, 47, 180);
    private final Pose middleFlowerShoot2 = poseFactory.of(35, 12, 180);
    private final Pose middleFlowerShoot2Control1 = poseFactory.of(37, 47, 0);
    private final Pose toPark = poseFactory.of(11, 95, 90);
    private final Pose toParkSegment1Start = poseFactory.of(11, 95, 180);
    private final Pose toParkSegment1End = poseFactory.of(11, 95, 106.1276);
    private final Pose toParkSegment3Start = poseFactory.of(11, 95, 106.1276);
    private final Pose toParkSegment3End = poseFactory.of(11, 95, 90);

    public Path preloadShootToFirstShoot() {
        return Paths.path(Paths.line(shootToCornerIntakeStart, shootToCornerIntake).linear(shootToCornerIntakeStart, shootToCornerIntake), Paths.curve(shootToCornerIntake, cornerIntakeshoot1Control1, cornerIntakeshoot1Control2, cornerIntakeshoot1).heading(Interpolator.piecewise().until(0.3546, Interpolator.constant(cornerIntakeshoot1Segment1Heading)).until(0.7915, Interpolator.linear(cornerIntakeshoot1Segment2Start, cornerIntakeshoot1Segment2End)).until(1, Interpolator.constant(cornerIntakeshoot1Segment3Heading))));
    }

    public Path shoot1LeftFlower() {
        return Paths.line(cornerIntakeshoot1, shoot1LeftFlower).constant(shoot1LeftFlower);
    }

    public Path leftFlowerMiddleFlower() {
        return Paths.curve(shoot1LeftFlower, leftFlowerMiddleFlowerControl1, leftFlowerMiddleFlowerControl2, leftFlowerMiddleFlowerControl3, leftFlowerMiddleFlower).heading(Interpolator.piecewise().until(0.1647, Interpolator.linear(leftFlowerMiddleFlowerSegment1Start, leftFlowerMiddleFlowerSegment1End)).until(0.668, Interpolator.linear(leftFlowerMiddleFlowerSegment2Start, leftFlowerMiddleFlowerSegment2End)).until(1, Interpolator.constant(leftFlowerMiddleFlowerSegment3Heading)));
    }

    public Path middleFlowerShoot2() {
        return Paths.curve(leftFlowerMiddleFlower, middleFlowerShoot2Control1, middleFlowerShoot2).constant(middleFlowerShoot2);
    }

    public Path toPark() {
        return Paths.line(middleFlowerShoot2, toPark).heading(Interpolator.piecewise().until(0.1448, Interpolator.linear(toParkSegment1Start, toParkSegment1End)).until(0.9283, Interpolator.tangent).until(1, Interpolator.linear(toParkSegment3Start, toParkSegment3End)));
    }
}
