package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;
import static frc.robot.subsystems.elevator.ElevatorConstants.*;

import org.littletonrobotics.junction.Logger;

public class Elevator extends SubsystemBase {
    private final ElevatorIO io;
    private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();
    private final SysIdRoutine sysId;
    private boolean hasBeenHome = false;
    Alert notHomedAlert = new Alert("Elevator has not been homed", AlertType.kError);

    public enum Position {
        TRANSIT,
        ALGAE_FLOOR,
        ALGAE_FLOOR_ON_CORAL,
        ALGAE_PROCESSOR,
        ALGAE_REEF_LOW,
        ALGAE_REEF_HIGH,
        ALGAE_BARGE,
        CORAL_LOAD,
        CORAL_L1,
        CORAL_L2,
        CORAL_L3,
        CORAL_L4
    };

    public Elevator(ElevatorIO io) {
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
                    (voltage) -> runCharacterization(-voltage.in(Volts)), null, this));
    }

    @Override
    // Runs on a schedule, after some amount of milliseconds
    public void periodic() {
        io.updateInputs(inputs);
        if (!hasBeenHome) {
            hasBeenHome = inputs.bottomLimit;
            notHomedAlert.set(!hasBeenHome);
        }
        Logger.processInputs("Elevator", inputs);
    }

    public void setPosition(Position posistion) {
        if(!hasBeenHome && posistion != Position.TRANSIT){
            // We are not homed yet, REFUSE to move anywhere but home
            return;
        }
        // ensure coral and algae are in a safe position to move the elevator
        io.setPosition(posistion);
    }

    public boolean isAtSetPosition() {
        return inputs.isAtSetPosition;
    }

    /** Returns a command to run a quasistatic test in the specified direction. */
    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return run(() -> runCharacterization(0.0))
            .withTimeout(1.0)
            .andThen(sysId.quasistatic(direction));
    }

    /** Returns a command to run a dynamic test in the specified direction. */
    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return run(() -> runCharacterization(0.0)).withTimeout(1.0).andThen(sysId.dynamic(direction));
    }

    /** Runs the module with the specified output while controlling to zero degrees. */
    public void runCharacterization(double output) {
        io.setVoltage(output);
    }

    @Override
    public void simulationPeriodic() {
        io.simulationPeriodic();
    }

    public void manualUp() {
        io.setVoltage(kManualUpVoltage);
    }
    
    public void manualDown() {
        io.setVoltage(kManualDownVoltage);
    }

    public void stop() {
        io.setVoltage(0);//-0.5);
    }
}
