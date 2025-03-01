package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.util.Enums.*;

import java.util.ArrayList;

/**
 * Destinations for short autonomous paths
 */
public enum Destination {
    // All defined here relative to Blue. All functions below will mirror as needed.
    REEF_1_LEFT(3.15, 4.36, 0, 0, 9, 0, -1, 2.8, 3, 2.8, 5),
    REEF_1_RIGHT(3.15, 4.03, 0, 0, 9, 0, -1, 2.8, 3, 2.8, 5),
    REEF_2_LEFT(3.55, 3.05, 60, 2.7, 2.9, -1, 1, 5.5, -2, 4.5, 2.2),
    REEF_2_RIGHT(3.83, 2.89, 60, 2.7, 2.9, -1, 1, 5.5, -2, 4.5, 2.2),
    REEF_3_LEFT(4.86, 2.72, 120, 4.5, 2.2, 3.5, -2, 10, 1, 6.2, 3),
    REEF_3_RIGHT(5.15, 2.89, 120, 4.5, 2.2, 3.5, -2, 10, 1, 6.2, 3),
    REEF_4_LEFT(5.81, 3.7, 180, 9, -1, 9, 9, 6.2, 5, 6.2, 3),
    REEF_4_RIGHT(5.81, 4.03, 180, 9, -1, 9, 9, 6.2, 5, 6.2, 3),
    REEF_5_LEFT(5.43, 5.0, -120, 4.5, 5.8, 3.5, 10, 10, 7, 6.2, 5),
    REEF_5_RIGHT(5.15, 5.16, -120, 4.5, 5.8, 3.5, 10, 10, 7, 6.2, 5),
    REEF_6_LEFT(4.12, 5.33, -60, 2.7, 5.1, -1, 7, 5.5, 10, 4.5, 5.8),
    REEF_6_RIGHT(3.83, 5.16, -60, 2.7, 5.1, -1, 7, 5.5, 10, 4.5, 5.8),

    PROCESSOR(5.99, 0.48, 270, 8.3, 0, 8.3, 3.5, 3, 3.5, 1, 0);

    public static Destination fromSegmentAndPosition(ReefSegment segment, ReefLocation location,
            DriverStation.Alliance alliance) {
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
    // vertices of the trapezoid in which the destination is possible
    private final ArrayList<Translation2d> zone;

    private Destination(double x, double y, double angle, double p1x, double p1y, double p2x, double p2y, double p3x,
            double p3y, double p4x, double p4y) {
        this.pose = new Pose2d(x, y, Rotation2d.fromDegrees(angle));
        this.zone = new ArrayList<>();
        zone.add(new Translation2d(p1x, p1y));
        zone.add(new Translation2d(p2x, p2y));
        zone.add(new Translation2d(p3x, p3y));
        zone.add(new Translation2d(p4x, p4y));
    }

    public Rotation2d getAngle(DriverStation.Alliance alliance) {
        return alliance == DriverStation.Alliance.Blue ? pose.getRotation()
                : pose.getRotation().plus(Rotation2d.k180deg);
    }

    public Pose2d getPose(DriverStation.Alliance alliance) {
        return alliance == DriverStation.Alliance.Blue ? pose
                : new Pose2d(17.55 - pose.getX(), 8.05 - pose.getY(), pose.getRotation().unaryMinus());
    }

    public boolean inZone(Pose2d pos, DriverStation.Alliance alliance) {
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
