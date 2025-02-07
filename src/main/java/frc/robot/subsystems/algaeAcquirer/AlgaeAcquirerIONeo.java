package frc.robot.subsystems.algaeAcquirer;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.Constants;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.acquirerState;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.Position;

public class AlgaeAcquirerIONeo implements AlgaeAcquirerIO {
    private final SparkMax leftFlyWheel = new SparkMax(Constants.algaeFlywheelLeftNeoCanID, MotorType.kBrushless);
    private final SparkMax rightFlyWheel = new SparkMax(Constants.algaeFlywheelRightNeoCanID, MotorType.kBrushless);
    protected final SparkMax algaeAngleMotor = new SparkMax(Constants.algaeAngleMotorNeoCanID, MotorType.kBrushless);
    private final SparkMaxConfig sparkMaxConfig = new SparkMaxConfig();

    protected double currentAngleSetPoint = 0;

    public AlgaeAcquirerIONeo() {
        ClosedLoopConfig closedLoopConfig = new ClosedLoopConfig();
        closedLoopConfig.maxMotion.maxAcceleration(2000);
        closedLoopConfig.maxMotion.maxVelocity(600);
        closedLoopConfig.maxMotion.positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal);
        closedLoopConfig.maxMotion.allowedClosedLoopError(AlgaeAcquirerConstants.closedLoopAngleAllowedError);
        closedLoopConfig.feedbackSensor(FeedbackSensor.kAbsoluteEncoder);
        closedLoopConfig.pidf(0.2, 0, 0, 0);

        sparkMaxConfig.absoluteEncoder.setSparkMaxDataPortConfig();

        sparkMaxConfig.idleMode(IdleMode.kCoast).smartCurrentLimit(40).apply(closedLoopConfig);

        algaeAngleMotor.configure(sparkMaxConfig, null, null);
    }

    @Override
    public void updateInputs(AlgaeAcquirerIOInputs inputs) {
        // BaseStatusSignal.refreshAll(positionRot, velocityRotPerSec, appliedVolts,
        // currentAmps);

        // inputs.positionRad =
        // Units.rotationsToRadians(positionRot.getValueAsDouble());
        // inputs.velocityRadPerSec =
        // Units.rotationsToRadians(velocityRotPerSec.getValueAsDouble());
        // inputs.appliedVolts = appliedVolts.getValueAsDouble();
        // inputs.currentAmps = currentAmps.getValueAsDouble();
    }

    public double getAlgaeAngle() {
        return algaeAngleMotor.getEncoder().getPosition();
    }

    public double getVoltageLeft() {
        return leftFlyWheel.getAppliedOutput();
    }

    public double getVoltageRight() {
        return rightFlyWheel.getAppliedOutput();
    }

    public void setVoltageLeft(double voltage) {
        // angleEncoder.setControl(motionMagic.withPosition(position));
        leftFlyWheel.setVoltage(voltage);
    }

    public void setVoltageRight(double voltage) {
        // angleEncoder.setControl(motionMagic.withPosition(position));
        rightFlyWheel.setVoltage(voltage);
    }

    public void setAlgaeAcquirer(acquirerState state) {
        double leftFlyWheelVoltage = 0;
        double rightFlyWheelVoltage = 0;
        switch (state) {
            case SHOOT:
                leftFlyWheelVoltage = AlgaeAcquirerConstants.shootVoltageLeft;
                rightFlyWheelVoltage = AlgaeAcquirerConstants.shootVoltageRight;
                break;
            case ACQUIRE:
                leftFlyWheelVoltage = AlgaeAcquirerConstants.acquireVoltageLeft;
                rightFlyWheelVoltage = AlgaeAcquirerConstants.acquireVoltageRight;
                break;
            case STOP:
                break;
        }
        setVoltageLeft(leftFlyWheelVoltage);
        setVoltageRight(rightFlyWheelVoltage);
    }

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
        algaeAngleMotor.getClosedLoopController().setReference(currentAngleSetPoint, null);
    }

    @Override
    public boolean isAtSetPosition() {
        return Math.abs(algaeAngleMotor.getAbsoluteEncoder().getPosition()
                - currentAngleSetPoint) <= AlgaeAcquirerConstants.closedLoopAngleAllowedError;
    }
}
