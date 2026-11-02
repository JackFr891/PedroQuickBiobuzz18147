package org.firstinspires.ftc.teamcode.otherCode.opModes.Teleop;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

@NextTeleop(name ="Shooter Test", group = "Custom Tuning")
public class ShootTest extends NextOpMode {

    private final CompleteRobot bot;
    public ShootTest(CompleteRobot robot){
        super(robot);
        this.bot = robot;

        Scheduler.reset();

    }
    @Override
    public void disabledPeriodic(){

    }
    @Override
    public void start(){
        
        CommandGamepad gp = new CommandGamepad(gamepad1);
        gp.dpadLeft().onTrue(bot.shooter.adjustShooterSpeed('d'));
        gp.dpadRight().onTrue(bot.shooter.adjustShooterSpeed('u'));
        gp.dpadDown().onTrue(bot.shooter.adjustShooterSpeed('D'));
        gp.dpadUp().onTrue(bot.shooter.adjustShooterSpeed('U'));

        gp.x().onTrue(Commands.instant(() -> adjustHoodAngle(-0.05)));
        gp.b().onTrue(Commands.instant(() -> adjustHoodAngle(0.05)));
        gp.y().onTrue(Commands.instant(() -> adjustHoodAngle(0.2)));
        gp.a().onTrue(Commands.instant(() -> adjustHoodAngle(-0.2)));

        gp.rightTrigger().isOver(0.5)
                .onTrue(bot.intake.intake(true));
    }
    @Override
    public void periodic(){
        bot.hood.hoodUpDistance();

    }
    @Override
    public void end(){

    }

    private void adjustHoodAngle(double adjustment) {
        bot.hood.hoodAngle = Math.max(MiscVar.hoodMin,
                Math.min(MiscVar.hoodMax, bot.hood.hoodAngle + adjustment));
        bot.hood.hoodUpDistance().schedule();
    }
}
