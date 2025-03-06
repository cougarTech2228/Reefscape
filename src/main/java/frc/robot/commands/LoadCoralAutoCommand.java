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
import frc.robot.util.Enums.CoralStationSide;

public class LoadCoralAutoCommand extends CTSequentialCommandGroup {
    private Destination dest;
    public LoadCoralAutoCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone, Drive drive, CoralStationSide side)
    {
       // = Destination.fromSegmentAndPosition(segment, ReefLocation.L2_R);
        this.addCommands(
            // Wait until we're in the destination zone
            new ParallelRaceGroup(
                new SequentialCommandGroup(
                    new ZoneWatcherCommand(Destination.LOADER_LEFT_CENTER, drive),
                    new InstantCommand(() -> {
                        switch (side) {
                            case Left:
                                dest = Destination.LOADER_LEFT_LEFT;
                                break;
                            case Center:
                                dest = Destination.LOADER_LEFT_CENTER;
                                break;
                            case Right:
                                dest = Destination.LOADER_LEFT_RIGHT;
                                break;
                        }
                    })
                ),
                new SequentialCommandGroup(
                    new ZoneWatcherCommand(Destination.LOADER_RIGHT_CENTER, drive),
                    new InstantCommand(() -> {
                        switch (side) {
                            case Left:
                                dest = Destination.LOADER_RIGHT_LEFT;
                                break;
                            case Center:
                                dest = Destination.LOADER_RIGHT_CENTER;
                                break;
                            case Right:
                                dest = Destination.LOADER_RIGHT_RIGHT;
                                break;
                        }
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
