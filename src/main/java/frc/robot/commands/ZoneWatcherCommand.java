package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.Drive;

/**
 * Runs and does nothing until robot enters the zone for the given destination.
 * At that point will create and start a path to the destination.
 */
public class ZoneWatcherCommand extends Command {

    private Destination dest;
    private Drive drive;

    public ZoneWatcherCommand(Destination dest, Drive drive) {
        this.dest = dest;
        this.drive = drive;
    }

    public boolean isFinished() {
        if (dest.inZone(drive.getPose(), DriverStation.getAlliance().get())) {
            // Command path = DriveCommands.shortAutoPath(drive, dest);
            // CommandScheduler.getInstance().schedule(path);
            return true;
        }
        return false;
    }
}
