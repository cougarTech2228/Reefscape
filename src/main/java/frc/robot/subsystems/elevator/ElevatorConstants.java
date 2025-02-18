package frc.robot.subsystems.elevator;

public class ElevatorConstants {
  public static final int elevatorACanID = 18;
  public static final int elevatorBCanID = 19;
  public static final double motorReduction = 1.0;
  
  /**
   * The maximum amount of stator current the drive motors can apply without
   * slippage.
   */
  public static final double slipCurrent = 120;

  
  public static final double topPosition = 50;
  public static final double bottomPosition = 0;
  // 1.2
  // 0.12

  // public static final double accelerationFast = 0.05; // Results in hard jerks, caution when using
  
  public static final double kManualUpVoltage = -1.5;
  public static final double kManualDownVoltage = 0;

  //Enum Stuff

  // Min extend == 0 Max extend == -24.96 
  public static final double HEIGHT_TRANSIT = 0;
  public static final double HEIGHT_CORAL_LOAD = -4.71;
  public static final double HEIGHT_CORAL_L1 = -1.79;
  public static final double HEIGHT_CORAL_L2 = -5.31;
  public static final double HEIGHT_CORAL_L3 = -10.04;
  public static final double HEIGHT_CORAL_L4 = -19.58;
  public static final double HEIGHT_ALGAE_FLOOR = -1;
  public static final double HEIGHT_ALGAE_ON_CORAL = -2.00;
  public static final double HEIGHT_ALGAE_PROCESSOR = -4.66;
  public static final double HEIGHT_ALGAE_REEF_LOW = HEIGHT_CORAL_L3;
  public static final double HEIGHT_ALGAE_REEF_HIGH = -18.86;
  public static final double HEIGHT_ALGAE_BARGE = -24.94;

  public static final double ClosedLoopErrorThreshold = 0.05;
}
