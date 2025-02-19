package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.FlywheelState;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class LoadAlgaeCommand extends Command{

    public enum AlgaeHeight {
        Floor,
        FloorOnCoral,
        REEF_LOW,
        REEF_HIGH
    }

    private final AlgaeHeight height;
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;
    private AlgaeAcquirer.Position anglePostition;
    private boolean angleSet = false;

    private boolean commandInitialized = false;

    public LoadAlgaeCommand(AlgaeHeight height, Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.height = height;
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
    }

    @Override
    public void initialize() {
        System.out.println("Starting LoadAlgaeCommand height: " + height);
        switch (height){
            case Floor:
                anglePostition = AlgaeAcquirer.Position.FLOOR_ACQUIRE;
                elevator.setPosition(Elevator.Position.ALGAE_FLOOR);
                break;
            case FloorOnCoral:
                anglePostition = AlgaeAcquirer.Position.FLOOR_ACQUIRE;
                elevator.setPosition(Elevator.Position.ALGAE_FLOOR_ON_CORAL);
                break;
            case REEF_LOW:
                anglePostition = AlgaeAcquirer.Position.REEF_ACQUIRE;
                elevator.setPosition(Elevator.Position.ALGAE_REEF_LOW);
                break;
            case REEF_HIGH:
                anglePostition = AlgaeAcquirer.Position.REEF_ACQUIRE;
                elevator.setPosition(Elevator.Position.ALGAE_REEF_LOW);
                break;            
        }
        coralCone.setPosition(CoralCone.Position.STOWED);
        commandInitialized = true;
    }
    
    @Override
    public void execute() {
        if (!commandInitialized) {
            return;
        }

        if (!angleSet && elevator.isAtSetPosition()) {
            angleSet = true;
            algaeAcquirer.setPosition(anglePostition);
        }

        if (algaeAcquirer.isAtSetPosition()) {
            algaeAcquirer.setFlywheelState(FlywheelState.ACQUIRE);
        }
    }

    @Override
    public boolean isFinished() {
        boolean finished = algaeAcquirer.isLoaded();
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
