package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;

public class MainAutoPositions {

    private final PoseFactory poseFactory = PoseFactory.degrees();


    public final Pose rightStart = poseFactory.of(38, 7, 180);
    private final Pose shootToCornerIntakeStart = poseFactory.of(38, 7, 180);
    private final Pose shootToCornerIntake = poseFactory.of(10, 7, 180);
    private final Pose cornerIntakeshoot1 = poseFactory.of(48, 113, 90);
    private final Pose cornerIntakeshoot1Control1 = poseFactory.of(57, 20, 0);
    private final Pose cornerIntakeshoot1Control2 = poseFactory.of(11, 95, 0);
    private final Pose cornerIntakeshoot1Segment1Heading = poseFactory.of(47, 118, 180);
    private final Pose cornerIntakeshoot1Segment2Start = poseFactory.of(47, 118, 180);
    private final Pose cornerIntakeshoot1Segment2End = poseFactory.of(47, 118, 90);
    private final Pose cornerIntakeshoot1Segment3Heading = poseFactory.of(47, 118, 90);
    private final Pose shoot1LeftFlower = poseFactory.of(48, 126, 90);
    private final Pose leftFlowerMiddleFlower = poseFactory.of(13, 48, 180);
    private final Pose leftFlowerMiddleFlowerControl1 = poseFactory.of(50, 100, 0);
    private final Pose leftFlowerMiddleFlowerControl2 = poseFactory.of(3, 115, 0);
    private final Pose leftFlowerMiddleFlowerControl3 = poseFactory.of(59, 44, 0);
    private final Pose leftFlowerMiddleFlowerSegment1Start = poseFactory.of(14, 47, 90);
    private final Pose leftFlowerMiddleFlowerSegment1End = poseFactory.of(14, 47, 90);
    private final Pose leftFlowerMiddleFlowerSegment2Start = poseFactory.of(14, 47, 90);
    private final Pose leftFlowerMiddleFlowerSegment2End = poseFactory.of(14, 47, 180);
    private final Pose leftFlowerMiddleFlowerSegment3Heading = poseFactory.of(14, 47, 180);
    private final Pose middleFlowerShoot2 = poseFactory.of(33, 123, 90);
    private final Pose middleFlowerShoot2Control1 = poseFactory.of(37, 45, 0);
    private final Pose toPark = poseFactory.of(10, 118, 90);

    public Path startToIntake(){
        return Paths.path(Paths.line(shootToCornerIntakeStart, shootToCornerIntake).linear(shootToCornerIntakeStart, shootToCornerIntake));
    }

    public Path intakeToFirstShoot() {
        return Paths.path(Paths.curve(shootToCornerIntake, cornerIntakeshoot1Control1, cornerIntakeshoot1Control2, cornerIntakeshoot1).heading(Interpolator.piecewise().until(0.3546, Interpolator.constant(cornerIntakeshoot1Segment1Heading)).until(0.7915, Interpolator.linear(cornerIntakeshoot1Segment2Start, cornerIntakeshoot1Segment2End)).until(1, Interpolator.constant(cornerIntakeshoot1Segment3Heading))));
    }

    public Path shoot1LeftFlower() {
        return Paths.line(cornerIntakeshoot1, shoot1LeftFlower).constant(shoot1LeftFlower);
    }

    public Path leftFlowerMiddleFlower() {
        return Paths.curve(shoot1LeftFlower, leftFlowerMiddleFlowerControl1, leftFlowerMiddleFlowerControl2, leftFlowerMiddleFlowerControl3, leftFlowerMiddleFlower).heading(Interpolator.piecewise().until(0.1647, Interpolator.linear(leftFlowerMiddleFlowerSegment1Start, leftFlowerMiddleFlowerSegment1End)).until(0.668, Interpolator.linear(leftFlowerMiddleFlowerSegment2Start, leftFlowerMiddleFlowerSegment2End)).until(1, Interpolator.constant(leftFlowerMiddleFlowerSegment3Heading)));
    }

    public Path middleFlowerShoot2() {
        return Paths.curve(leftFlowerMiddleFlower, middleFlowerShoot2Control1, middleFlowerShoot2).linear(leftFlowerMiddleFlower,middleFlowerShoot2);
    }

    public Path toPark() {
        return Paths.line(middleFlowerShoot2, toPark).heading(Interpolator.constant(toPark));
    }

}
