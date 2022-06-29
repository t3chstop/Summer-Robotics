package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;




@TeleOp(name = "Encoder Test", group = "Concept")
public class EncoderTest extends LinearOpMode{

    //Motors
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackRight;

    //Basic Mecanum drive for joysticks
    @Override
    public void runOpMode() throws InterruptedException {

        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                motorFrontLeft.setPower(0.5);
            } else {
                motorFrontLeft.setPower(0);
            }

            if (gamepad1.dpad_left) {
                motorFrontRight.setPower(0.5);
            } else {
                motorFrontRight.setPower(0);
            }

            if (gamepad1.dpad_right) {
                motorBackRight.setPower(0.5);
            } else {
                motorBackRight.setPower(0);
            }

            if (gamepad1.dpad_down) {
                motorBackLeft.setPower(0.5);
            } else {
                motorBackLeft.setPower(0);
            }

            telemetry.addData("frontLeft", motorFrontLeft.getCurrentPosition());
            telemetry.addData("frontRight", motorFrontRight.getCurrentPosition());
            telemetry.addData("backLeft", motorBackLeft.getCurrentPosition());
            telemetry.addData("backRight", motorBackRight.getCurrentPosition());

            telemetry.update();
        }



    }

}