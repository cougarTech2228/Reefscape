package frc.robot.subsystems.coralCone;

public class CoralConeConstants {
    public static final double motorReduction = 1.0;
    public static final int currentLimit = 40;
    public static final int topLimitSwitchDI = 1;
    public static final int bottomLimitSwitchDIO = 2;

    // Angle motor values
    public static final double safeElevatorAngle = 0.49;
    public static final double stowedAngle = 0.502;
    public static final double loadAngle = 0.521; // 0.288;
    public static final double L1Angle = 0.61;
    public static final double L2Angle = 0.693;
    public static final double L3Angle = L2Angle;
    public static final double L4Angle = 0.823;
    public static final double bargeShootAngle = 0.4;
    public static final double ANGLE_MAX = 0.823;
    public static final double ANGLE_MIN = 0.4;

    // wheel motor values
    public static final double loadVoltage = -2;
    public static final double shootVoltage = 6;
    public static final double transitVoltage = 0; // lock wheel in place
    public static final double shootFastVoltage = 8;
    public static final double extraLoadRotations = 3; // motor rotations to keep loading after the beam break

    // 1.2
    // 0.12
    public static final double velocitySlow = 1.2;
    public static final double velocityFast = 0.12;
    public static final double accelerationSlow = 0.15;
    public static final double accelerationFast = 0.05; // Results in hard jerks, caution when using

    public static final double closedLoopAngleAllowedError = 0.01;

    public static final double kManualUpVoltage = 0.5;
    public static final double kManualDownVoltage = -0.5;
}
