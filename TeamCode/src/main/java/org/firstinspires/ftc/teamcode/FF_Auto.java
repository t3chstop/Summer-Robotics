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

        telemetry.addData("Initialized", "Press play to start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                drive.followTrajectory(phase1);
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
}


