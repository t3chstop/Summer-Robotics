package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.Locale;

@Autonomous(name = "Roadrunner auto test")
public class FF_Auto extends LinearOpMode {
    ColorSensor colorSensor;
    DistanceSensor distanceSensor;
    @Override
    public void runOpMode() {
        colorSensor = hardwareMap.get(ColorSensor.class, "colorsensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "colorsensor");

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Trajectory myTrajectory = drive.trajectoryBuilder(new Pose2d())
                .forward(40)
                .splineTo(new Vector2d(56, -20), Math.toRadians(-90))
                .build();
        Trajectory myTrajectory2 = drive.trajectoryBuilder(new Pose2d())
                .splineTo(new Vector2d(40, 0), Math.toRadians(90

                ))
                .back(40)
                .build();


        telemetry.addData("Initialized", "Press play to start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                //drive.followTrajectory(myTrajectory);
                sleep(4000);
                drive.followTrajectory(myTrajectory2);
                sleep(30000);
//                telemetry.addData("Distance (cm)",
//                        String.format(Locale.US, "%.02f", distanceSensor.getDistance(DistanceUnit.CM)));
//                telemetry.update();
            }
        }
    }
}


