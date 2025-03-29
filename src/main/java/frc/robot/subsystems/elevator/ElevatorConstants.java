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

  // public static final double accelerationFast = 0.05; // Results in hard jerks,
  // caution when using

  public static final double kManualUpVoltage = -1.5;
  public static final double kManualDownVoltage = 0;

  public static final double RATIO = 0.8;
  public static final double OFFSET = 2.079;

  // Enum Stuff
  public static final double HEIGHT_TRANSIT = 0;
  public static final double HEIGHT_CORAL_LOAD = -16.38;// -22.16*RATIO; // -14.85 // 16.38
  public static final double HEIGHT_CORAL_L1 = -7.552 + OFFSET;//9.44*RATIO;
  public static final double HEIGHT_CORAL_L2 = -17.071 + OFFSET;//21.34*RATIO;
  public static final double HEIGHT_CORAL_L3 = -36.496 + OFFSET;//34.417
  public static final double HEIGHT_CORAL_L4 = -79.784 + OFFSET;//99.73*RATIO;
  public static final double HEIGHT_ALGAE_FLOOR = -0;
  public static final double HEIGHT_ALGAE_ON_CORAL = -15.472 + OFFSET;//19.34*RATIO;
  public static final double HEIGHT_ALGAE_PROCESSOR = -12.264 + OFFSET;//15.33*RATIO;
  public static final double HEIGHT_ALGAE_REEF_LOW = HEIGHT_CORAL_L3;
  public static final double HEIGHT_ALGAE_REEF_HIGH = -55.360 + OFFSET;//69.2*RATIO;
  public static final double HEIGHT_ALGAE_BARGE = -100.4 + OFFSET;//125.5*RATIO;

  public static final double HEIGHT_MIN = -100.5;
  public static final double HEIGHT_MAX = 0;

  public static final double ClosedLoopErrorThreshold = 0.1;
}
