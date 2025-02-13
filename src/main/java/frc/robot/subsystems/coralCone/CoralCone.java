package frc.robot.subsystems.coralCone;

import static frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants.*;
import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;


public class CoralCone extends SubsystemBase {
    private final CoralConeIO io;
    private CoralConeIOInputsAutoLogged inputs = new CoralConeIOInputsAutoLogged();
    private WheelState currentWheelState = WheelState.TRANSIT;
    private final SysIdRoutine sysId;

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

        // Configure SysId
        sysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                Volts.of(0.5).per(Second),
                Volts.of(3),
                null,
                (state) -> Logger.recordOutput("Elevator/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism(
                (voltage) -> runCharacterization(voltage.in(Volts)), null, this));
    }

    public void setPosition(Position position) {
        io.setPosition(position);
    }

    public void setWheel(WheelState state) {
        io.setWheel(state);
        currentWheelState = state;
    }

    /** Returns a command to run a quasistatic test in the specified direction. */
    public Command sysIdAngleQuasistatic(SysIdRoutine.Direction direction) {
        return run(() -> runCharacterization(0.0))
            .withTimeout(1.0)
            .andThen(sysId.quasistatic(direction));
    }

    /** Returns a command to run a dynamic test in the specified direction. */
    public Command sysIdAngleDynamic(SysIdRoutine.Direction direction) {
        return run(() -> runCharacterization(0.0)).withTimeout(1.0).andThen(sysId.dynamic(direction));
    }

    /** Runs the module with the specified output while controlling to zero degrees. */
    public void runCharacterization(double output) {
        io.setAngleVoltage(output);
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
