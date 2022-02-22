package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanism.RevHubControlActuators;
import org.firstinspires.ftc.teamcode.mechanism.RevHubControlDriveTrain;

@TeleOp(name = "FFTeleOp", group = "FTC14464")
public class FFTeleOp extends OpMode {

    RevHubControlDriveTrain rcDrive = new RevHubControlDriveTrain();
    RevHubControlActuators rcIntake = new RevHubControlActuators();
    @Override
    public void init() {
        rcDrive.init(hardwareMap);
        rcIntake.init(hardwareMap);
    }

    @Override
    public void loop() {
        double speedY= -gamepad1.left_stick_y;
        double speedX= gamepad1.left_stick_x;
        double speedRX= gamepad1.right_stick_x;

        rcDrive.setMecanumMove(speedX,speedY);
        rcDrive.setMecanumTurn(speedRX);

        if (gamepad1.right_trigger>0)
            rcIntake.setIntakeSpeed(gamepad1.right_trigger);
        else if (gamepad1.left_trigger>0)
            rcIntake.setIntakeSpeed(-gamepad1.left_trigger);
        else
            rcIntake.setIntakeSpeed(0);
        rcIntake.setDuckWheelSpeed(gamepad2.right_trigger);

        rcDrive.setFrontLeftMotorSpeed(gamepad2.left_stick_y);
        rcDrive.setFrontRightMotorSpeed(gamepad2.right_stick_y);
        rcDrive.setBackLeftMotorSpeed(gamepad2.left_trigger);
        rcDrive.setBackRightMotorSpeed(gamepad2.right_trigger);

    }
}
