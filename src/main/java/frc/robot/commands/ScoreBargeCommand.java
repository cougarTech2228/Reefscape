package frc.robot.commands;

import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.util.CTSequentialCommandGroup;

public class ScoreBargeCommand extends CTSequentialCommandGroup {
    public ScoreBargeCommand(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone, Drive drive)
    {
        Destination barge = Destination.BARGE;
        // when the elevator is all the way up, we need to be SUPER slow!
        PathConstraints pathConstraints = new PathConstraints(1.5, 1, 2 * Math.PI, 3 * Math.PI);

        //Destination dest = Destination.fromSegmentAndPosition(segment, location, DriverStation.getAlliance().get());
        this.addCommands(
            // Wait until we're in the destination zone
            new ZoneWatcherCommand(barge, drive),
            new BargeCommand(elevator, algaeAcquirer, coralCone),

            
            new AutoAlignCommand(drive, new Pose2d(
                7.48,
                drive.getPose().getY(),
                barge.getPose().getRotation()),
                pathConstraints),
            // once we're in the right place, shoot the coral
            new FireAlgaeCommand(algaeAcquirer),
            new WaitCommand(0.5),
            new CollapseCommand( elevator, algaeAcquirer, coralCone)
        );
    }
}
