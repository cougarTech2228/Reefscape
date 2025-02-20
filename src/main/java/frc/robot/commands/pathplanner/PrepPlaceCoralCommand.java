package frc.robot.commands.pathplanner;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ReefLocation;
import frc.robot.Constants.ReefSegment;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class PrepPlaceCoralCommand extends Command {

    private final ReefLocation location;
    private final ReefSegment segment;
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;

    private boolean commandInitialized = false;

    public PrepPlaceCoralCommand(
            ReefSegment segment, ReefLocation location, Elevator elevator,
            AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.segment = segment;
        this.location = location;
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;

        addRequirements(elevator, algaeAcquirer, coralCone);
    }

    @Override
    public void initialize() {
        System.out.println("Starting PrepPlaceCoralCommand segment: " + segment
                + ", location: " + location);
        switch (location) {
            case L1:
                elevator.setPosition(Elevator.Position.CORAL_L1);
                coralCone.setPosition(CoralCone.Position.L1_SHOOT);
                break;
            case L2_L:
                elevator.setPosition(Elevator.Position.CORAL_L2);
                coralCone.setPosition(CoralCone.Position.L2_SHOOT);
            case L2_R:
                elevator.setPosition(Elevator.Position.CORAL_L2);
                coralCone.setPosition(CoralCone.Position.L2_SHOOT);
                break;
            case L3_L:
                elevator.setPosition(Elevator.Position.CORAL_L3);
                coralCone.setPosition(CoralCone.Position.L3_SHOOT);
            case L3_R:
                elevator.setPosition(Elevator.Position.CORAL_L3);
                coralCone.setPosition(CoralCone.Position.L3_SHOOT);
                break;
            case L4_L:
                elevator.setPosition(Elevator.Position.CORAL_L4);
                coralCone.setPosition(CoralCone.Position.L4_SHOOT);
            case L4_R:
                elevator.setPosition(Elevator.Position.CORAL_L4);
                coralCone.setPosition(CoralCone.Position.L4_SHOOT);
                break;
        }
        algaeAcquirer.setPosition(AlgaeAcquirer.Position.STOWED);
        commandInitialized = true;
    }

    @Override
    public void execute() {
        if (!commandInitialized) {
            return;
        }

        if (coralCone.isAtSetPosition()) {
            // coralCone.setWheel(CoralCone.WheelState.SHOOT);
        }
    }

    @Override
    public boolean isFinished() {
        boolean finished = elevator.isAtSetPosition() && coralCone.isAtSetPosition() && algaeAcquirer.isAtSetPosition();
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