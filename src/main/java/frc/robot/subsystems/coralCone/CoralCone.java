package frc.robot.subsystems.coralCone;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class CoralCone extends SubsystemBase {
    private final CoralConeIO io;
    private CoralConeIOInputsAutoLogged inputs = new CoralConeIOInputsAutoLogged();
    private WheelState currentWheelState = WheelState.TRANSIT;

    public enum Position {
        LOAD,
        L1_SHOOT,
        L2_SHOOT,
        L3_SHOOT,
        L4_SHOOT
    };

    public enum WheelState {
        LOAD,
        SHOOT,
        TRANSIT
    }

    public CoralCone(CoralConeIO io) {
        this.io = io;
    }

    public void setPosition(Position position) {
        io.setPosition(position);
    }

    public void setWheel(WheelState state) {
        io.setWheel(state);
        currentWheelState = state;
    }

    public boolean isAtSetPosition() {
        return inputs.angleMotorIsAtSetPosition;
    }

    public boolean isLoaded() {
        return inputs.isLoaded;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("CoralCone", inputs);
        if (currentWheelState == WheelState.LOAD && isLoaded()){
            setWheel(WheelState.TRANSIT);
        }
    }
}
