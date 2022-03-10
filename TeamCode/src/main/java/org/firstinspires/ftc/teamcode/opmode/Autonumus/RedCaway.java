package org.firstinspires.ftc.teamcode.opmode.Autonumus;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.mechanism.RevControlHub;
import org.firstinspires.ftc.teamcode.mechanism.RevHubControlActuators;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.List;
@Autonomous(name = "RedCAway", group = "FTC14464")
public class RedCaway extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };
    private static final String VUFORIA_KEY =
            "AW0B6lj/////AAABmQA5lGR6E03stUwudPz4jjtfiHNBfLnS356MmOfp2XILxG407rQgxA5D4Mr7HvMFwoxLYyhaj3dbd0LN02XL2GzH01nVum1IALLwHX2FPSdobPjGLnENWgIAdFVgfUBPAUGT2kiQKXy87bqEj0TdL8Ba6kAEVvUOBjC81uJ3hYMTaeHCljhwTBkINPLQtxkZY3g3cZchMquPV/wDMyx1pQUpZjef1FtawGLPZR6OonNAUDpOLZF+oZYLqsLARNE1dzs1i7tR3AXTFMqAb72GuzLZ1JJlrveOx+fJTi9QG3TXV/yKvDCvz6rg8Vt9GMJxb8B2wGbz+xJ4WcLXJ6ZdYEzJcz8pDtifWMf3SByweytm";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    int identifiedLevel= 3;
    float markerLocation;
    float duckLocation;
    boolean isDuckFound = false;
    RevHubControlActuators rcIntake = new RevHubControlActuators();
    RevControlHub rcHub = new RevControlHub();
    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
    private void setlocations(){
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.
                int i = 0;

                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                    i++;
                    // Check label to see if the camera get the ducks
                    if (recognition.getLabel().equals("Duck")){
                        isDuckFound = true;
                        duckLocation = recognition.getLeft();
                        telemetry.addData("ojbect Detected d " , "Duck");
                        telemetry.addData("Location " , duckLocation);
                    }

                    // Check label to see if the camera get the ducks
                    if (recognition.getLabel().equals("Marker")){
                        markerLocation = recognition.getLeft();
                        telemetry.addData("ojbect Detected d " , "Marker");
                        telemetry.addData("Location " , markerLocation);
                    }
                }
                telemetry.update();

            }
        }
    }

    private void setLevles(){
        // TODO: Get the level
        if(isDuckFound){
            if (duckLocation > markerLocation){
                identifiedLevel = 2;
            }
            else {
                identifiedLevel = 1;
            }
        }
        else {
            identifiedLevel = 3;
        }
        telemetry.addData("level",identifiedLevel);
        telemetry.update();
    }
    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();
        rcIntake.init(hardwareMap);
        rcHub.init(hardwareMap);
        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d startPose = new Pose2d(0, 0, 0);
        drive.setPoseEstimate(startPose);
        TrajectorySequence trajSeq = drive.trajectorySequenceBuilder(startPose)
                .forward(2)
                .strafeLeft(15)
                .back(15)
                .forward(18)
                .build();
        TrajectorySequence trajSeq1 = drive.trajectorySequenceBuilder(startPose)
                .back(19)
                .build();
        TrajectorySequence trajSeq2 = drive.trajectorySequenceBuilder(startPose)
                   .turn(Math.toRadians(-90),Math.toRadians(90),Math.toRadians(90))
                .strafeRight(5)
                .forward(50)
                //   .strafeRight(50)
                .build();
        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        waitForStart();
// Get the location of Duck
        setlocations();
        setLevles();

        ///code
        // run the arm
        if (!isStopRequested()) {
            if (identifiedLevel == 1) {
                rcHub.armRuntoPosition(2000, 1.0);
            } else if (identifiedLevel == 2){
                rcHub.armRuntoPosition(4000, 1.0);
            } else {
                rcHub.armRuntoPosition(6200, 1.0);
            }
            // 1 path
            drive.followTrajectorySequence(trajSeq);

            // outtake
            rcIntake.setIntakeSpeed(-1);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rcIntake.setIntakeSpeed(0);

            // 2 path
            drive.setPoseEstimate(startPose);
            drive.followTrajectorySequence(trajSeq1);




            //  drive.turn(Math.toRadians(-90));
            // 3 path
            drive.setPoseEstimate(startPose);
            drive.followTrajectorySequence(trajSeq2);

        }


    }
}
