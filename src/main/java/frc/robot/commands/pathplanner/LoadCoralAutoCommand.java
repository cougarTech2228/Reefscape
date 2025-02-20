package frc.robot.commands.pathplanner;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.coralCone.CoralCone.WheelState;
import frc.robot.subsystems.elevator.Elevator;

public class LoadCoralAutoCommand extends Command {
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;
    private boolean commandInitialized = false;

    public LoadCoralAutoCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralClaw) {
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralClaw;
    }

    @Override
    public void initialize() {
        System.out.println("Starting LoadCoralAutoCommand");
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
        boolean finished = coralCone.isLoaded();
        if (finished) {
            commandInitialized = false;
        }
        return finished;
    }

    @Override
    public void end(boolean interrupted) {
        coralCone.setWheel(WheelState.TRANSIT);
        if (interrupted) {
            coralCone.stop();
            algaeAcquirer.stop();
            elevator.stop();
        }
    }
}
