package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.subsystems.elevator.ElevatorIOInputsAutoLogged;

import java.util.concurrent.Flow.Processor;

import org.littletonrobotics.junction.Logger;

public class Elevator extends SubsystemBase {
  private final ElevatorIO io;
  private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();

  public enum Position {
    ALGAE_FLOOR,
    ALGAE_PROCESSOR,
    ALGAE_REEF_LOW,
    ALGAE_REEF_HIGH,
    ALGAE_BARGE,
    CORAL_LOAD,
    CORAL_L1,
    CORAL_L2,
    CORAL_L3,
    CORAL_L4
  };

  public Elevator(ElevatorIO io) {
    this.io = io;
  }


  @Override
  // Runs on a schedule, after some amount of milliseconds
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Elevator", inputs);
  }

  public void setPosition(Position posistion) {
    // ensure coral and algae are in a safe position to move the elevator
    io.setPosition(posistion);
  }

  public boolean isAtSetPosition() {
    return io.isAtSetPosition();
  }
}
