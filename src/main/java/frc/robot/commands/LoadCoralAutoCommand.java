package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.util.CTSequentialCommandGroup;

public class LoadCoralAutoCommand extends CTSequentialCommandGroup {
    private Destination dest;
    public LoadCoralAutoCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone, Drive drive)
    {
       // = Destination.fromSegmentAndPosition(segment, ReefLocation.L2_R);
        this.addCommands(
            // Wait until we're in the destination zone
            new ParallelRaceGroup(
                new SequentialCommandGroup(
                    new ZoneWatcherCommand(Destination.LOADER_LEFT, drive),
                    new InstantCommand(() -> {
                        dest = Destination.LOADER_LEFT;
                    })
                ),
                new SequentialCommandGroup(
                    new ZoneWatcherCommand(Destination.LOADER_RIGHT, drive),
                    new InstantCommand(() -> {
                        dest = Destination.LOADER_RIGHT;
                    })
                )
            ),

            // Take over driving, and move all components to the right places
            new ParallelCommandGroup(
                new LoadCoralCommand(elevator, algaeAcquirer, coralCone),
                new InstantCommand(() -> {
                    CommandScheduler.getInstance().schedule(new AutoAlignCommand(drive, dest));
                })
            )
        );
    }
}
