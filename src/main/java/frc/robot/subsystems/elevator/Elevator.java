package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.subsystems.elevator.ElevatorIOInputsAutoLogged;

import org.littletonrobotics.junction.Logger;

public class Elevator extends SubsystemBase {
  private final ElevatorIO io;
  private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();

  public Elevator(ElevatorIO io) {
    this.io = io;
  }

  @Override
  // Runs on a schedule, after some amount of milliseconds
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Elevator", inputs);

    if (io.isAtBottomLimit()) {
      io.setPosition(ElevatorConstants.bottomPosition);
    }
    if (io.isAtTopLimit()) {
      io.setPosition(ElevatorConstants.topPosition);
    }
  }

  public void setPosition(double position) {
    io.setPosition(position);
  }
}
