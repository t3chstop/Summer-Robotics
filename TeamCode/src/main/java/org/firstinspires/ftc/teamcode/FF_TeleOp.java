package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;




@TeleOp(name = "FF TeleOp(One controller)", group = "Concept")
public class FF_TeleOp extends LinearOpMode{

    //Motors
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor intake;
    DcMotor slide;
    DcMotor flywheelL;
    DcMotor flywheelR;
    Servo slideServo;

    //Basic Mecanum drive for joysticks
    @Override
    public void runOpMode() throws InterruptedException {

        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        intake = hardwareMap.dcMotor.get("intake");
        slide = hardwareMap.dcMotor.get("slide");
        flywheelL = hardwareMap.dcMotor.get("flyWheel");
        flywheelR = hardwareMap.dcMotor.get("flyWheel2");
        slideServo = hardwareMap.servo.get("slideServo");

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double vertical = 0.7 * gamepad1.left_stick_y;
            double horizontal = -0.7 * gamepad1.left_stick_x;
            double pivot = -0.7 * gamepad1.right_stick_x;

            motorFrontRight.setPower(vertical - pivot - horizontal);
            motorBackRight.setPower(vertical - pivot + horizontal);
            motorFrontLeft.setPower(vertical + pivot + horizontal);
            motorBackLeft.setPower(vertical + pivot - horizontal);



            //Engage Linear Slide
            if (gamepad1.a) {
                slide.setPower(0.75);
            } else if (gamepad1.b) {
                slide.setPower(-0.5);
            } else {
                slide.setPower(0);
            }


            //Intake
            if (gamepad1.x) {
                intake.setPower(-1);
            } else {
                intake.setPower(0);
            }

            //Flywheel
            if (gamepad1.left_trigger>0.8) {
                flywheelL.setPower(-1);
            } else if (gamepad1.right_trigger>0.8) {
                flywheelL.setPower(1);
            } else {
                flywheelL.setPower(0);
            }


            //Box
            if (gamepad1.left_bumper) {
                slideServo.setPosition(slideServo.getPosition() + 0.05);
                sleep(100);
            } else if (gamepad1.right_bumper) {
                slideServo.setPosition(0.02);
            }

            //Automated lift and tilt box
            if (gamepad1.dpad_up) {
                liftAndTilt();
            }
            if (gamepad1.dpad_down) {
                homeSlide();
            }

            if (gamepad1.dpad_right) {
                slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            telemetry.addData("Slide position", slide.getCurrentPosition());
            telemetry.update();
        }

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