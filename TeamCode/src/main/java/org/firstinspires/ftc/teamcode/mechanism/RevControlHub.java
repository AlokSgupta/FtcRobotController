package org.firstinspires.ftc.teamcode.mechanism;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class RevControlHub {
    private TouchSensor touchSensor;
    private DcMotor armMotor;
    private Servo capServo;
    private Servo handleServo;
    private double ticksPerRotation;

    public void init (HardwareMap hwMap){
        touchSensor = (TouchSensor) hwMap.get("touch");
        armMotor = (DcMotor) hwMap.get("arm");
        capServo = (Servo) hwMap.get("rev1");
        handleServo = (Servo) hwMap.get("rev2");
        armMotor.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public boolean getTouchSensorState() {
        return touchSensor.isPressed();
    }

    public void setArmMotorSpeed(double speed){
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setPower(speed);
    }

    public double getArmMotorRotations() {
        return armMotor.getCurrentPosition();
    }

    public void level3(){

    }

    public void armRuntoPosition(int position, double power){
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setTargetPosition(position);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(power);
       // armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setServoPosition (double postion){
        capServo.setPosition(postion);

    }

    public void setHServoPosition (double postion){
        handleServo.setPosition(postion);

    }
}
