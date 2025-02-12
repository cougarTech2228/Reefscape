package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class BargeCommand extends Command{
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;

    private boolean commandInitialized = false;

    public BargeCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone) {
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
    }

    @Override
    public void initialize() {

        commandInitialized = true;
    }

    @Override
    public void execute() {
        if (!commandInitialized){
            return;
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
