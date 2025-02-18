package frc.robot.subsystems.algaeAcquirer;

public class AlgaeAcquirerConstants {
    public static final int acquireVoltage = -8; // to be changed
    public static final int shootVoltage = 12; // to be changed

    
    public static final double stowedAngle = 0.726;
    public static final double reefAcquireAngle = 0.464;
    public static final double floorAcquireAngle = 0.5;
    public static final double bargeShootAngle = 0.6;
    public static final double processorShootAngle = 0.5;

    public static final double closedLoopAngleAllowedError = 0.02;

    public static final double kManualUpVoltage = 0.5;
    public static final double kManualDownVoltage = -0.5;
    
    // below this velocity and above this current will be concidered loaded
    public static final double kLoadedVelocityThreshold = 15;
    public static final double kLoadedCurrentDrawThreshold = 5;
}
