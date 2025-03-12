package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.pathplanner.PrepPlaceCoralCommand;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.util.CTSequentialCommandGroup;
import frc.robot.util.Enums.*;

public class PlaceCoralCommand extends CTSequentialCommandGroup {
    public PlaceCoralCommand(boolean coralAndAlgae, ReefSegment segment, ReefLocation location,
                             Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone, Drive drive)
    {
        Destination dest = Destination.fromSegmentAndPosition(segment, location);
        this.addCommands(
            // Wait until we're in the destination zone
            new ZoneWatcherCommand(dest, drive),

            // Take over driving, and move all components to the right places
            new PrepPlaceCoralCommand(coralAndAlgae, segment, location, elevator, algaeAcquirer, coralCone));
        if (coralAndAlgae){
            // After the coral fires, then turn on the algae flywheels, giving it 1 se
            this.addCommands(new InstantCommand(() -> {
                algaeAcquirer.setFlywheelState(AlgaeAcquirer.FlywheelState.ACQUIRE);
            }));
        }

        this.addCommands(    // once we're in the right place, shoot the coral
            new AutoAlignCommand(drive, dest),
            new WaitCommand(0.25),
            new FireCoralCommand(coralCone, false)
        );
        if (location.equals(ReefLocation.L2_L) || location.equals(ReefLocation.L2_R)) {
            if (!coralAndAlgae){
                // if we're not also loading an algae, collapse things
                this.addCommands(
                    // Once we've shot, go back to a safe state to transit
                    new CollapseCommand(elevator, algaeAcquirer, coralCone, Elevator.Position.CORAL_LOAD)
                );
            }
        } else if (location.equals(ReefLocation.L1)) {
            this.addCommands(
                // Once we've shot, go back to a safe state to transit
                new CollapseCommand( elevator, algaeAcquirer, coralCone, Elevator.Position.CORAL_LOAD)
            );
        } else {
            if (!coralAndAlgae){
                // if we're not also loading an algae, collapse things
                this.addCommands(
                    // Once we've shot, go back to a safe state to transit
                    new CollapseCommand(elevator, algaeAcquirer, coralCone, Elevator.Position.CORAL_LOAD)
                );
            }
        }
    }

    public PlaceCoralCommand(ReefSegment segment, ReefLocation location, Elevator elevator,
                             AlgaeAcquirer algaeAquirer, CoralCone coralCone, Drive drive)
    {
        this(false, segment, location, elevator, algaeAquirer, coralCone, drive);
    }
}
