package org.firstinspires.ftc.teamcode.otherCode.Mecanisms;

import static com.pedropathing.ivy.groups.Groups.parallel;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.otherCode.robotMap;

import dev.nextftc.hardware.actuators.NextRGBIndicator;
import dev.nextftc.robot.Mechanism;

public class Lights implements Mechanism {

    public Lights(){

    }
    public final NextRGBIndicator lLight = new NextRGBIndicator(robotMap.leftLight);
    public final NextRGBIndicator rlight = new NextRGBIndicator(robotMap.rightLight);


    public Command setLight(char index, NextRGBIndicator.Color color){
        if (index == 'l'){ // Only sets left
            return Commands.instant(()->lLight.setColor(color));
        } else if (index == 'r') { // Only sets right
            return Commands.instant(()->rlight.setColor(color));
        } else if (index == 'b') { // Sets both if b is pushed through
            return parallel(
                    Commands.instant(()->lLight.setColor(color)),
                    Commands.instant(()->rlight.setColor(color))
            );
        } else{
            return null;
        }
    }

    public Command lightBrightness(char index, double b){
        if (index == 'l'){ // Only sets left
            return Commands.instant(()->lLight.setBrightness(b));
        } else if (index == 'r') { // Only sets right
            return Commands.instant(()->rlight.setBrightness(b));
        } else if (index == 'b') { // Sets both if b is pushed through
            return parallel(
                    Commands.instant(()->lLight.setBrightness(b)),
                    Commands.instant(()->rlight.setBrightness(b))
            );
        } else{
            return null;
        }
    }
}
