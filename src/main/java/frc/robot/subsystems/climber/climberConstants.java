package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj.DutyCycle;

public class ClimberConstants {

    public static final double RAISE_VOLTAGE = 2;
    public static final double LOWER_VOLTAGE = -2;
    public static final double servoLockedAngle = 90;
    public static final double servoUnlockedAngle = 0;

    // public static final int kClimberAngleEncoderDIO = 1; use the one in constants

    public static final double climberAngleThreshold = 2; // to change
    public static final double kIZone = 2; // to change
    public static final double ClosedLoopErrorThreshold = 2;

}
