package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;

public class TestPositions {
    private static final PoseFactory poseFactory = PoseFactory.degrees();
    public static final Pose startPose = poseFactory.of(0,0,0);
    private final Pose lastPose = poseFactory.of(24,0,90);
    public Path testPathConstantGo() {
        return Paths.path(Paths.line(startPose, lastPose).constant(startPose));
    }
    public Path testPathConstantBack() {
        return Paths.path(Paths.line(lastPose, startPose).constant(startPose));
    }
    public Path testPathLinearGo() {
        return Paths.path(Paths.line(startPose, lastPose).linear(startPose, lastPose));
    }
    public Path testPathLinearBack() {
        return Paths.path(Paths.line(lastPose, startPose).linear(lastPose, startPose));
    }
    public Path testPathPiecewiseGo() {
        return Paths.path(Paths.line(startPose, lastPose).heading(Interpolator.piecewise().until(0.5,Interpolator.constant(startPose)).until(1,Interpolator.linear(startPose,lastPose))));
    }
    public Path testPathPiecewiseBack() {
        return Paths.path(Paths.line(lastPose, startPose).heading(Interpolator.piecewise().until(0.5,Interpolator.constant(lastPose)).until(1,Interpolator.linear(lastPose,startPose))));
    }
}
