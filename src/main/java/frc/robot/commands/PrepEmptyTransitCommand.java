package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class PrepEmptyTransitCommand extends Command {
    
    private final Elevator elevator;
    private final CoralCone coralCone;
    private final AlgaeAcquirer algaeAcquirer;

    public PrepEmptyTransitCommand(Elevator elevator, CoralCone coralCone, AlgaeAcquirer algaeAcquirer){
        this.elevator = elevator;
        this.coralCone = coralCone;
        this.algaeAcquirer = algaeAcquirer;
        addRequirements(elevator, algaeAcquirer, coralCone);
    }

    @Override
    public void initialize() {
        elevator.setPosition(Elevator.Position.TRANSIT);
        coralCone.setPosition(CoralCone.Position.STOWED);
        algaeAcquirer.setPosition(AlgaeAcquirer.Position.STOWED);
    }

    @Override
    public boolean isFinished() {
        return elevator.isAtSetPosition() && coralCone.isAtSetPosition() && algaeAcquirer.isAtSetPosition();
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
