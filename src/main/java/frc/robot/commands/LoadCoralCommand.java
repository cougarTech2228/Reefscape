package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.coralCone.CoralCone.WheelState;
import frc.robot.subsystems.elevator.Elevator;

public class LoadCoralCommand extends Command {
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;
    private boolean commandInitialized = false;

    public LoadCoralCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralClaw) {
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralClaw;
    }

    @Override
    public void initialize() {
        System.out.println("Starting LoadCoralCommand");
        algaeAcquirer.setPosition(AlgaeAcquirer.Position.STOWED);
        elevator.setPosition(Elevator.Position.CORAL_LOAD);
        coralCone.setPosition(CoralCone.Position.LOAD);
        commandInitialized = true;
    }

    @Override
    public void execute() {
        if (!commandInitialized) {
            return;
        }
    
        if (coralCone.isAtSetPosition()) {
            coralCone.setWheel(CoralCone.WheelState.LOAD);
        }
    }

    @Override
    public boolean isFinished() {
        boolean finished = elevator.isAtSetPosition() && algaeAcquirer.isAtSetPosition() && coralCone.isAtSetPosition()
                && coralCone.isLoaded();
        if (finished) {
            commandInitialized = false;
        }
        return finished;
    }

    @Override
    public void end(boolean interrupted) {
        coralCone.setWheel(WheelState.TRANSIT);
    }
}
