package frc.robot.subsystems.climber;

public class ClimberConstants {

    public static final double RAISE_VOLTAGE = 2;
    public static final double LOWER_VOLTAGE = -2;
    public static final double servoLockedAngle = 0.1;
    public static final double servoUnlockedAngle = 0.5;

    public static final double climberAngleThreshold = 0.02; // to change
    public static final double kIZone = 0.02; // to change

    public static final double ClosedLoopErrorThreshold = 2;
    public static final double angleExtended = 0.516;
    public static final double angleRetracted = 0.345;

    // encoder value less than this amount is a critical failure!
    public static final double overRetractThreshold = 0.330;

    // public static final double angleExtended = 0.4;
    // public static final double angleRetracted = 0.33;

}