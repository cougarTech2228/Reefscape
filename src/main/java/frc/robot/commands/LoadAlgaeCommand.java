package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.FlywheelState;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.util.Enums.AlgaeHeight;

public class LoadAlgaeCommand extends Command{
    private final AlgaeHeight height;
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;
    private AlgaeAcquirer.Position anglePostition;

    private boolean commandInitialized = false;
    private final boolean finishOnLoaded;
    private boolean flywheelsOn = false;

    public LoadAlgaeCommand(boolean finishOnLoaded, AlgaeHeight height, Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.height = height;
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
        this.finishOnLoaded = finishOnLoaded;
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
                elevator.setPosition(Elevator.Position.ALGAE_REEF_HIGH);
                break;            
        }
        coralCone.setPosition(CoralCone.Position.STOWED);
        algaeAcquirer.setPosition(anglePostition);
        commandInitialized = true;
    }
    
    @Override
    public void execute() {
        if (!commandInitialized) {
            return;
        }

        if (algaeAcquirer.isAtSetPosition()) {
            algaeAcquirer.setFlywheelState(FlywheelState.ACQUIRE);
            flywheelsOn = true;
        }
    }

    @Override
    public boolean isFinished() {
        if (finishOnLoaded) {
            return algaeAcquirer.isLoaded();
        } else {
            return algaeAcquirer.isAtSetPosition() && elevator.isAtSetPosition() && flywheelsOn;
        }
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
