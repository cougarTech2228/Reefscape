package frc.robot.commands.pathplanner;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class PrepProcessorCommand extends Command {
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;

    private boolean commandInitialized = false;

    public PrepProcessorCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
    }

    @Override
    public void initialize() {
        System.out.println("Starting PrepProcessorCommand");
        algaeAcquirer.setPosition(AlgaeAcquirer.Position.PROCESSOR_SHOOT);
        elevator.setPosition(Elevator.Position.ALGAE_PROCESSOR);
        coralCone.setPosition(CoralCone.Position.STOWED);
        commandInitialized = true;
    }

    @Override
    public void execute() {
        if (!commandInitialized) {
            return;
        }

        if (algaeAcquirer.isAtSetPosition()) {
            // algaeAcquirer.setFlywheelState(AlgaeAcquirer.FlywheelState.SHOOT);
        }
    }

    @Override
    public boolean isFinished() {
        boolean finished = elevator.isAtSetPosition() && algaeAcquirer.isAtSetPosition() && coralCone.isAtSetPosition();
        // && !algaeAcquirer.isLoaded();
        if (finished) {
            commandInitialized = false;
        }
        return finished;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted){
            if (interrupted) {
                coralCone.stop();
                algaeAcquirer.stop();
                elevator.stop();
            }
        }
    }
}