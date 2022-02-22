package org.firstinspires.ftc.teamcode.mechanism;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RevHubControlDriveTrain {
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    public void init(HardwareMap hwMap){
        frontLeftMotor = (DcMotor) hwMap.get("FL");
        frontRightMotor = (DcMotor) hwMap.get("FR");
        backLeftMotor = (DcMotor) hwMap.get("BL");
        backRightMotor = (DcMotor) hwMap.get("BR");
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setFrontLeftMotorSpeed(double speed)
    {
        frontLeftMotor.setPower(speed);
    }
    public void setFrontRightMotorSpeed(double speed)
    {
        frontRightMotor.setPower(speed);
    }
    public void setBackLeftMotorSpeed(double speed)
    {
        backLeftMotor.setPower(speed);
    }
    public void setBackRightMotorSpeed(double speed)
    {
        backRightMotor.setPower(speed);
    }
    public void setMecanumMove(double speedX, double speedY)
    {
        frontLeftMotor.setPower((-speedY+speedX)*(1.0));
        frontRightMotor.setPower((-speedY-speedX)*(1.0));
        backLeftMotor.setPower((-speedY-speedX)*(1.0));
        backRightMotor.setPower((-speedY+speedX)*(1.0));
    }

    public void setMecanumTurn(double speedRX)
    {
        frontLeftMotor.setPower((speedRX)*(0.5));
        frontRightMotor.setPower((speedRX)*(-0.5));
        backLeftMotor.setPower((speedRX)*(0.5));
        backRightMotor.setPower((speedRX)*(-0.5));
    }
}
