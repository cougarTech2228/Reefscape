package frc.robot.subsystems.elevator;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.elevator.Elevator.Position;

public interface ElevatorIO {
  @AutoLog
  public static class ElevatorIOInputs {
    public double positionRad = 0.0;
    public double velocityRadPerSec = 0.0;
    public double appliedVolts = 0.0;
    public double currentAmps = 0.0;
    public boolean bottomLimit = false;
  }

  /** Update the set of loggable inputs. */
  public void updateInputs(ElevatorIOInputs inputs);

  public void setPosition(Position position);

  public double getPosition();

  public boolean isAtBottomLimit();

  public boolean isAtSetPosition();

  // Only used for sysid runCharacterization
  public void setVoltage(double output);
}
