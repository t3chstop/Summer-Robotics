package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.Locale;

@Autonomous(name = "SensorTest")
public class SensorTest extends LinearOpMode {
    ColorSensor colorSensor;
    DistanceSensor distanceSensor;
    DcMotor intake;
    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;
    DcMotor motorBackLeft;
    DcMotor motorBackRight;

    @Override
    public void runOpMode() {
        colorSensor = hardwareMap.get(ColorSensor.class, "colorsensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "colorsensor");
        intake = hardwareMap.dcMotor.get("intake");
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");

        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        telemetry.addData("Initialized", "Press play to start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                opModeIsActive();
                intake.setPower(-1);

                while (distanceSensor.getDistance(DistanceUnit.CM)>4) {
                    opModeIsActive();
                    motorFrontRight.setPower(-0.3);
                    motorFrontLeft.setPower(-0.3);
                    motorBackRight.setPower(-0.3);
                    motorBackLeft.setPower(-0.3);

                    telemetry.addData("Freight is not inside box at distance:",
                            String.format(Locale.US, "%.02f", distanceSensor.getDistance(DistanceUnit.CM)));
                    telemetry.update();
                }

                motorFrontRight.setPower(0);
                motorFrontLeft.setPower(0);
                motorBackRight.setPower(0);
                motorBackLeft.setPower(0);

                intake.setPower(0);

                telemetry.addData("Freight is inside box at distance:",
                        String.format(Locale.US, "%.02f", distanceSensor.getDistance(DistanceUnit.CM)));
                telemetry.update();

                sleep(30000);

            }
        }
    }
}


