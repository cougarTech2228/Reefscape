package frc.robot.commands.ui;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.Elevator.Position;

public class LoadCoralCommand extends Command {
  private final Elevator elevator;

  public LoadCoralCommand(Elevator elevator) {
    this.elevator = elevator;
  }

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    super.initialize();
    elevator.setPosition(Position.CORAL_LOAD);
  }

  // @Override
  // public void execute() {
  //     // TODO Auto-generated method stub
  //     super.execute();
  // }

  @Override
  public boolean isFinished() {
    return elevator.isAtSetPosition();
  }
}
