package frc.robot.subsystems.algaeAcquirer;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;
import static frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants.*;

public class AlgaeAcquirer extends SubsystemBase {
    private final AlgaeAcquirerIO io;
    private final SysIdRoutine sysId;
    private AlgaeAcquirerIOInputsAutoLogged inputs = new AlgaeAcquirerIOInputsAutoLogged();

    public enum FlywheelState {
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

    public void setFlywheelState(FlywheelState state) {
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

    public boolean isLoaded() {
        return inputs.isLoaded;
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
}
