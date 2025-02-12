package frc.robot.subsystems.algaeAcquirer;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants.*;

public class AlgaeAcquirer extends SubsystemBase {
    private final AlgaeAcquirerIO io;
    private AlgaeAcquirerIOInputsAutoLogged inputs = new AlgaeAcquirerIOInputsAutoLogged();

    public enum acquirerState {
        SHOOT,
        ACQUIRE,
        STOP
    }

    public enum Position {
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

    public void setPosition(Position position) {
        io.setPosition(position);
    }

    public boolean isAtSetPosition() {
        return inputs.angleIsAtSetPosition;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("AlgaeAcquirer", inputs);
    }

    public void manualUp() {
        io.setAngleVoltage(kManualUpVoltage);
    }

    public void manualDown() {
        io.setAngleVoltage(kManualDownVoltage);
    }    

    public void stop() {
        io.setAngleVoltage(0);
    }
}
