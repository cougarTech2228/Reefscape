package frc.robot.subsystems.algaeAcquirer;

public class AlgaeAcquirerConstants {
    public static final int acquireVoltage = -8; // to be changed
    public static final int shootVoltage = 12; // to be changed

    public static final double offset = 0.09;
    public static final double stowedAngle = 0.726 + offset;
    public static final double reefAcquireAngle = 0.48 + offset;
    public static final double floorAcquireAngle = 0.5 +offset;
    public static final double bargeShootAngle = 0.60 +offset;
    public static final double processorShootAngle = 0.5 +offset;

    public static final double ANGLE_MIN  = 0.3;
    public static final double ANGLE_MAX  = 0.726 + offset;

    public static final double closedLoopAngleAllowedError = 0.02;

    public static final double kManualUpVoltage = 0.5;
    public static final double kManualDownVoltage = -0.5;
    
    // below this velocity and above this current will be concidered loaded
    public static final double kLoadedVelocityThreshold = 1000;
    public static final double kLoadedCurrentDrawThreshold = 5;
}
