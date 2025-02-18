package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.ReefLocation;
import frc.robot.Constants.ReefSegment;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.elevator.Elevator;

public class PlaceCoralCommand extends SequentialCommandGroup {
    public PlaceCoralCommand(ReefSegment segment, ReefLocation location, Elevator elevator, AlgaeAcquirer algaeAquirer, CoralCone coralCone, Drive drive) {
        Destination dest =Destination.fromSegmentAndPosition(segment, location, DriverStation.getAlliance().get());
        this.addCommands(
            // Wait until we're in the destination zone
            new ZoneWatcherCommand(dest, drive),

            // Take over driving, and move all components to the right places
            new ParallelCommandGroup(
                new PrepPlaceCoralCommand(segment, location, elevator, algaeAquirer, coralCone),
                new AutoAlignCommand(drive, dest)
            ),
            // once we're in the right place, shoot the coral
            new FireCoralCommand(coralCone),

            // Once we've shot, go back to a safe state to transit
            new PrepEmptyTransitCommand( elevator, coralCone, algaeAquirer)
        );
    }
}
