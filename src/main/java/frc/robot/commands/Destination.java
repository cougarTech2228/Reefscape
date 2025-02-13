package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import java.util.ArrayList;

/**
 * Destinations for short autonomous paths
 */
public enum Destination {
    BLUE_1_LEFT(3.1, 4.33, 0, 0, 9, 0, -1, 2.8, 3, 2.8, 5),
    BLUE_1_RIGHT(3.1, 4.0, 0, 0, 9, 0, -1, 2.8, 3, 2.8, 5),
    BLUE_2_LEFT(3.6, 2.9, 60, 2.7, 2.9, -1, 1, 5.5, -2, 4.5, 2.2),
    BLUE_2_RIGHT(3.8, 2.8, 60, 2.7, 2.9, -1, 1, 5.5, -2, 4.5, 2.2),
    BLUE_3_LEFT(5, 2.7, 120, 4.5, 2.2, 3.5, -2, 10, 1, 6.2, 3),
    BLUE_3_RIGHT(5.2, 2.8, 120, 4.5, 2.2, 3.5, -2, 10, 1, 6.2, 3),
    BLUE_4_LEFT(5.8, 3.67, 180, 9, -1, 9, 9, 6.2, 5, 6.2, 3),
    BLUE_4_RIGHT(5.8, 4.0, 180, 9, -1, 9, 9, 6.2, 5, 6.2, 3),
    BLUE_5_LEFT(5.4, 5.1, -120, 4.5, 5.8, 3.5, 10, 10, 7, 6.2, 5),
    BLUE_5_RIGHT(5.2, 5.2, -120, 4.5, 5.8, 3.5, 10, 10, 7, 6.2, 5),
    BLUE_6_LEFT(4, 5.3, -60, 2.7, 5.1, -1, 7, 5.5, 10, 4.5, 5.8),
    BLUE_6_RIGHT(3.8, 5.2, -60, 2.7, 5.1, -1, 7, 5.5, 10, 4.5, 5.8);

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

    public Rotation2d getAngle() {
        return pose.getRotation();
    }

    public Pose2d getPose() {
        return pose;
    }

    public boolean inZone(Pose2d pos) {
        // for the given pose, is it in the trapezoid?
        // n>2 Keep track of cross product sign changes
        int pve = 0;
        int neg = 0;

        double x = pos.getX();
        double y = pos.getY();

        for (int i = 0; i < 4; i++) {
            // Form a segment between the i'th point
            double x1 = zone.get(i).getX();
            double y1 = zone.get(i).getY();

            // And the i+1'th, or if i is the last, with the first point
            int i2 = (i + 1) % 4;

            double x2 = zone.get(i2).getX();
            double y2 = zone.get(i2).getY();

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
