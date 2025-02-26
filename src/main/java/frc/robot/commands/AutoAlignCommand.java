package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.drive.Drive;

import java.util.List;

import com.pathplanner.lib.auto.AutoBuilder;
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
        DriverStation.Alliance all = DriverStation.getAlliance().get();

        // turn in place first, then go to destination
        // since the whole path gets mirrored for red, we need to pre-un-mirror our
        // in-place pose
        Pose2d turnPose;
        if (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue)
            turnPose = new Pose2d(currentPose.getX(), currentPose.getY(), dest.getAngle(all));
        else
            turnPose = new Pose2d(17.55 - currentPose.getX(), 8.05 - currentPose.getY(), dest.getAngle(all));

        List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
                turnPose, dest.getPose(all));

        // constraints are max-v, max-a, max-angv, max-anga
        PathConstraints constraints = new PathConstraints(1.0, 1.0, 1 * Math.PI, 2 * Math.PI);
        PathPlannerPath path = new PathPlannerPath(waypoints, constraints, null,
                new GoalEndState(0.0, dest.getAngle(all)));

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
