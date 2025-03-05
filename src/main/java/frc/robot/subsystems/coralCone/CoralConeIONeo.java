package frc.robot.subsystems.coralCone;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.DigitalInput;

import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.Constants;
import frc.robot.subsystems.coralCone.CoralCone.Position;
import frc.robot.subsystems.coralCone.CoralCone.WheelState;

public class CoralConeIONeo implements CoralConeIO {
    protected final SparkMax wheelMotor = new SparkMax(Constants.coralFlywheelCanID, MotorType.kBrushless);
    protected final SparkMax angleMotor = new SparkMax(Constants.coralRotateCanID, MotorType.kBrushless);
    private final SparkMaxConfig angleMotorConfig = new SparkMaxConfig();
    private final SparkMaxConfig wheelMotorConfig = new SparkMaxConfig();
    protected final DigitalInput beamBreakSensor = new DigitalInput(Constants.CoralBeamBreakSensorDIO);

    protected double currentAngleSetPoint = 0;
    private WheelState currentWheelState = WheelState.TRANSIT;

    public CoralConeIONeo() {

        ClosedLoopConfig closedLoopConfig = new ClosedLoopConfig();
        closedLoopConfig.maxMotion.maxAcceleration(2000);
        closedLoopConfig.maxMotion.maxVelocity(600);
        closedLoopConfig.maxMotion.positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal);
        closedLoopConfig.maxMotion.allowedClosedLoopError(CoralConeConstants.closedLoopAngleAllowedError);
        closedLoopConfig.feedbackSensor(FeedbackSensor.kAbsoluteEncoder);
        closedLoopConfig.pidf(0.2, 0, 0, 0);

        angleMotorConfig.absoluteEncoder.setSparkMaxDataPortConfig();
        angleMotorConfig
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(20)
                .apply(closedLoopConfig);

        angleMotor.configure(angleMotorConfig, null, null);

        wheelMotorConfig
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(20);
        wheelMotor.configure(wheelMotorConfig, null, null);

    }

    @Override
    public void updateInputs(CoralConeIOInputs inputs) {
        inputs.angleMotorVoltage = angleMotor.getAppliedOutput();
        inputs.angleMotorPosition = angleMotor.getEncoder().getPosition();
        inputs.angleMotorVelocity = angleMotor.getEncoder().getVelocity();
        inputs.angleMotorCurrent = angleMotor.getOutputCurrent();
        inputs.angleMotorEncoderVelocity = angleMotor.getAbsoluteEncoder().getVelocity();
        inputs.angleMotorEncoderPosition = angleMotor.getAbsoluteEncoder().getPosition();
        inputs.angleMotorIsAtSetPosition = Math.abs(angleMotor.getAbsoluteEncoder().getPosition()
            - currentAngleSetPoint) <= CoralConeConstants.closedLoopAngleAllowedError;
        inputs.wheelVoltage = wheelMotor.getAppliedOutput();
        inputs.wheelPosition = wheelMotor.getEncoder().getPosition();
        if (Constants.currentMode == Constants.Mode.SIM && currentWheelState == WheelState.LOAD) {
            inputs.beamBreak = true;
        } else {
            inputs.beamBreak = !beamBreakSensor.get();
        }
        inputs.angleSetPoition = currentAngleSetPoint;
    }

    public void setPosition(Position angle) {
        switch (angle) {
            case STOWED:
                currentAngleSetPoint = CoralConeConstants.stowedAngle;
                break;
            case LOAD:
                currentAngleSetPoint = CoralConeConstants.loadAngle;
                break;
            case L1_SHOOT:
                currentAngleSetPoint = CoralConeConstants.L1Angle;
                break;
            case L2_SHOOT:
                currentAngleSetPoint = CoralConeConstants.L2Angle;
                break;
            case L3_SHOOT:
                currentAngleSetPoint = CoralConeConstants.L3Angle;
                break;
            case L4_SHOOT:
                currentAngleSetPoint = CoralConeConstants.L4Angle;
                break;
        }
        angleMotor.getClosedLoopController().setReference(currentAngleSetPoint, ControlType.kMAXMotionPositionControl);
    }

    public void setWheel(WheelState state) {
        double motorVoltage = 0;
        currentWheelState = state;
        switch (state) {
            case LOAD:
                motorVoltage = CoralConeConstants.loadVoltage;
                break;
            case SHOOT:
                motorVoltage = CoralConeConstants.shootVoltage;
                break;
            case TRANSIT:
                motorVoltage = CoralConeConstants.transitVoltage;
                break;
            case SHOOT_FAST:
                motorVoltage = CoralConeConstants.shootFastVoltage;
                break;
        }
        wheelMotor.setVoltage(motorVoltage);
    }

    public void setAngleVoltage(double voltage) {
        wheelMotor.setVoltage(voltage);
    }

    @Override
    public void setManualPosition(double position) {
        currentAngleSetPoint = position;
        angleMotor.getClosedLoopController().setReference(currentAngleSetPoint, ControlType.kMAXMotionPositionControl);
    }
}
