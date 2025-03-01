package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class BargeCommand extends Command {
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;

    private boolean commandInitialized = false;
    private boolean elevatorPositionSet = false;
    private boolean algaePositionSet = false;

    public BargeCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
    }

    @Override
    public void initialize() {
        System.out.println("Starting BargeCommand");
        algaeAcquirer.setPosition(AlgaeAcquirer.Position.PROCESSOR_SHOOT);
        coralCone.setPosition(CoralCone.Position.LOAD);
        commandInitialized = true;
    }

    @Override
    public void execute() {
        if (!commandInitialized) {
            return;
        }

        if (elevatorPositionSet && elevator.isAtSetPosition()){
            coralCone.setPosition(CoralCone.Position.STOWED);
            algaeAcquirer.setPosition(AlgaeAcquirer.Position.BARGE_SHOOT);
            algaePositionSet = true;
        }

        if (algaeAcquirer.isAtSetPosition() && coralCone.isAtSetPosition()) {
            // algae and coral are in a safe location to extend the elevator
            elevator.setPosition(Elevator.Position.ALGAE_BARGE);
            elevatorPositionSet = true;
        }
    }

    @Override
    public boolean isFinished() {
        boolean finished = algaePositionSet &&
            elevator.isAtSetPosition() &&
            algaeAcquirer.isAtSetPosition() &&
            coralCone.isAtSetPosition();
        if (finished) {
            System.out.println("Barge Finished");
        }
        return finished;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted){
            if (interrupted) {
                coralCone.setPosition(CoralCone.Position.LOAD);
                algaeAcquirer.setPosition(AlgaeAcquirer.Position.FLOOR_ACQUIRE);
                elevator.stop();
            }
        }
    }
}
