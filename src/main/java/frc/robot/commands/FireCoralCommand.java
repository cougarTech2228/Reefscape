package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.coralCone.CoralCone.WheelState;

/**
 * Shoot the coral. Command finishes after timer expires.
 */
public class FireCoralCommand extends Command {
    private final CoralCone coralCone;

    private final double shootDurationSec = 0.25;
    private double startTime = 0;
    private boolean isFast;

    public FireCoralCommand(CoralCone coralCone, boolean isFast) {
        this.coralCone = coralCone;
        this.isFast = isFast;
        addRequirements(coralCone);
    }

    @Override
    public void initialize() {
        if (isFast) {
            coralCone.setWheel(WheelState.SHOOT_FAST);
        } else {
            coralCone.setWheel(WheelState.SHOOT);
        }
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public boolean isFinished() {
        double now = Timer.getFPGATimestamp();
        return ((now - startTime) > shootDurationSec);
    }

    @Override
    public void end(boolean interrupted) {
        coralCone.setWheel(WheelState.TRANSIT);
        if (interrupted) {
            if (interrupted) {
                coralCone.stop();
            }
        }
    }
}
