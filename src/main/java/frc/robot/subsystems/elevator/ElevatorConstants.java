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
  public static final double LEVEL1 = 1;
  public static final double LEVEL2 = 2;
  public static final double LEVEL3 = 3;
  public static final double LEVEL4 = 4;
  public static final double PROCESSOR = 5;
  public static final double NET = 6;
}
