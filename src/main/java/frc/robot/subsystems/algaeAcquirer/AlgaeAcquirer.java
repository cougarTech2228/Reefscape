package frc.robot.subsystems.algaeAcquirer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.subsystems.angleEncoder.CoralClawIOInputsAutoLogged;

public class AlgaeAcquirer extends SubsystemBase {
    private final AlgaeAcquirerIO io;
    private CoralClawIOInputsAutoLogged inputs = new CoralClawIOInputsAutoLogged();

    public enum acquirerState {
        SHOOT,
        ACQUIRE,
        STOP
    }

    public enum algaeAngle {
        STOWED,
        REEF_ACQUIRE,
        FLOOR_ACQUIRE,
        BARGE_SHOOT,
        PROCESSOR_SHOOT
    }

    public AlgaeAcquirer(AlgaeAcquirerIO io) {
        this.io = io;
    }

    public void setAlgaeAcquirer(acquirerState state) {
        io.setAlgaeAcquirer(state);
    }

    public void setAlgaeAngle(algaeAngle angle) {
        io.setAlgaeAngle(angle);
    }
}
