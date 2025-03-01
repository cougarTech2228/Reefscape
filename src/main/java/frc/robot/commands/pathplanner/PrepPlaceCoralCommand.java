package frc.robot.commands.pathplanner;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.util.Enums.*;

public class PrepPlaceCoralCommand extends Command {

    private final ReefLocation location;
    private final ReefSegment segment;
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;
    private final boolean coralAndAlgae;

    public PrepPlaceCoralCommand( boolean coralAndAlgae,
            ReefSegment segment, ReefLocation location, Elevator elevator,
            AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.segment = segment;
        this.location = location;
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
        this.coralAndAlgae = coralAndAlgae;

        addRequirements(elevator, algaeAcquirer, coralCone);
    }

    public PrepPlaceCoralCommand(
        ReefSegment segment, ReefLocation location, Elevator elevator,
        AlgaeAcquirer algaeAcquirer, CoralCone coralCone)
    {
        this(false, segment, location, elevator, algaeAcquirer, coralCone);
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
        if (coralAndAlgae){
            algaeAcquirer.setPosition(AlgaeAcquirer.Position.REEF_ACQUIRE);
            // algaeAcquirer.setFlywheelState(AlgaeAcquirer.FlywheelState.ACQUIRE);
        } else {
            algaeAcquirer.setPosition(AlgaeAcquirer.Position.STOWED);
        }
    }

    @Override
    public boolean isFinished() {
        boolean finished = elevator.isAtSetPosition() && coralCone.isAtSetPosition() && algaeAcquirer.isAtSetPosition();
        return finished;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted){
            if (interrupted) {
                coralCone.stop();
                algaeAcquirer.stop();
                elevator.stop();

                if (!algaeAcquirer.isLoaded()) {
                    algaeAcquirer.setPosition(AlgaeAcquirer.Position.STOWED);
                }
            }
        }
    }
}