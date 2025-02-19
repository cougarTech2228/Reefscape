package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.FlywheelState;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class LoadAlgaeAutoCommand extends Command{

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

    public LoadAlgaeAutoCommand(AlgaeHeight height, Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.height = height;
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
    }

    @Override
    public void initialize() {
        System.out.println("Starting LoadAlgaeCommand height: " + height);
        commandInitialized = true;
    }
    
    @Override
    public void execute() {
        if (!commandInitialized) {
            return;
        }
        algaeAcquirer.setFlywheelState(FlywheelState.ACQUIRE);
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
