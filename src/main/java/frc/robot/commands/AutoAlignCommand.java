package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
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
    private final boolean useApproachPoint;
    private Pose2d destPoint;

    // constraints are max-v, max-a, max-angv, max-anga
    private PathConstraints globalConstraints = new PathConstraints(2, 2.0, 2 * Math.PI, 3 * Math.PI);
    private PathConstraints endingConstraints = new PathConstraints(1, 0.5, 2 * Math.PI, 3 * Math.PI);
    private ArrayList<ConstraintsZone> constraintZones;

    public AutoAlignCommand(Drive drive, Destination dest, boolean useApproachPoint) {
        this.drive = drive;
        this.dest = dest;
        this.useApproachPoint = useApproachPoint;

        constraintZones = new ArrayList<>();
        constraintZones.add(new ConstraintsZone(0.75, 1, endingConstraints));
    }

    public AutoAlignCommand(Drive drive, Destination dest) {
        this(drive, dest, true);
    }

    public AutoAlignCommand(Drive drive, Pose2d dest, PathConstraints globalConstraints) {
        this.drive = drive;
        this.destPoint = dest;
        useApproachPoint = false;
        this.dest = null;
        this.globalConstraints = globalConstraints;
        constraintZones = new ArrayList<>();
    }

    @Override
    public void initialize() {
        Pose2d currentPose = drive.getPose();
        DriverStation.Alliance alliance = DriverStation.Alliance.Blue;// DriverStation.getAlliance().get();
        PathPlannerPath path;
        if (dest != null) {
            // turn in place first, then go to destination
            // since the whole path gets mirrored for red, we need to pre-un-mirror our
            // in-place pose
            Pose2d turnPose;
            if (DriverStation.getAlliance().get() == DriverStation.Alliance.Blue)
                turnPose = new Pose2d(currentPose.getX(), currentPose.getY(), dest.getAngle(alliance));
            else
                turnPose = new Pose2d(17.55 - currentPose.getX(), 8.05 - currentPose.getY(), dest.getAngle(alliance));

            List<Waypoint> waypoints;
            
            if (useApproachPoint) {
                waypoints = PathPlannerPath.waypointsFromPoses(
                    turnPose, 
                    dest.getApproachPose(),
                    dest.getPose());
            } else {
                waypoints = PathPlannerPath.waypointsFromPoses(
                    turnPose,
                    dest.getPose());
            }
            
            ArrayList<ConstraintsZone> constraintZones = new ArrayList<>();
            constraintZones.add(new ConstraintsZone(0.5, 1, endingConstraints));

            path = new PathPlannerPath(
                waypoints,
                new ArrayList<>(), // holonomicRotations,
                new ArrayList<>(), // pointTowardsZones,
                constraintZones,
                new ArrayList<>(), // eventMarkers,
                globalConstraints,
                null, // idealStartingState,
                new GoalEndState(0.0, dest.getAngle(alliance)),
                false); // reversed
            subCommand = AutoBuilder.followPath(path);
        } else {
            // List<Waypoint> waypoints;
            // //return pose.transformBy(new Transform2d(-Constants.robotLength/2, 0, new Rotation2d()));
            // Pose2d approchPoint = destPoint.transformBy(
            //     new Transform2d(-Constants.robotLength/2, 0, new Rotation2d()));
            // if (useApproachPoint) {
            //     waypoints = PathPlannerPath.waypointsFromPoses(
            //         approchPoint,
            //         destPoint);
            // } else {
            //     waypoints = PathPlannerPath.waypointsFromPoses(
            //         currentPose,
            //         new Pose2d(currentPose.getX(), currentPose.getY(), destPoint.getRotation()),
            //         destPoint);
            // }

            // path = new PathPlannerPath(
            //     waypoints,
            //     new ArrayList<>(), // holonomicRotations,
            //     new ArrayList<>(), // pointTowardsZones,
            //     constraintZones,
            //     new ArrayList<>(), // eventMarkers,
            //     globalConstraints,
            //     null, // idealStartingState,
            //     new GoalEndState(0.0, destPoint.getRotation()),
            //     false); // reversed
            subCommand = AutoBuilder.pathfindToPose(destPoint, globalConstraints);
        }

        // path.preventFlipping = true;

        subCommand.addRequirements(drive);

        drive.setIsAutoDriving(true);
        CommandScheduler.getInstance().schedule(subCommand);
    }

    @Override
    public boolean isFinished() {
        if(subCommand.isFinished()){
            drive.setIsAutoDriving(false);
            return true;
        }

        return false;
    }
}
