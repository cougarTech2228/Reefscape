package frc.robot.subsystems.climber;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Servo;
import frc.robot.Constants;
import frc.robot.subsystems.climber.Climber.ClimberPosition;
import frc.robot.subsystems.climber.Climber.ServoLockPosition;

public class ClimberIOTalonFX implements ClimberIO {
    protected final TalonFX climberMotor = new TalonFX(Constants.climberFalconCanID, "canivore");
    protected final Servo servoLockMotor = new Servo(0);

    protected final DutyCycleEncoder mClimberMotorEncoder = new DutyCycleEncoder(Constants.kClimberAngleEncoderDIO);

    private final StatusSignal<Voltage> climberMotorAppliedVoltage = climberMotor.getMotorVoltage();
    private final StatusSignal<Angle> climberMotorPosition = climberMotor.getPosition();
    private final StatusSignal<AngularVelocity> climberMotorVelocity = climberMotor.getVelocity();
    private final StatusSignal<Current> climberMotorCurrentAmps = climberMotor.getSupplyCurrent();

    public ClimberIOTalonFX() {
    }

    // @Override
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
        // System.out.println("Voltage " + output);
    }
}