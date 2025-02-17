package frc.robot.subsystems.climber;

import frc.robot.Constants;

import static frc.robot.subsystems.climber.ClimberConstants.*;
import frc.robot.subsystems.climber.Climber.ClimberPosition;
import frc.robot.subsystems.climber.Climber.ServoLockPosition;
import frc.robot.subsystems.elevator.ElevatorIO.ElevatorIOInputs;
import frc.robot.subsystems.climber.ClimberIO.ClimberIOInputs;
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
    protected final DutyCycleEncoder mClimberMotorEncoder = new DutyCycleEncoder(Constants.kClimberAngleEncoderDIO);

    private final StatusSignal<Voltage> climberMotorAppliedVoltage = climberMotor.getMotorVoltage();
    private final StatusSignal<Angle> climberMotorPosition = climberMotor.getPosition();
    private final StatusSignal<AngularVelocity> climberMotorVelocity = climberMotor.getVelocity();
    private final StatusSignal<Current> climberMotorCurrentAmps = climberMotor.getSupplyCurrent();
    // private final StatusSignal<ForwardLimitValue> forwardLimit =
    // climberMotor.getForwardLimit();

    //
    public ClimberIOTalonFX() {
        // var talonFXConfigs = new TalonFXConfiguration();
        // talonFXConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    }

    public void updateInputs(ClimberIOInputs inputs) {
        BaseStatusSignal.refreshAll(
                climberMotorAppliedVoltage, climberMotorPosition, climberMotorVelocity, climberMotorCurrentAmps);

        inputs.climberMotorPosition = climberMotorPosition.getValueAsDouble();
        inputs.climberMotorVelocity = climberMotorVelocity.getValueAsDouble();
        inputs.climberMotorAppliedVoltage = climberMotorAppliedVoltage.getValueAsDouble();
        inputs.climberMotorCurrentAmps = climberMotorCurrentAmps.getValueAsDouble();
        inputs.climberMotorEncoderPosition = mClimberMotorEncoder.get();

        inputs.climberMotorIsAtSetPosition = climberMotor.getClosedLoopError()
                .getValue() < ClimberConstants.ClosedLoopErrorThreshold;
    }

    public void setClimberPosition(ClimberPosition climberPosition) {
        double climberMotorVoltage = 0;
        switch (climberPosition) {
            case UP:
                climberMotorVoltage = ClimberConstants.RAISE_VOLTAGE;
                break;
            case DOWN:
                climberMotorVoltage = ClimberConstants.LOWER_VOLTAGE;
                break;
        }
        climberMotor.setVoltage(climberMotorVoltage);
    }

    public void setServoPosition(ServoLockPosition servoLockPosition) {
        double servoAngle = 0;
        switch (servoLockPosition) {
            case LOCKED:
                servoAngle = ClimberConstants.servoLockedAngle;
                break;
            case UNLOCKED:
                servoAngle = ClimberConstants.servoUnlockedAngle;
                break;
        }
        servoLockMotor.set(servoAngle);
    }

    public void setVoltage(double output) {
        climberMotor.setVoltage(output);
        System.out.println("Voltage " + output);
    }
}
