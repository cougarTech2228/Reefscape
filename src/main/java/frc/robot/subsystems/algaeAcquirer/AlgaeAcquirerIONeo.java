package frc.robot.subsystems.algaeAcquirer;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.Constants;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.FlywheelState;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.Position;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants.FlyMode;

import static frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants.*;

public class AlgaeAcquirerIONeo implements AlgaeAcquirerIO {
    protected final SparkMax leftFlyWheel = new SparkMax(Constants.algaeFlywheelLeftNeoCanID, MotorType.kBrushless);
    protected final SparkMax rightFlyWheel = new SparkMax(Constants.algaeFlywheelRightNeoCanID, MotorType.kBrushless);
    protected final SparkMax algaeAngleMotor = new SparkMax(Constants.algaeAngleMotorNeoCanID, MotorType.kBrushless);
    private final SparkMaxConfig angleMotorConfig = new SparkMaxConfig();
    private final SparkMaxConfig flywheelConfig = new SparkMaxConfig();

    protected double currentAngleSetPoint = 0;
    private FlywheelState currentFlywheelState = FlywheelState.STOP;

    public AlgaeAcquirerIONeo() {
        ClosedLoopConfig angleClosedLoopConfig = new ClosedLoopConfig();
        angleClosedLoopConfig.maxMotion.maxAcceleration(2000);
        angleClosedLoopConfig.maxMotion.maxVelocity(600);
        angleClosedLoopConfig.maxMotion.positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal);
        angleClosedLoopConfig.maxMotion.allowedClosedLoopError(AlgaeAcquirerConstants.closedLoopAngleAllowedError);
        angleClosedLoopConfig.feedbackSensor(FeedbackSensor.kAbsoluteEncoder);
        angleClosedLoopConfig.pidf(0.4, 0, 0, 0);

        ClosedLoopConfig flyClosedLoopConfig = new ClosedLoopConfig();
        flyClosedLoopConfig.maxMotion.maxAcceleration(2000);
        flyClosedLoopConfig.maxMotion.maxVelocity(600);
        flyClosedLoopConfig.maxMotion.positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal);
        flyClosedLoopConfig.maxMotion.allowedClosedLoopError(AlgaeAcquirerConstants.closedLoopAngleAllowedError);
        flyClosedLoopConfig.feedbackSensor(FeedbackSensor.kPrimaryEncoder);
        flyClosedLoopConfig.pidf(0.2, 0, 0, 0);

        angleMotorConfig.absoluteEncoder.setSparkMaxDataPortConfig();
        angleMotorConfig
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(20)
            .apply(angleClosedLoopConfig);
        algaeAngleMotor.configure(angleMotorConfig, null, null);

        flywheelConfig
            .idleMode(IdleMode.kCoast)
            .smartCurrentLimit(6)
            .apply(flyClosedLoopConfig);

        rightFlyWheel.configure(flywheelConfig, null, null);
        // leftFlyWheel.configure(flywheelConfig, null, null);
        leftFlyWheel.configure(flywheelConfig.disableFollowerMode(), null, null);

    }

    @Override
    public void updateInputs(AlgaeAcquirerIOInputs inputs) {
        inputs.angleSetPoition = currentAngleSetPoint;
        inputs.anglePosition = algaeAngleMotor.getEncoder().getPosition();
        inputs.angleVelocity = algaeAngleMotor.getEncoder().getVelocity();
        inputs.angleEncoderPosition = algaeAngleMotor.getAbsoluteEncoder().getPosition();
        inputs.angleEncoderVelocity = algaeAngleMotor.getAbsoluteEncoder().getVelocity();
        inputs.angleAppliedVolts = algaeAngleMotor.getAppliedOutput();
        inputs.angleCurrentAmps = algaeAngleMotor.getOutputCurrent();
        inputs.angleIsAtSetPosition = Math.abs(algaeAngleMotor.getAbsoluteEncoder().getPosition()
                - currentAngleSetPoint) <= AlgaeAcquirerConstants.closedLoopAngleAllowedError;
        inputs.flyAppliedVoltsLeft = leftFlyWheel.getAppliedOutput();
        inputs.flyVelocityLeft = leftFlyWheel.getEncoder().getVelocity();
        inputs.flyCurrentAmpsLeft = leftFlyWheel.getOutputCurrent();

        inputs.flyAppliedVoltsRight = rightFlyWheel.getAppliedOutput();
        inputs.flyVelocityRight = rightFlyWheel.getEncoder().getVelocity();
        inputs.flyCurrentAmpsRight = rightFlyWheel.getOutputCurrent();

        // determine loaded state based on high current draw, and low velocity
        if (Constants.currentMode == Constants.Mode.SIM && currentFlywheelState == FlywheelState.ACQUIRE) {
            inputs.isLoaded = true;
        } else {
            inputs.isLoaded = (currentFlywheelState == FlywheelState.ACQUIRE && 
                (Math.abs(inputs.flyVelocityRight) < kLoadedVelocityThreshold &&
                (Math.abs(inputs.flyCurrentAmpsRight) > 0))
                || currentFlywheelState == FlywheelState.HOLD);
            // inputs.isLoaded = currentFlywheelState == FlywheelState.HOLD;
        }

        inputs.flyState = currentFlywheelState.toString();
    }

    @Override
    public void setFlyVoltage(double voltage) {
        rightFlyWheel.setVoltage(voltage);
        leftFlyWheel.setVoltage(-voltage);
    }

    @Override
    public void setAlgaeAcquirer(FlywheelState state) {
        currentFlywheelState = state;
        switch (state) {
            case SHOOT:
                setFlyVoltage(AlgaeAcquirerConstants.shootVoltage);
                break;
            case ACQUIRE:
                setFlyVoltage(AlgaeAcquirerConstants.acquireVoltage);
                break;
            case STOP:
                setFlyVoltage(0);
            break;
            case HOLD:
                rightFlyWheel.getClosedLoopController().setReference(rightFlyWheel.getEncoder().getPosition(), ControlType.kMAXMotionPositionControl);
                leftFlyWheel.getClosedLoopController().setReference(leftFlyWheel.getEncoder().getPosition(), ControlType.kMAXMotionPositionControl);
                break;
        }
    }

    @Override
    public void setPosition(Position position) {
        switch (position) {
            case STOWED:
                currentAngleSetPoint = AlgaeAcquirerConstants.stowedAngle;
                break;
            case REEF_ACQUIRE:
                currentAngleSetPoint = AlgaeAcquirerConstants.reefAcquireAngle;
                break;
            case FLOOR_ACQUIRE:
                currentAngleSetPoint = AlgaeAcquirerConstants.floorAcquireAngle;
                break;
            case BARGE_SHOOT:
                currentAngleSetPoint = AlgaeAcquirerConstants.bargeShootAngle;
                break;
            case PROCESSOR_SHOOT:
                currentAngleSetPoint = AlgaeAcquirerConstants.processorShootAngle;
                break;
        }
        algaeAngleMotor.getClosedLoopController().setReference(currentAngleSetPoint, ControlType.kMAXMotionPositionControl);
    }

    @Override
    public void setManualPosition(double position) {
        currentAngleSetPoint = position;
        algaeAngleMotor.getClosedLoopController().setReference(currentAngleSetPoint, ControlType.kMAXMotionPositionControl);
    }

    @Override
    public void setAngleVoltage(double voltage) {
        algaeAngleMotor.setVoltage(voltage);
    }
}
