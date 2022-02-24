package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanism.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanism.RevControlHub;
import org.firstinspires.ftc.teamcode.mechanism.RevHubControlActuators;

@TeleOp (name="FTC14464-FF", group = "FTC14464")
public class OpMode_v1 extends OpMode {

    RevControlHub rcHub = new RevControlHub();
    RevHubControlActuators rcIntake = new RevHubControlActuators();
    MecanumDrive mcDrive = new MecanumDrive();

    @Override
    public void init() {
        rcHub.init(hardwareMap);
        rcIntake.init(hardwareMap);
        mcDrive.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Touch Sensor" , rcHub.getTouchSensorState());
        telemetry.addData("button",gamepad1.a);
        telemetry.addData("button",gamepad1.b);
        telemetry.addData("Press ", rcHub.getArmMotorRotations());
        telemetry.addData("FL ", mcDrive.getFLMotorRotations());
        telemetry.addData("FR ", mcDrive.getFRMotorRotations());
        telemetry.addData("BL ", mcDrive.getBLMotorRotations());
        telemetry.addData("BR ", mcDrive.getBRMotorRotations());

        // Drive Train  Starts
        //double speedY= -gamepad1.left_stick_y; //Y is reversed
        //double speedX= gamepad1.left_stick_x; // X co-ordinates
        //double speedRX= gamepad1.right_stick_x; // Angle or turn
        mcDrive.DriveRobot(-gamepad1.left_stick_y*.6,-gamepad1.left_stick_x*.6,-gamepad1.right_stick_x*.6);
        // Drive train ends

        // Intake  Starts
        if (gamepad1.right_trigger>0)
            rcIntake.setIntakeSpeed(gamepad1.right_trigger);
        else if (gamepad1.left_trigger>0)
            rcIntake.setIntakeSpeed(-gamepad1.left_trigger);
        else
            rcIntake.setIntakeSpeed(0);
        // Intake Ends

        // Carosuel Wheel - Game Pad 2
        if (gamepad2.right_trigger>0)
            rcIntake.setDuckWheelSpeed(gamepad2.right_trigger);
        else if (gamepad2.left_trigger>0)
            rcIntake.setDuckWheelSpeed(-gamepad2.left_trigger);
        else
            rcIntake.setDuckWheelSpeed(0);
        //Cap stone - Game Pad 2
        rcHub.setServoPosition(gamepad2.left_stick_x);

        // Arm program - Starts
        if (rcHub.getTouchSensorState()){
            rcHub.setArmMotorSpeed(0);
            // Press b: Go to level 2
            if(gamepad1.b) {
                rcHub.armRuntoPosition(4000,1.0);
                telemetry.addData("Press b", rcHub.getArmMotorRotations());
                telemetry.update();
            }
            // Press Y: goto Level 3
            if(gamepad1.y){
                rcHub.armRuntoPosition(6200,1.0);
                telemetry.addData("Press Y", rcHub.getArmMotorRotations());
                telemetry.update();
            }
        }
        else
        {
            // back to default Position. And it will stop as soon as touch sensor is pressed
            if(gamepad1.a)
            {
                rcHub.setArmMotorSpeed(-1.0);
                telemetry.addData("Press a", rcHub.getArmMotorRotations());
                telemetry.update();
            }
            // Stop the arm at a location
            if(gamepad1.x)
            {
                rcHub.setArmMotorSpeed(0.0);
                telemetry.addData("Press x", rcHub.getArmMotorRotations());
                telemetry.update();
            }
        }
        // Arm program - End
    }
}
