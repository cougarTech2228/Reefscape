package frc.robot.commands;

import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.util.CTSequentialCommandGroup;
import frc.robot.util.Enums.*;

public class LoadAlgaeAutoCommand extends CTSequentialCommandGroup {
    public LoadAlgaeAutoCommand(ReefSegment segment, AlgaeHeight height, Elevator elevator,
         AlgaeAcquirer algaeAcquirer, CoralCone coralCone, Drive drive)
    {
        Destination dest = Destination.fromSegmentAndPosition(segment, ReefLocation.L2_R);
        this.addCommands(
            // Wait until we're in the destination zone
            new ZoneWatcherCommand(dest, drive),

            // Take over driving, and move all components to the right places
            new CTSequentialCommandGroup(
                //AlgaeHeight height, Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone
                new LoadAlgaeCommand(false, height, elevator, algaeAcquirer, coralCone),
                new AutoAlignCommand(drive, dest)
            )
        );
    }
}
