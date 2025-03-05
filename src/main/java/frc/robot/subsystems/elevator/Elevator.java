package frc.robot.subsystems.elevator;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.RobotContainer;

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

    private final String manaulValueKey = "Elevator/manualSetpoint/value";
    private final String manaulEnableKey = "Elevator/manualSetpoint/enabled";
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

        if (SmartDashboard.getBoolean(manaulEnableKey, false)){
            // the UI shows positive values, but we work in negative space, so invert it here
            setManualPosition(-SmartDashboard.getNumber(manaulValueKey, 0));
        }

        double height = (2.09 / 125.5) * Math.abs(inputs.position_A);

        RobotContainer.elevatorStage1Pose = new Pose3d(
            RobotContainer.elevatorStage1Pose.getX(),
            RobotContainer.elevatorStage1Pose.getY(),
            height / 3,
            RobotContainer.elevatorStage1Pose.getRotation());

        RobotContainer.elevatorStage2Pose = new Pose3d(
            RobotContainer.elevatorStage2Pose.getX(),
            RobotContainer.elevatorStage2Pose.getY(),
            (height * 2) / 3,
            RobotContainer.elevatorStage2Pose.getRotation());

        RobotContainer.elevatorCarriagePose = new Pose3d(
            RobotContainer.elevatorCarriagePose.getX(),
            RobotContainer.elevatorCarriagePose.getY(),
            height,
            RobotContainer.elevatorCarriagePose.getRotation());

        RobotContainer.coralConePose = new Pose3d(
            RobotContainer.coralConePose.getX(),
            RobotContainer.coralConePose.getY(),
            height + RobotContainer.coralConeBaseHeight,
            RobotContainer.coralConePose.getRotation());

        RobotContainer.algaeAcquirerPose = new Pose3d(
            RobotContainer.algaeAcquirerPose.getX(),
            RobotContainer.algaeAcquirerPose.getY(),
            height + RobotContainer.algaeAcquirerBaseHeight,
            RobotContainer.algaeAcquirerPose.getRotation());
    }

    public void setPosition(Position position) {
        if(!hasBeenHome && position != Position.TRANSIT){
            // We are not homed yet, REFUSE to move anywhere but home
            return;
        }
        // ensure coral and algae are in a safe position to move the elevator
        io.setPosition(position);
    }

    private void setManualPosition(double position) {
        if(!hasBeenHome && position != 0){
            // We are not homed yet, REFUSE to move anywhere but home
            return;
        }
        if (position < HEIGHT_MIN) {
            position = HEIGHT_MIN;
        } else if ( position > HEIGHT_MAX) {
            position = HEIGHT_MAX;
        }
        io.setManualPosition(position);
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

    /** return a value between 0 and 1 */
    public double getCurrentHeightPercentage() {
        return Math.abs((
            Math.abs(ElevatorConstants.HEIGHT_MIN) - Math.abs(inputs.position_A)) / ElevatorConstants.HEIGHT_MIN);
    }
}
