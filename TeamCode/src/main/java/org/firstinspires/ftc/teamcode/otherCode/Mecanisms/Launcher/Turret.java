package org.firstinspires.ftc.teamcode.otherCode.Mecanisms.Launcher;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;

import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MathFunctions;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;
import org.firstinspires.ftc.teamcode.otherCode.robotMap;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.Units;
import dev.nextftc.units.measuretypes.Angle;

public class Turret implements Mechanism {
    public Turret(Follower follower){
        this.f = follower;
        turret.getPositionConstants()
                .withP(MiscVar.turretkP)
                .withI(MiscVar.turretkI)
                .withD(MiscVar.turretkD)
                .withS(MiscVar.turretkS);
    }

    private Follower f;
    public final NextMotor turret = new NextMotor(robotMap.turretMotor);

    //saved Positions
    public Pose activeGoal;
    public static double targetTurretAng;
    public boolean SOTM;
    public boolean rotComp;
    private Angle turretAngle;
    private double desiredAngle;

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

        desiredAngle = MathFunctions.normalizeAngle(goalFieldDeg - headingDeg + angOffset);

        targetTurretAng = MathFunctions.clamp(desiredAngle, MiscVar.TURRET_MIN_DEG, MiscVar.TURRET_MAX_DEG);
    }
    private double turretAngleToMotor(double angle){
        return (angle*MiscVar.TURRET_RATIO); //
    }
    public boolean turretAimed(){
        Angle currentAngle = turret.getEncoderPosition();
        return Math.abs(currentAngle.getMagnitude()-desiredAngle)>=MiscVar.allowedAngle.getMagnitude();

    }
    private void update(){
        turret.setPositionSetpoint(turretAngle);
    }
    @Override
    public void periodic(){
        turretLoop();
    }

}
