package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.pathplanner.PrepPlaceCoralCommand;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.util.Enums.*;

public class PlaceCoralCommand extends SequentialCommandGroup {
    public PlaceCoralCommand(boolean coralAndAlgae, ReefSegment segment, ReefLocation location,
                             Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone, Drive drive)
    {
        Destination dest = Destination.fromSegmentAndPosition(segment, location, DriverStation.getAlliance().get());
        this.addCommands(
            // Wait until we're in the destination zone
            new ZoneWatcherCommand(dest, drive),

            // Take over driving, and move all components to the right places
            new ParallelCommandGroup(
                new PrepPlaceCoralCommand(coralAndAlgae, segment, location, elevator, algaeAcquirer, coralCone),
                new AutoAlignCommand(drive, dest)
            ),
            // once we're in the right place, shoot the coral
            new FireCoralCommand(coralCone, false)
        );
        if (coralAndAlgae){
            // After the coral fires, then turn on the algae flywheels, giving it 1 se
            this.addCommands(new InstantCommand(() -> {
                algaeAcquirer.setFlywheelState(AlgaeAcquirer.FlywheelState.ACQUIRE);
            }));
        } else {
            // if we're not also loading an algae, collapse things
            this.addCommands(
                // Once we've shot, go back to a safe state to transit
                new CollapseCommand( elevator, algaeAcquirer, coralCone)
            );
        }
    }

    public PlaceCoralCommand(ReefSegment segment, ReefLocation location, Elevator elevator,
                             AlgaeAcquirer algaeAquirer, CoralCone coralCone, Drive drive)
    {
        this(false, segment, location, elevator, algaeAquirer, coralCone, drive);
    }
}
