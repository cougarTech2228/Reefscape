package frc.robot.subsystems.coralCone;

import static frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants.*;
import org.littletonrobotics.junction.Logger;
import frc.robot.subsystems.coralCone.CoralConeConstants;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;


public class CoralCone extends SubsystemBase {
    private final CoralConeIO io;
    private CoralConeIOInputsAutoLogged inputs = new CoralConeIOInputsAutoLogged();
    private WheelState currentWheelState = WheelState.TRANSIT;

    public enum Position {
        STOWED,
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
