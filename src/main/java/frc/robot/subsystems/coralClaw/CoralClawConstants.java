package frc.robot.subsystems.coralClaw;

public class CoralClawConstants {
  

  public static final int coralClawAngleCanID = 17 // might be changed
  public static final int coralClawFlyWheelCanID = 18 // might be changed

  public static final double motorReduction = 1.0;
  public static final int currentLimit = 40;
  public static final int topLimitSwitchDI = 1;
  public static final int bottomLimitSwitchDIO = 2;

  public static final double topAnglePosition = 180; // is to change
  public static final double bottomAnglePosition = 0; // is to change
  // 1.2
  // 0.12
  public static final double velocitySlow = 1.2;
  public static final double velocityFast = 0.12;
  public static final double accelerationSlow = 0.15;
  public static final double accelerationFast = 0.05; // Results in hard jerks, caution when using
}
