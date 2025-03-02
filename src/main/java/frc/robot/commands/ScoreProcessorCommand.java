package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.util.CTSequentialCommandGroup;

public class ScoreProcessorCommand extends CTSequentialCommandGroup {
    public ScoreProcessorCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone, Drive drive)
    {
        //Destination dest = Destination.fromSegmentAndPosition(segment, location, DriverStation.getAlliance().get());
        this.addCommands(
            // Wait until we're in the destination zone
            new ZoneWatcherCommand(Destination.PROCESSOR, drive),

            // Take over driving, and move all components to the right places
            new ParallelCommandGroup(
                new PrepProcessorCommand(elevator, algaeAcquirer, coralCone),
                new AutoAlignCommand(drive, Destination.PROCESSOR)
            ),
            // once we're in the right place, shoot the coral
            new FireAlgaeCommand(algaeAcquirer),
            new CollapseCommand( elevator, algaeAcquirer, coralCone)
        );
    }
}
