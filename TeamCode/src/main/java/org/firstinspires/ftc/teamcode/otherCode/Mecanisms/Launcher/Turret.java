package org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;

import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MathFunctions;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;
import org.firstinspires.ftc.teamcode.otherCode.robotMap;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.Units;
import dev.nextftc.units.measuretypes.Angle;

public class Turret implements Mechanism {
    public Turret(Follower follower){
        this.f = follower;
    }

    private Follower f;
    public final NextMotor turret = new NextMotor(robotMap.turretMotor);

    //saved Positions
    public Pose frontGoalRed = new Pose(58,55);
    public Pose backGoalRed = new Pose(58,86);
    public Pose frontGoalBlue = new Pose(84,55);
    public Pose backGoalBlue = new Pose(84,86);
    public Pose activeGoal;
    public static double targetTurretAng;
    public static double TURRET_MIN_DEG = -135;
    public static double TURRET_MAX_DEG = 135;
    public boolean SOTM;
    public boolean rotComp;
    private Angle turretAngle;

    public void setGoal(Pose goal){
        activeGoal = goal;
    }
    public Command AIM(boolean Y){
        if(Y){
            turretAngle = Units.getDegrees(turretAngleToMotor(targetTurretAng));

        }else {
            turretAngle = Units.getDegrees(0);
        }
        return null;
    }
    public Command AIMcomp(Boolean sotm){
        return instant(()-> SOTM = sotm);
    }
    public void turretLoop() {
        double xOffset,yOffset,angOffset;
        Pose botPose = f.pose();
        Velocity wVel = f.velocity(); // gets the velocity in terms of field directions
        if (SOTM){

            double dDist = f.pose().distance(activeGoal); //Finds the distance to activeGoal


            xOffset = wVel.vx*dDist*MiscVar.constantSOTM; //Finds how much to offset goal pose in x and y to cancel out velocity
            yOffset = wVel.vy*dDist+MiscVar.constantSOTM;
            angOffset = wVel.omega*MiscVar.constantRotComp; //Finds how much to change angle by to cancel out velocity
        }else{
            xOffset = 0;
            yOffset = 0;
            angOffset = 0;
        }

        double dx = activeGoal.x() - botPose.x() + xOffset;
        double dy = activeGoal.y() - botPose.y() + yOffset;

        double goalFieldDeg = Math.toDegrees(Math.atan2(dy, dx));
        double headingDeg = Math.toDegrees(botPose.heading());

        double desired = MathFunctions.normalizeAngle(goalFieldDeg - headingDeg + angOffset);

        targetTurretAng = MathFunctions.clamp(desired, TURRET_MIN_DEG, TURRET_MAX_DEG);
    }
    private double turretAngleToMotor(double angle){
        return (angle*MiscVar.TURRET_RATIO); //
    }
    private void update(){
        turret.setPositionSetpoint(turretAngle);
    }
    @Override
    public void periodic(){
        turretLoop();
    }

}
