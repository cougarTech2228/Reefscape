package frc.robot.subsystems.algaeAcquirer;

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

import static frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants.*;

public class AlgaeAcquirerIONeo implements AlgaeAcquirerIO {
    protected final SparkMax leftFlyWheel = new SparkMax(Constants.algaeFlywheelLeftNeoCanID, MotorType.kBrushless);
    protected final SparkMax rightFlyWheel = new SparkMax(Constants.algaeFlywheelRightNeoCanID, MotorType.kBrushless);
    protected final SparkMax algaeAngleMotor = new SparkMax(Constants.algaeAngleMotorNeoCanID, MotorType.kBrushless);
    private final SparkMaxConfig angleSparkMaxConfig = new SparkMaxConfig();
    private final SparkMaxConfig flySparkMaxConfig = new SparkMaxConfig();

    protected double currentAngleSetPoint = 0;

    public AlgaeAcquirerIONeo() {
        ClosedLoopConfig closedLoopConfig = new ClosedLoopConfig();
        closedLoopConfig.maxMotion.maxAcceleration(2000);
        closedLoopConfig.maxMotion.maxVelocity(600);
        closedLoopConfig.maxMotion.positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal);
        closedLoopConfig.maxMotion.allowedClosedLoopError(AlgaeAcquirerConstants.closedLoopAngleAllowedError);
        closedLoopConfig.feedbackSensor(FeedbackSensor.kAbsoluteEncoder);
        closedLoopConfig.pidf(0.2, 0, 0, 0);

        angleSparkMaxConfig.absoluteEncoder.setSparkMaxDataPortConfig();
        angleSparkMaxConfig.idleMode(IdleMode.kCoast).smartCurrentLimit(20).apply(closedLoopConfig);
        algaeAngleMotor.configure(angleSparkMaxConfig, null, null);

        flySparkMaxConfig.smartCurrentLimit(20);
        rightFlyWheel.configure(flySparkMaxConfig, null, null);
        leftFlyWheel.configure(flySparkMaxConfig.follow(rightFlyWheel, true), null, null);
    }

    @Override
    public void updateInputs(AlgaeAcquirerIOInputs inputs) {
        inputs.anglePosition = algaeAngleMotor.getEncoder().getPosition();
        inputs.angleVelocity = algaeAngleMotor.getEncoder().getVelocity();
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
        inputs.isLoaded = (Math.abs(inputs.flyVelocityRight) < kLoadedVelocityThreshold &&
            Math.abs(inputs.flyCurrentAmpsRight) > kLoadedCurrentDrawThreshold);
    }

    @Override
    public void setFlyVoltage(double voltage) {
        rightFlyWheel.setVoltage(voltage);
    }

    @Override
    public void setAlgaeAcquirer(FlywheelState state) {
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
        algaeAngleMotor.getClosedLoopController().setReference(currentAngleSetPoint, null);
    }

    @Override
    public void setAngleVoltage(double voltage) {
        algaeAngleMotor.setVoltage(voltage);
    }
}
