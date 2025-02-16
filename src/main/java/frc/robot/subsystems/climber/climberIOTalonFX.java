package frc.robot.subsystems.climber;

import frc.robot.Constants;

import static frc.robot.subsystems.climber.ClimberConstants.*;
import frc.robot.subsystems.climber.Climber.ClimberPosition;
import frc.robot.subsystems.climber.Climber.ServoLockPosition;
import frc.robot.subsystems.elevator.ElevatorIO.ElevatorIOInputs;
import frc.robot.sybsystems.climber.ClimberIO.ClimberIOInputs;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.HardwareLimitSwitchConfigs;
// import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
// import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
// import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ForwardLimitValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;

public class ClimberIOTalonFX implements ClimberIO {
    protected final TalonFX climberMotor = new TalonFX(Constants.climberFalconCanID, "canivore");
    protected final Servo servoLockMotor = new Servo(0);
    protected final DutyCycleEncoder mClimberMotorEncoder = new DutyCycleEncoder(
            ClimberConstants.kClimberAngleEncoderDIO);

    private final StatusSignal<Angle> climberMotorVoltage = climberMotor.getPosition();
    private final StatusSignal<AngularVelocity> velocityRotPerSecA = elevatorA.getVelocity();
    private final StatusSignal<Voltage> appliedVoltsA = elevatorA.getMotorVoltage();
    private final StatusSignal<Current> currentAmpsA = elevatorA.getSupplyCurrent();
    private final StatusSignal<ForwardLimitValue> forwardLimitA = elevatorA.getForwardLimit();

    private final StatusSignal<Angle> positionRotB = elevatorB.getPosition();
    private final StatusSignal<AngularVelocity> velocityRotPerSecB = elevatorB.getVelocity();
    private final StatusSignal<Voltage> appliedVoltsB = elevatorB.getMotorVoltage();
    private final StatusSignal<Current> currentAmpsB = elevatorB.getSupplyCurrent();

    ClimberIOTalonFX() {
        // var talonFXConfigs = new TalonFXConfiguration();
        // talonFXConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        // @Override
        // public void updateInputs(ClimberIOInputs inputs) {
        // BaseStatusSignal.refreshAll(
        // climberMotorVoltage, climberMotorPosition, climberMotorVelocity,
        // climberMotorCurrent,
        // climberMotorEncoderVelocity, climberMotorEncoderPosition);

        // inputs.position_A = positionRotA.getValueAsDouble();
        // inputs.velocity_A = velocityRotPerSecA.getValueAsDouble();
        // inputs.appliedVolts_A = appliedVoltsA.getValueAsDouble();
        // inputs.currentAmps_A = currentAmpsA.getValueAsDouble();

        // inputs.position_B = positionRotB.getValueAsDouble()
        // inputs.velocity_B = velocityRotPerSecB.getValueAsDouble();
        // inputs.appliedVolts_B = appliedVoltsB.getValueAsDouble();
        // inputs.currentAmps_B = currentAmpsB.getValueAsDouble();

        // inputs.bottomLimit = forwardLimitA.getValue() ==
        // ForwardLimitValue.ClosedToGround;
        // inputs.isAtSetPosition = elevatorA.getClosedLoopError().getValue() <
        // ClosedLoopErrorThreshold;
    }

    public void setClimberPosition(ClimberPosition climberPosition) {
        double climberMotorVoltage = 0;
        switch (climberPosition) {
            case UP:
                climberMotorVoltage = ClimberConstants.upPosition;
                break;
            case DOWN:
                climberMotorVoltage = ClimberConstants.downPosition;
                break;
        }

    }

    public void setVoltage(double output) {
        climberMotor.setVoltage(output);
        System.out.println("Voltage " + output);
    }
}
