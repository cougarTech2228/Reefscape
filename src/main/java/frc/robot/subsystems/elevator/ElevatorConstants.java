package frc.robot.subsystems.elevator;

public class ElevatorConstants {
  public static final int elevatorLeaderCanID = 18;
  public static final int elevatorFollowerCanID = 19;
  public static final double motorReduction = 1.0;
  public static final int currentLimit = 40;
  public static final int topLimitSwitchDI = 1;
  public static final int bottomLimitSwitchDIO = 2;
  
  public static final double topPosition = 50;
  public static final double bottomPosition = 0;
  // 1.2
  // 0.12
  public static final double velocitySlow = 1.2;
  public static final double velocityFast = 0.12;
  public static final double accelerationSlow = 0.15;
  public static final double accelerationFast = 0.05; // Results in hard jerks, caution when using
  
  //Enum Stuff

  public static final double HEIGHT_ALGAE_FLOOR = 10.0;
  public static final double HEIGHT_ALGAE_PROCESSOR = 20.0;
  public static final double HEIGHT_ALGAE_REEF_LOW = 30.0;
  public static final double HEIGHT_ALGAE_REEF_HIGH = 40.0;
  public static final double HEIGHT_ALGAE_BARGE = 50.0;
  public static final double HEIGHT_CORAL_LOAD = 60.0;
  public static final double HEIGHT_CORAL_L1 = 70.0;
  public static final double HEIGHT_CORAL_L2 = 80.0;
  public static final double HEIGHT_CORAL_L3 = 90.0;
  public static final double HEIGHT_CORAL_L4 = 100.0;

  public static final double ClosedLoopErrorThreshold = 2;
}
