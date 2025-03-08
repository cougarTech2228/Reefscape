package frc.robot.subsystems.coralCone;

import static frc.robot.subsystems.coralCone.CoralConeConstants.*;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;

public class CoralCone extends SubsystemBase {
    private final CoralConeIO io;
    private CoralConeIOInputsAutoLogged inputs = new CoralConeIOInputsAutoLogged();
    private WheelState currentWheelState = WheelState.TRANSIT;
    private final SysIdRoutine sysId;
    private boolean extraRotationsDone = false;
    private double extraRotationsStart = 0;
    private final String manaulValueKey = "CoralCone/manualSetpoint/value";
    private final String manaulEnableKey = "CoralCone/manualSetpoint/enabled";

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
        TRANSIT,
        SHOOT_FAST
    }

    public CoralCone(CoralConeIO io) {
        this.io = io;

        // Configure SysId
        sysId = new SysIdRoutine(
                new SysIdRoutine.Config(
                        Volts.of(0.5).per(Second),
                        Volts.of(3),
                        null,
                        (state) -> Logger.recordOutput("Elevator/SysIdState", state.toString())),
                new SysIdRoutine.Mechanism(
                        (voltage) -> runCharacterization(voltage.in(Volts)), null, this));

        SmartDashboard.putNumber(manaulValueKey, 0);
        SmartDashboard.putBoolean(manaulEnableKey, false);
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

    /**
     * Runs the module with the specified output while controlling to zero degrees.
     */
    public void runCharacterization(double output) {
        io.setAngleVoltage(output);
    }

    public boolean isAtSetPosition() {
        return inputs.angleMotorIsAtSetPosition;
    }

    public boolean isLoaded() {
        return inputs.beamBreak && extraRotationsDone;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("CoralCone", inputs);
        if (currentWheelState == WheelState.LOAD && inputs.beamBreak) {
            if (extraRotationsStart == 0) {
                extraRotationsStart = inputs.wheelPosition;
            } else {
                if (inputs.wheelPosition < (extraRotationsStart - extraLoadRotations)) {
                    setWheel(WheelState.TRANSIT);
                    extraRotationsStart = 0;
                    extraRotationsDone = true;
                }
            }
        }

        if (!inputs.beamBreak) {
            extraRotationsDone = false;
        }

        if (Constants.currentMode == Constants.Mode.SIM) {
            extraRotationsDone = true;
        }

        if (SmartDashboard.getBoolean(manaulEnableKey, false)){
            setManualPosition(SmartDashboard.getNumber(manaulValueKey, 0));
        }

        double angle = (360 * (inputs.angleMotorEncoderPosition - 0.59));
        RobotContainer.coralConePose = new Pose3d(
            RobotContainer.coralConePose.getX(),
            RobotContainer.coralConePose.getY(),
            RobotContainer.coralConePose.getZ(),
            new Rotation3d(0, Units.degreesToRadians(angle), 0));
    }

    private void setManualPosition(double position) {
        if (position < ANGLE_MIN) {
            position = ANGLE_MIN;
        } else if (position > ANGLE_MAX) {
            position = ANGLE_MAX;
        }
        io.setManualPosition(position);
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

    public void teleopInit() {
        if (inputs.beamBreak) {
            extraRotationsDone = true;
        }
    }
}
