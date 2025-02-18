package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.FlywheelState;

/**
 * Shoot the coral. Command finishes after timer expires.
 */
public class FireAlgaeCommand extends Command {
    private final AlgaeAcquirer algaeAcquirer;

    private final double shootDurationSec = 0.75;
    private double startTime = 0;

    public FireAlgaeCommand(AlgaeAcquirer algaeAcquirer) {
        this.algaeAcquirer = algaeAcquirer;
        addRequirements(algaeAcquirer);
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void initialize() {
        algaeAcquirer.setFlywheelState(FlywheelState.SHOOT);
    }

    @Override
    public boolean isFinished() {
        double now = Timer.getFPGATimestamp();
        return ((now - startTime) > shootDurationSec);
    }

    @Override
    public void end(boolean interrupted) {
        algaeAcquirer.setFlywheelState(FlywheelState.STOP);
        if (interrupted){
            if (interrupted) {
                algaeAcquirer.stop();
            }
        }
    }
}
