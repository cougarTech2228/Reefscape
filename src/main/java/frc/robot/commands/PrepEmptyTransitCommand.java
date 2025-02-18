package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class PrepEmptyTransitCommand extends SequentialCommandGroup {

    public PrepEmptyTransitCommand(Elevator elevator, CoralCone coralCone, AlgaeAcquirer algaeAcquirer){

        addRequirements(elevator);
        addCommands(
            new SetCoralAngleCommand(coralCone, CoralCone.Position.STOWED),
            new SetAlgaeAngleCommand(algaeAcquirer, AlgaeAcquirer.Position.STOWED),
            new InstantCommand(() -> {
                //elevator.stop();
                elevator.setPosition(Elevator.Position.TRANSIT);
            })
        );
    }
}
