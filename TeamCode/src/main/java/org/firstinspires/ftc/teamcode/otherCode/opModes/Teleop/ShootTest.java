package org.firstinspires.ftc.teamcode.otherCode.opModes.Teleop;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.otherCode.CompleteRobot;
import org.firstinspires.ftc.teamcode.otherCode.utilNextFTCcopy.MiscVar;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import com.qualcomm.robotcore.hardware.Gamepad;

@NextTeleop(name ="Shooter Test", group = "Custom Tuning")
public class ShootTest extends NextOpMode {

    private enum PidfTerm { P, I, D, V }
    private static final double PIDF_STEP = 0.0001;
    private final boolean[] previousPidfButtons = new boolean[4];
    private PidfTerm selectedPidf = PidfTerm.P;
    private double pidfStepMultiplier = 1;
    private boolean previousDpadLeft;
    private boolean previousDpadRight;

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
        bot.getFollower(hardwareMap);
        bot.initNormal(hardwareMap);
        bot.shooter.startFlywheel();
        
        CommandGamepad gp = new CommandGamepad(gamepad1);
        gp.dpadLeft().onTrue(bot.shooter.adjustShooterSpeed('d'));
        gp.dpadRight().onTrue(bot.shooter.adjustShooterSpeed('u'));
        gp.dpadDown().onTrue(bot.shooter.adjustShooterSpeed('D'));
        gp.dpadUp().onTrue(bot.shooter.adjustShooterSpeed('U'));

        gp.x().onTrue(Commands.instant(() -> adjustHoodAngle(-0.02)));
        gp.b().onTrue(Commands.instant(() -> adjustHoodAngle(0.02)));
        gp.y().onTrue(Commands.instant(() -> adjustHoodAngle(0.1)));
        gp.a().onTrue(Commands.instant(() -> adjustHoodAngle(-0.1)));

        gp.rightTrigger().isOver(0.5)
                .onTrue(bot.intake.intake(true))
                .onTrue(bot.shooter.openGate())
                .onFalse(bot.intake.intake(false))
                .onFalse(bot.shooter.closeGate());

    }
    @Override
    public void periodic(){
        updatePidfControls(gamepad2);
        // Advance Pedro localization before reading pose for the range calculation.
        bot.updateFollower();
        bot.updateTargetDistance();
        bot.hood.hoodUpDistance();
        bot.standardTelemetry();

    }

    private void updatePidfControls(Gamepad gp2) {
        boolean[] pidfButtons = {gp2.x, gp2.y, gp2.a, gp2.b};
        PidfTerm[] terms = {PidfTerm.P, PidfTerm.I, PidfTerm.D, PidfTerm.V};
        for (int i = 0; i < pidfButtons.length; i++) {
            if (pidfButtons[i] && !previousPidfButtons[i]) selectedPidf = terms[i];
            previousPidfButtons[i] = pidfButtons[i];
        }

        if (gp2.dpad_right && !previousDpadRight) pidfStepMultiplier *= 10;
        if (gp2.dpad_left && !previousDpadLeft) pidfStepMultiplier = Math.max(0.000001, pidfStepMultiplier / 10);
        previousDpadRight = gp2.dpad_right;
        previousDpadLeft = gp2.dpad_left;

        double direction = gp2.dpad_up ? 1 : (gp2.dpad_down ? -1 : 0);

        if (direction != 0) {
            double adjustment = direction * PIDF_STEP * pidfStepMultiplier;
            switch (selectedPidf) {
                case P: MiscVar.shootkP = Math.max(0, MiscVar.shootkP + adjustment); break;
                case I: MiscVar.shootkI = Math.max(0, MiscVar.shootkI + adjustment); break;
                case D: MiscVar.shootkD = Math.max(0, MiscVar.shootkD + adjustment); break;
                case V: MiscVar.shootkV = Math.max(0, MiscVar.shootkV + adjustment); break;
            }
            bot.shooter.refreshPIDTuning();
        }
        dev.nextftc.robot.Telemetry.log("PIDF button map", "X = P, Y = I, A = D, B = F (V)");
        dev.nextftc.robot.Telemetry.log("Editing PIDF", selectedPidf + " (" + pidfButtonName(selectedPidf) + ")");
        dev.nextftc.robot.Telemetry.log("PIDF step", PIDF_STEP * pidfStepMultiplier +
                " (d-pad left/right divides/multiplies by 10; up/down adjusts)");
    }

    private String pidfButtonName(PidfTerm term) {
        switch (term) {
            case P: return "X";
            case I: return "Y";
            case D: return "A";
            case V: return "B";
            default: return "?";
        }
    }
    @Override
    public void end(){
        bot.shutDown();

    }

    private void adjustHoodAngle(double adjustment) {
        bot.hood.hoodAngle = Math.max(MiscVar.hoodMin,
                Math.min(MiscVar.hoodMax, bot.hood.hoodAngle + adjustment));
        bot.hood.hoodUpDistance().schedule();
    }
}
