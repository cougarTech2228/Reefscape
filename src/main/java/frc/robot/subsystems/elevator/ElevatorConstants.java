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

  // Min extend == 0 Max extend == - 
  public static final double HEIGHT_TRANSIT = 0;
  public static final double HEIGHT_CORAL_LOAD = -23.55;
  public static final double HEIGHT_CORAL_L1 = -8.95;
  public static final double HEIGHT_CORAL_L2 = -26.55;
  public static final double HEIGHT_CORAL_L3 = -50.2;
  public static final double HEIGHT_CORAL_L4 = -97.9;
  public static final double HEIGHT_ALGAE_FLOOR = -5;
  public static final double HEIGHT_ALGAE_ON_CORAL = -10.00;
  public static final double HEIGHT_ALGAE_PROCESSOR = -23.3;
  public static final double HEIGHT_ALGAE_REEF_LOW = HEIGHT_CORAL_L3;
  public static final double HEIGHT_ALGAE_REEF_HIGH = -94.3;
  public static final double HEIGHT_ALGAE_BARGE = -125;

  public static final double ClosedLoopErrorThreshold = 0.1;
}
