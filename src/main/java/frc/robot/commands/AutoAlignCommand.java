package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.drive.Drive;

import java.util.ArrayList;
import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.ConstraintsZone;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

public class AutoAlignCommand extends Command {

    private final Drive drive;
    private final Destination dest;

    private Command subCommand;

    public AutoAlignCommand(Drive drive, Destination dest) {
        this.drive = drive;
        this.dest = dest;
    }

    @Override
    public void initialize() {
        Pose2d currentPose = drive.getPose();
        DriverStation.Alliance alliance = DriverStation.Alliance.Blue;// DriverStation.getAlliance().get();

        // turn in place first, then go to destination
        // since the whole path gets mirrored for red, we need to pre-un-mirror our
        // in-place pose
        Pose2d turnPose;
        if (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue)
            turnPose = new Pose2d(currentPose.getX(), currentPose.getY(), dest.getAngle(alliance));
        else
            turnPose = new Pose2d(17.55 - currentPose.getX(), 8.05 - currentPose.getY(), dest.getAngle(alliance));

        List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
                turnPose, dest.getPose(alliance));

        // constraints are max-v, max-a, max-angv, max-anga
        PathConstraints globalConstraints = new PathConstraints(2, 2.0, 2 * Math.PI, 3 * Math.PI);
        PathConstraints endingConstraints = new PathConstraints(2, 1.0, 2 * Math.PI, 3 * Math.PI);

        ArrayList<ConstraintsZone> constraintZones = new ArrayList<>();
        constraintZones.add(new ConstraintsZone(0.75, 1, endingConstraints));

        PathPlannerPath path = new PathPlannerPath(
            waypoints,
            new ArrayList<>(), // holonomicRotations,
            new ArrayList<>(), // pointTowardsZones,
            constraintZones,
            new ArrayList<>(), // eventMarkers,
            globalConstraints,
            null, // idealStartingState,
            new GoalEndState(0.0, dest.getAngle(alliance)),
            false); // reversed

        // path.preventFlipping = true;
        subCommand = AutoBuilder.followPath(path);
        subCommand.addRequirements(drive);

        CommandScheduler.getInstance().schedule(subCommand);
    }

    @Override
    public boolean isFinished() {
        return subCommand.isFinished();
    }
}
