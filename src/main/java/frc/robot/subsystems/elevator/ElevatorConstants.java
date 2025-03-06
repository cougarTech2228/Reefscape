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

  // Enum Stuff
  public static final double HEIGHT_TRANSIT = 0;
  public static final double HEIGHT_CORAL_LOAD = -22.16; // -25.12;
  public static final double HEIGHT_CORAL_L1 = -9.44;
  public static final double HEIGHT_CORAL_L2 = -21.34;
  public static final double HEIGHT_CORAL_L3 = -45.62;
  public static final double HEIGHT_CORAL_L4 = -99.73;
  public static final double HEIGHT_ALGAE_FLOOR = -0;
  public static final double HEIGHT_ALGAE_ON_CORAL = -19.34;
  public static final double HEIGHT_ALGAE_PROCESSOR = -15.33;
  public static final double HEIGHT_ALGAE_REEF_LOW = HEIGHT_CORAL_L3;
  public static final double HEIGHT_ALGAE_REEF_HIGH = -69.2;
  public static final double HEIGHT_ALGAE_BARGE = -125.5;

  public static final double HEIGHT_MIN = -125.5;
  public static final double HEIGHT_MAX = 0;

  public static final double ClosedLoopErrorThreshold = 0.1;
}
