package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.util.Enums.*;

import java.util.ArrayList;

import static frc.robot.subsystems.vision.VisionConstants.aprilTagLayout;

/**
 *  We have to declare these in a class if we want to use them in the constructor of Destination objects
 *  Thanks Java....
 */
class DestConsts {
    static double loaderCenterOffset = -0.125;
    static double loaderRightOffset = loaderCenterOffset + 0.6096;
    static double loaderLeftOffset = loaderCenterOffset - 0.6096;

    static double reefLeftOffset = -0.33;

    static double angleTolerance = 60; // how close to destination angle to be in zone (degrees)
}

/**
 * Destinations for short autonomous paths
 */
public enum Destination {
    // All defined here relative to Blue. All functions below will mirror as needed.
    REEF_1_LEFT (18, DestConsts.reefLeftOffset, 0,  0,   9,   0,  -1,  2.8, 3,  2.8, 5),
    REEF_1_RIGHT(18, 0, 0, 0,   9,   0,  -1,  2.8, 3,  2.8, 5),
    REEF_2_LEFT (17, DestConsts.reefLeftOffset, 0,  2.7, 2.9, -1,  1,  5.5, -2, 4.5, 2.2),
    REEF_2_RIGHT(17, 0, 0, 2.7, 2.9, -1,  1,  5.5, -2, 4.5, 2.2),
    REEF_3_LEFT (22, DestConsts.reefLeftOffset, 0,  4.5, 2.2, 3.5, -2, 10,  1,  6.2, 3),
    REEF_3_RIGHT(22, 0, 0, 4.5, 2.2, 3.5, -2, 10,  1,  6.2, 3),
    REEF_4_LEFT (21, DestConsts.reefLeftOffset, 0,  9,  -1,   9,   9,  6.2, 5,  6.2, 3),
    REEF_4_RIGHT(21, 0, 0, 9,  -1,   9,   9,  6.2, 5,  6.2, 3),
    REEF_5_LEFT (20, DestConsts.reefLeftOffset, 0,  4.5, 5.8, 3.5, 10, 10,  7,  6.2, 5),
    REEF_5_RIGHT(20, 0, 0, 4.5, 5.8, 3.5, 10, 10,  7,  6.2, 5),
    REEF_6_LEFT (19, DestConsts.reefLeftOffset, 0,  2.7, 5.1, -1,  7,  5.5, 10, 4.5, 5.8),
    REEF_6_RIGHT(19, 0, 0, 2.7, 5.1, -1,  7,  5.5, 10, 4.5, 5.8),

    PROCESSOR(16, 0, 0.1, 6.5, 0.55, 7.3, 2, 5, 2, 5.6, 0.55),

    LOADER_RIGHT_LEFT(12, DestConsts.loaderLeftOffset, 0, 1.7, 0.55, 2.9, 1.3, 1.2, 2.6, 0.1, 1.2),
    LOADER_RIGHT_CENTER(12, DestConsts.loaderCenterOffset, 0, 1.7, 0.55, 2.9, 1.3, 1.2, 2.6, 0.1, 1.2),
    LOADER_RIGHT_RIGHT(12, DestConsts.loaderRightOffset, 0, 1.7, 0.55, 2.9, 1.3, 1.2, 2.6, 0.1, 1.2),

    LOADER_LEFT_LEFT(13, DestConsts.loaderLeftOffset, 0, 0.3, 6.8, 1, 5, 3, 6.5, 1.7, 8),
    LOADER_LEFT_CENTER(13, DestConsts.loaderCenterOffset, 0, 0.3, 6.8, 1, 5, 3, 6.5, 1.7, 8),
    LOADER_LEFT_RIGHT(13, DestConsts.loaderRightOffset, 0, 0.3, 6.8, 1, 5, 3, 6.5, 1.7, 8),

    BARGE (14, 0, 0, 8.2, 7.4, 6.5, 7.4, 6.5, 4.8, 8.2, 4.8);

    public static Destination fromSegmentAndPosition(ReefSegment segment, ReefLocation location) {
        Destination destination = null;
        switch (segment) {
            case Segment_1:
                switch (location) {
                    case L2_L:
                    case L3_L:
                    case L4_L:
                        destination = REEF_1_LEFT;
                        break;

                    case L1:
                    case L2_R:
                    case L3_R:
                    case L4_R:
                        destination = REEF_1_RIGHT;
                        break;
                }
                break;
            case Segment_2:
                switch (location) {
                    case L2_L:
                    case L3_L:
                    case L4_L:
                        destination = REEF_2_LEFT;
                        break;

                    case L1:
                    case L2_R:
                    case L3_R:
                    case L4_R:
                        destination = REEF_2_RIGHT;
                        break;
                }
                break;
            case Segment_3:
                switch (location) {
                    case L2_L:
                    case L3_L:
                    case L4_L:
                        destination = REEF_3_LEFT;
                        break;

                    case L1:
                    case L2_R:
                    case L3_R:
                    case L4_R:
                        destination = REEF_3_RIGHT;
                        break;
                }
                break;
            case Segment_4:
                switch (location) {
                    case L2_L:
                    case L3_L:
                    case L4_L:
                        destination = REEF_4_LEFT;
                        break;

                    case L1:
                    case L2_R:
                    case L3_R:
                    case L4_R:
                        destination = REEF_4_RIGHT;
                        break;
                }
                break;
            case Segment_5:
                switch (location) {
                    case L2_L:
                    case L3_L:
                    case L4_L:
                        destination = REEF_5_LEFT;
                        break;

                    case L1:
                    case L2_R:
                    case L3_R:
                    case L4_R:
                        destination = REEF_5_RIGHT;
                        break;
                }
                break;
            case Segment_6:
                switch (location) {
                    case L2_L:
                    case L3_L:
                    case L4_L:
                        destination = REEF_6_LEFT;
                        break;

                    case L1:
                    case L2_R:
                    case L3_R:
                    case L4_R:
                        destination = REEF_6_RIGHT;
                        break;
                }
                break;
        }
        return destination;
    }

    // x, y, angle of the destination
    private final Pose2d pose;
    private final Pose2d tagPose;

    // vertices of the trapezoid in which the destination is possible
    private final ArrayList<Translation2d> zone;
    private final double yShift;
    private final double xShift;

    private Destination(double x, double y, double angle, double p1x, double p1y, double p2x, double p2y, double p3x,
            double p3y, double p4x, double p4y) {
        this.pose = new Pose2d(x, y, Rotation2d.fromDegrees(angle));
        this.tagPose = pose;
        this.zone = new ArrayList<>();
        zone.add(new Translation2d(p1x, p1y));
        zone.add(new Translation2d(p2x, p2y));
        zone.add(new Translation2d(p3x, p3y));
        zone.add(new Translation2d(p4x, p4y));
        yShift = 0;
        xShift = 0;
    }

    private Destination(int tagID, double yShift, double xShift, double p1x, double p1y, double p2x, double p2y, double p3x,
    double p3y, double p4x, double p4y) {
        tagPose = aprilTagLayout.getTagPose(tagID).get().toPose2d();
        this.pose = tagPose.transformBy(new Transform2d(Constants.robotLength/2 + xShift, yShift, new Rotation2d(Math.PI)));
        this.zone = new ArrayList<>();
        zone.add(new Translation2d(p1x, p1y));
        zone.add(new Translation2d(p2x, p2y));
        zone.add(new Translation2d(p3x, p3y));
        zone.add(new Translation2d(p4x, p4y));
        this.yShift = yShift;
        this.xShift = xShift;
    }

    public Rotation2d getAngle(DriverStation.Alliance alliance) {
        return alliance == DriverStation.Alliance.Blue ? pose.getRotation()
                : pose.getRotation().plus(Rotation2d.k180deg);
    }

    public Pose2d getPose() {
        return pose;
    }

    public Pose2d getApproachPose() {
        // return pose.transformBy(new Transform2d(-Constants.robotLength/2, xShift, new Rotation2d()));
        return tagPose.transformBy(new Transform2d(Constants.robotLength + xShift, yShift, new Rotation2d(Math.PI)));
    }

    public boolean inZone(Pose2d pos, DriverStation.Alliance alliance) {
        // if the robot is more than 90 degrees away from the right angle, it's not in the zone.
        double destAngle = getAngle(alliance).getDegrees();
        double robAngle = pos.getRotation().getDegrees();
        double angleDiff = (robAngle-destAngle+720) % 360; //sorry
        if (angleDiff > DestConsts.angleTolerance && angleDiff < 360-DestConsts.angleTolerance)
            return false;
        
        // for the given pose, is it in the trapezoid?
        // n>2 Keep track of cross product sign changes
        int pve = 0;
        int neg = 0;

        double x = pos.getX();
        double y = pos.getY();

        for (int i = 0; i < 4; i++) {
            // Form a segment between the i'th point
            double x1 = zone.get(i).getX();
            if (alliance == DriverStation.Alliance.Red)
                x1 = 17.55 - x1;
            double y1 = zone.get(i).getY();
            if (alliance == DriverStation.Alliance.Red)
                y1 = 8.05 - y1;

            // And the i+1'th, or if i is the last, with the first point
            int i2 = (i + 1) % 4;

            double x2 = zone.get(i2).getX();
            if (alliance == DriverStation.Alliance.Red)
                x2 = 17.55 - x2;
            double y2 = zone.get(i2).getY();
            if (alliance == DriverStation.Alliance.Red)
                y2 = 8.05 - y2;

            // Compute the cross product
            double d = (x - x1) * (y2 - y1) - (y - y1) * (x2 - x1);

            if (d > 0)
                pve++;
            if (d < 0)
                neg++;

            // If the sign changes, then point is outside
            if (pve > 0 && neg > 0)
                return false;
        }

        // If no change in direction, then on same side of all segments, and thus inside
        return true;
    }
}
