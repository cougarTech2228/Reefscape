package frc.robot.subsystems.elevator;


import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.elevator.Elevator.Position;

public interface ElevatorIO {

  @AutoLog
  public static class ElevatorIOInputs {
    public double position = 0.0;
    public double velocity = 0.0;
    public double appliedVolts = 0.0;
    public double currentAmps = 0.0;
    public boolean bottomLimit = false;
    public boolean isAtSetPosition = false;
  }

  /** Update the set of loggable inputs. */
  public default void updateInputs(ElevatorIOInputs inputs) {};

  public default void setPosition(Position position) {};

  // Only used for sysid runCharacterization
  public default void setVoltage(double output){};

  public default void simulationPeriodic(){};
}
