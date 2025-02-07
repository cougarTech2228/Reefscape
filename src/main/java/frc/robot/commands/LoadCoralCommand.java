package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class LoadCoralCommand extends Command {
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;

    public LoadCoralCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralClaw) {
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralClaw;
    }

    @Override
    public void initialize() {
        algaeAcquirer.setPosition(AlgaeAcquirer.Position.STOWED);
        elevator.setPosition(Elevator.Position.CORAL_LOAD);
        coralCone.setPosition(CoralCone.Position.LOAD);
    }

    @Override
    public void execute() {
        if (coralCone.isAtSetPosition()) {
            coralCone.setWheel(CoralCone.WheelState.LOAD);
        }
    }

    @Override
    public boolean isFinished() {
        return elevator.isAtSetPosition() && algaeAcquirer.isAtSetPosition() && coralCone.isAtSetPosition()
                && coralCone.isLoaded();
    }
}
