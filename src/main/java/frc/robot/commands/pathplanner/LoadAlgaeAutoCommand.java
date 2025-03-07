package frc.robot.commands.pathplanner;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.FlywheelState;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class LoadAlgaeAutoCommand extends Command{
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;
    private boolean commandInitialized = false;

    public LoadAlgaeAutoCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
    }

    @Override
    public void initialize() {
        System.out.println("Starting LoadAlgaeAutoCommand");
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
