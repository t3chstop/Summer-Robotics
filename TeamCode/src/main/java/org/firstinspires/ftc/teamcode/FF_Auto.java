package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Autonomous(name = "Max Point Auto")
public class FF_Auto extends LinearOpMode {
    ColorSensor colorSensor;
    DistanceSensor distanceSensor;
    Servo slideServo;
    DcMotor slide;

    private int duckPosition; //0 = left, 1=middle, 2=right
    /*
    Left: left: 52, right:128
    Middle: left: 289, right: 357
    Right: left: 514, right: 586
     */
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AaACIpX/////AAABmTUBppEwL0ifp04EjenXeghJ4ma+hRgg6QNrRRuLa4ImepASp+69DtGTTThY7lS4UnLeAN52ahhVW1fXYtPFI7UmJD7Gfc38YJK9sss7FQa7l9guGH3fZAyNFqkxsyw+EBuhS5b+xGTwJxbLk+jRi+zjz5NmUi+yk/A3BrGxxBU5m3vJ3uzygWk47ct2yT7IyLHWdXOgtcxwDoQmod/Tkpnc4ukFA8ASckGu87nH5cTHZzbQXWm2Wz+Xut+WgojFqRXG+mc8IzXW3+ffSG1FvOu8hipCuFWEMsKDSCw1ZwYZommvlJWJJNKk1Uq+SAikGJ6t8yX0kqMIjTq2S36YGm0dvcy2G3IBUsyEG7Q8KHw9";



    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        initVuforia();
        initTfod();
        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            ArrayList<Recognition> recognitions = (ArrayList<Recognition>) tfod.getUpdatedRecognitions();






            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // Ift your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1.12, 16.0/9.0);
        }


        colorSensor = hardwareMap.get(ColorSensor.class, "colorsensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "colorsensor");
        slideServo = hardwareMap.servo.get("slideServo");
        slide = hardwareMap.dcMotor.get("slide");

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d(-40, -63, 0));

        Trajectory phase1 = drive.trajectoryBuilder(new Pose2d(-40, -63, 0))
                .splineTo(new Vector2d(-10, -40), Math.toRadians(90))
                .build();

        Trajectory phase2 = drive.trajectoryBuilder(new Pose2d(-10, -40, Math.toRadians(90)), true)
                .splineTo(new Vector2d(20, -63), Math.toRadians(0))
                .build();

        Trajectory phase3 = drive.trajectoryBuilder(new Pose2d(20, -63, Math.toRadians(180)))
                .splineTo(new Vector2d(-10, -40), Math.toRadians(90))
                .build();

        slideServo.setPosition(0.02);

        telemetry.addData("Initialized", "Press play to start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
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
                            duckPosition = -1;
                            if(recognition.getLabel().equals("Duck")) {
                                if(recognition.getLeft() > 0 && recognition.getLeft() < 170) {
                                    duckPosition = 1;
                                }
                                else if(recognition.getLeft() >= 170 && recognition.getLeft() <= 401) {
                                    duckPosition = 2;
                                }
                                else if(recognition.getLeft() >= 401 ) {
                                    duckPosition = 3;
                                }
                            }
                            i++;
                        }
                        telemetry.addData("Duck position", duckPosition);
                        telemetry.update();
                    }
                }


                drive.followTrajectory(phase1);

                if (duckPosition==1) {

                }

                sleep(4000);
                drive.followTrajectory(phase2);
                sleep(4000);
                drive.followTrajectory(phase3);
                sleep(4000);
                drive.followTrajectory(phase2);
                sleep(4000);
                drive.followTrajectory(phase3);
                sleep(4000);
                drive.followTrajectory(phase2);
                sleep(4000);
                drive.followTrajectory(phase3);
                sleep(30000);
            }
        }
    }

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

    public void liftAndTilt() {
        slide.setTargetPosition(1810);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(0.75);
        while (slide.isBusy()) {
            continue;
        }
        slide.setPower(0);
        slideServo.setPosition(0.6);
    }

    public void homeSlide() {
        slideServo.setPosition(0.02);
        sleep(500);
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(-0.75);
        while (slide.isBusy()) {
            continue;
        }
        slide.setPower(0);
    }

}


