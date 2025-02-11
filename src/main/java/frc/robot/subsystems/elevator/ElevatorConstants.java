package frc.robot.subsystems.elevator;

public class ElevatorConstants {
  public static final int elevatorACanID = 18;
  public static final int elevatorBCanID = 19;
  public static final double motorReduction = 1.0;
  public static final int currentLimit = 80;
  
  /**
   * The maximum amount of stator current the drive motors can apply without
   * slippage.
   */
  public static final double slipCurrent = 120;

  
  public static final double topPosition = 50;
  public static final double bottomPosition = 0;
  // 1.2
  // 0.12
  public static final double velocitySlow = 1.2;
  public static final double velocityFast = 0.12;
  public static final double accelerationSlow = 0.15;
  public static final double accelerationFast = 0.05; // Results in hard jerks, caution when using
  
  public static final double kManualUpVoltage = -2;
  public static final double kManualDownVoltage = 2;

  //Enum Stuff

  // Min extend == 0 Max extend == -38.41 
  public static final double HEIGHT_CORAL_LOAD = 60.0;
  public static final double HEIGHT_CORAL_L1 = -10;
  public static final double HEIGHT_CORAL_L2 = -15;
  public static final double HEIGHT_CORAL_L3 = -20;
  public static final double HEIGHT_CORAL_L4 = -25;
  public static final double HEIGHT_ALGAE_FLOOR = -5;
  public static final double HEIGHT_ALGAE_PROCESSOR = -10;
  public static final double HEIGHT_ALGAE_REEF_LOW = HEIGHT_CORAL_L3;
  public static final double HEIGHT_ALGAE_REEF_HIGH = -22;
  public static final double HEIGHT_ALGAE_BARGE = -38;

  public static final double ClosedLoopErrorThreshold = 2;
}
