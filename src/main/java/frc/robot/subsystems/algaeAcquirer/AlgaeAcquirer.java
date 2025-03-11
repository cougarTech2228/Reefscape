package frc.robot.subsystems.algaeAcquirer;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.RobotContainer;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;
import static frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants.*;

public class AlgaeAcquirer extends SubsystemBase {
    private final AlgaeAcquirerIO io;
    private final SysIdRoutine sysId;
    private AlgaeAcquirerIOInputsAutoLogged inputs = new AlgaeAcquirerIOInputsAutoLogged();

    private final String manaulValueKey = "AlgaeAcquirer/manualSetpoint/value";
    private final String manaulEnableKey = "AlgaeAcquirer/manualSetpoint/enabled";

    private double acquirerStartTime = 0;
    public enum FlywheelState {
        SHOOT,
        ACQUIRE,
        STOP,
        HOLD
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

        SmartDashboard.putNumber(manaulValueKey, 0);
        SmartDashboard.putBoolean(manaulEnableKey, false);

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
        if (state == FlywheelState.ACQUIRE) {
            acquirerStartTime = Timer.getFPGATimestamp();
        } else {
            acquirerStartTime = 0;
        }
        io.setAlgaeAcquirer(state);
    }

    public void setPosition(Position position) {
        io.setPosition(position);
    }

    public boolean isAtSetPosition() {
        return inputs.angleIsAtSetPosition;
    }

    private void setManualPosition(double position) {
        if (position < ANGLE_MIN) {
            position = ANGLE_MIN;
        } else if ( position > ANGLE_MAX) {
            position = ANGLE_MAX;
        }
        io.setManualPosition(position);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("AlgaeAcquirer", inputs);

        if (SmartDashboard.getBoolean(manaulEnableKey, false)){
            setManualPosition(SmartDashboard.getNumber(manaulValueKey, 0));
        }
        double angle = (-360 * (inputs.angleEncoderPosition - AlgaeAcquirerConstants.floorAcquireAngle));

        RobotContainer.algaeAcquirerPose = new Pose3d(
            RobotContainer.algaeAcquirerPose.getX(),
            RobotContainer.algaeAcquirerPose.getY(),
            RobotContainer.algaeAcquirerPose.getZ(),
            new Rotation3d(0, Units.degreesToRadians(angle), 0));

        double now = Timer.getFPGATimestamp();
        if (isLoaded() && acquirerStartTime > 0 && ((now - acquirerStartTime) > 0.5)) {
            setFlywheelState(FlywheelState.HOLD);
        }
        // setFlywheelState(FlywheelState.STOP);
    }

    public void manualUp() {
        io.setAngleVoltage(kManualUpVoltage);
    }

    public void manualDown() {
        io.setAngleVoltage(kManualDownVoltage);
    }    

    public void stop() {
        io.setAngleVoltage(0);
        setFlywheelState(FlywheelState.STOP);
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
