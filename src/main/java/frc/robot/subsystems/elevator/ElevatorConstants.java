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
  public static final double velocity = 0.05; // lower is faster
  // public static final double velocityFast = 0.12;
  public static final double acceleration = 0.25; // lower is faster
  // public static final double accelerationFast = 0.05; // Results in hard jerks, caution when using
  
  public static final double kManualUpVoltage = -1.5;
  public static final double kManualDownVoltage = 0;

  //Enum Stuff

  // Min extend == 0 Max extend == -38.41 
  public static final double HEIGHT_TRANSIT = 0;
  public static final double HEIGHT_CORAL_LOAD = -7.86;
  public static final double HEIGHT_CORAL_L1 = -2.96;
  public static final double HEIGHT_CORAL_L2 = -7.72;
  public static final double HEIGHT_CORAL_L3 = -15.72;
  public static final double HEIGHT_CORAL_L4 = -29.59;
  public static final double HEIGHT_ALGAE_FLOOR = -1.86;
  public static final double HEIGHT_ALGAE_PROCESSOR = -5.45;
  public static final double HEIGHT_ALGAE_REEF_LOW = HEIGHT_CORAL_L3;
  public static final double HEIGHT_ALGAE_REEF_HIGH = -22.86;
  public static final double HEIGHT_ALGAE_BARGE = -38;

  public static final double ClosedLoopErrorThreshold = 2;
}
