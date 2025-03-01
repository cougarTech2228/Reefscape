package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class CollapseCommand extends Command {

    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;
    private boolean initialized = false;
    private boolean elevatorSet = false;

    public CollapseCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
        addRequirements(elevator, algaeAcquirer, coralCone);
    }

    @Override
    public void initialize() {
        // we want to collapse the algae acquirer only if it's not loaded
        if (algaeAcquirer.isLoaded()) {
            algaeAcquirer.setPosition(AlgaeAcquirer.Position.PROCESSOR_SHOOT);
        } else {
            algaeAcquirer.setPosition(AlgaeAcquirer.Position.STOWED);
            algaeAcquirer.setFlywheelState(AlgaeAcquirer.FlywheelState.STOP);
        }
        // put coral cone in a safe place to move the elevator
        coralCone.setPosition(CoralCone.Position.STOWED);
        initialized = true;
    }

    @Override
    public void execute() {
        if (!initialized){
            return;
        }

        if (algaeAcquirer.isAtSetPosition() && coralCone.isAtSetPosition()) {
            // attachments are in a safe position, move the elevator
            if (algaeAcquirer.isLoaded()) {
                elevator.setPosition(Elevator.Position.ALGAE_PROCESSOR);
            } else {
                elevator.setPosition(Elevator.Position.TRANSIT);
            }
            elevatorSet = true;
        }
    }

    @Override
    public boolean isFinished() {
        if (elevatorSet && elevator.isAtSetPosition()) {
            coralCone.setPosition(CoralCone.Position.STOWED);
            return true;
        }
        return false;
    }
}
