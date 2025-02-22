package frc.robot.subsystems.elevator;


import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.elevator.Elevator.Position;

public interface ElevatorIO {

  @AutoLog
  public static class ElevatorIOInputs {
    public double position_A = 0.0;
    public double velocity_A = 0.0;
    public double appliedVolts_A = 0.0;
    public double currentAmps_A = 0.0;
    public double pidOutput = 0.0;

    public double position_B = 0.0;
    public double velocity_B = 0.0;
    public double appliedVolts_B = 0.0;
    public double currentAmps_B = 0.0;

    public boolean bottomLimit = false;
    public boolean isAtSetPosition = false;
    public double setPosition = 0.0;
    public double closedLoopError = 0.0;
  }

  /** Update the set of loggable inputs. */
  public default void updateInputs(ElevatorIOInputs inputs) {};

  public default void setPosition(Position position) {};

  // Only used for sysid runCharacterization
  public default void setVoltage(double output){};

  public default void simulationPeriodic(){};

  public default void setManualPosition(double position){};
}
