package frc.robot.subsystems.coralCone;

public class CoralConeConstants {
    public static final double motorReduction = 1.0;
    public static final int currentLimit = 40;
    public static final int topLimitSwitchDI = 1;
    public static final int bottomLimitSwitchDIO = 2;

    // CHANGE, angle motor values
    public static final double loadAngle = 180;
    public static final double L1Angle = 0;
    public static final double L2Angle = 0;
    public static final double L3Angle = 0;
    public static final double L4Angle = 0;

    // wheel motor values
    public static final double forwardVoltage = 2;
    public static final double reverseVoltage = -2;
    public static final double transitVoltage = 0; // lock wheel in place

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
