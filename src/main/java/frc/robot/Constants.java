// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * This class defines the runtime mode used by AdvantageKit. The mode is always
 * "real" when running
 * on a roboRIO. Change the value of "simMode" to switch between "sim" (physics
 * sim) and "replay"
 * (log replay from a file).
 */
public final class Constants {
    public static final Mode simMode = Mode.SIM;
    public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

    public static final double robotWidth = 0.876;
    public static final double robotLength = 0.98425;

    public static enum Mode {
        /** Running on a real robot. */
        REAL,
        /** Running a physics simulator. */
        SIM,
        /** Replaying from a log file. */
        REPLAY
    }

    // CAN IDs
    public static final int algaeFlywheelLeftNeoCanID = 4;
    public static final int algaeFlywheelRightNeoCanID = 5;
    public static final int algaeAngleMotorNeoCanID = 8;

    public static final int coralFlywheelCanID = 6;
    public static final int coralRotateCanID = 7;

    public static final int climberFalconCanID = 9;
    public static final int kClimberAngleEncoderDIO = 1;

    public static final int frontLeftDriveFalconCanID = 10;
    public static final int frontLeftSteerCanID = 11;
    public static final int rearLeftDriveCanID = 12;
    public static final int rearLeftSteerCanID = 13;
    public static final int frontRightDriveCanID = 14;
    public static final int frontRightSteerCanID = 15;
    public static final int rearRightDriveCanID = 16;
    public static final int rearRightSteerCanID = 17;

    public static final int elevatorLeaderCanID = 18;
    public static final int elevatorFollowerCanID = 19;

    // DIOs
    public static final int CoralBeamBreakSensorDIO = 0;
}
