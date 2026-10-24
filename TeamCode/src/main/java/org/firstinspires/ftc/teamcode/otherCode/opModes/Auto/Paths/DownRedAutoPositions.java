package org.firstinspires.ftc.teamcode.otherCode.opModes.Auto.Paths;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;

public class DownRedAutoPositions {

    private static final PoseFactory poseFactory = PoseFactory.degrees();

    public static final Pose start = poseFactory.of(7.5257, 132.3594, 270);
    private final Pose startToPreFlower = poseFactory.of(47, 117.2574, -270);
    private final Pose startToPreFlowerControl1 = poseFactory.of(19.4248, 107.7109, 0);
    private final Pose startToPreFlowerSegment1Start = poseFactory.of(47, 117.2574, 270);
    private final Pose startToPreFlowerSegment1End = poseFactory.of(47, 117.2574, 270);
    private final Pose startToPreFlowerSegment2Start = poseFactory.of(47, 117.2574, -90);
    private final Pose startToPreFlowerSegment2End = poseFactory.of(47, 117.2574, 90);
    private final Pose preFlowerToFlower = poseFactory.of(47.1109, 127.7446, 90);
    private final Pose flowerToStart = poseFactory.of(7.5257, 132.3594, 270);
    private final Pose flowerToStartControl1 = poseFactory.of(41.0376, 97.8802, 0);
    private final Pose flowerToStartSegment1Start = poseFactory.of(7.5257, 132.3594, 90);
    private final Pose flowerToStartSegment1End = poseFactory.of(7.5257, 132.3594, 90);
    private final Pose flowerToStartSegment2Start = poseFactory.of(7.5257, 132.3594, 90);
    private final Pose flowerToStartSegment2End = poseFactory.of(7.5257, 132.3594, 270);
    private final Pose flowerToStartSegment3Start = poseFactory.of(7.5257, 132.3594, 270);
    private final Pose flowerToStartSegment3End = poseFactory.of(7.5257, 132.3594, 270);
    private final Pose startToPark = poseFactory.of(13.6465, 120.5901, 270);

    public Path startToPreFlower() {
        return Paths.curve(start, startToPreFlowerControl1, startToPreFlower).heading(Interpolator.piecewise().until(0.1549, Interpolator.linear(startToPreFlowerSegment1Start, startToPreFlowerSegment1End)).until(1, Interpolator.linear(startToPreFlowerSegment2Start, startToPreFlowerSegment2End)));
    }

    public Path preFlowerToFlower() {
        return Paths.line(startToPreFlower, preFlowerToFlower).constant(preFlowerToFlower);
    }

    public Path flowerToStart() {
        return Paths.curve(preFlowerToFlower, flowerToStartControl1, flowerToStart).heading(Interpolator.piecewise().until(0.15, Interpolator.linear(flowerToStartSegment1Start, flowerToStartSegment1End)).until(0.85, Interpolator.linear(flowerToStartSegment2Start, flowerToStartSegment2End)).until(1, Interpolator.linear(flowerToStartSegment3Start, flowerToStartSegment3End)));
    }

    public Path startToPark() {
        return Paths.line(flowerToStart, startToPark).constant(startToPark);
    }
}