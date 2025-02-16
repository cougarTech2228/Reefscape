package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
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

        // turn in place first, then go to destination
        List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
            new Pose2d(currentPose.getX(), currentPose.getY(), dest.getAngle()),
            dest.getPose());
    
        // constraints are max-v, max-a, max-angv, max-anga
        PathConstraints constraints = new PathConstraints(2.0, 3.0, 2 * Math.PI, 4 * Math.PI);
        PathPlannerPath path = new PathPlannerPath(waypoints, constraints, null,
            new GoalEndState(0.0, dest.getAngle()));
    
        path.preventFlipping = true;
        subCommand = AutoBuilder.followPath(path);
        subCommand.addRequirements(drive);
        
        CommandScheduler.getInstance().schedule(subCommand);
    }
    
    @Override
    public boolean isFinished() {
        return subCommand.isFinished();
    }
}
