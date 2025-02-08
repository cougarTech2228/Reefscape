package frc.robot.subsystems.algaeAcquirer;

import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.acquirerState;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.algaeAngle;

public class AlgaeAcquirerIONeo implements AlgaeAcquirerIO {
  private final SparkMax leftFlyWheel = new SparkMax(Constants.algaeFlywheelLeftNeoCanID, MotorType.kBrushless);
  private final SparkMax rightFlyWheel = new SparkMax(Constants.algaeFlywheelRightNeoCanID, MotorType.kBrushless);
  private final SparkMax algaeAngleMotor = new SparkMax(Constants.algaeAngleMotorNeoCanID, MotorType.kBrushless);
  private final SparkMaxConfig sparkMaxConfig = new SparkMaxConfig();
  // private final StatusSignal<Angle> positionRot = angleEncoder.getPosition();
  // private final StatusSignal<AngularVelocity> velocityRotPerSec = angleEncoder.getVelocity();
  // private final StatusSignal<Voltage> appliedVolts = angleEncoder.getMotorVoltage();
  // private final StatusSignal<Current> currentAmps = angleEncoder.getSupplyCurrent();

  // private final VoltageOut voltageRequest = new VoltageOut(0.0);
  // private final MotionMagicExpoVoltage motionMagic = new MotionMagicExpoVoltage(0);

  DigitalInput topLimitSwitch = new DigitalInput(AlgaeAcquirerConstants.topLimitSwitchDI);
  DigitalInput bottomLimitSwitch = new DigitalInput(AlgaeAcquirerConstants.bottomLimitSwitchDIO);

  public AlgaeAcquirerIONeo() {
    // LimitSwitchConfig limitSwitchConfig = new LimitSwitchConfig();
    // limitSwitchConfig.forwardLimitSwitchEnabled(true);
    // limitSwitchConfig.reverseLimitSwitchEnabled(true);
    ClosedLoopConfig closedLoopConfig = new ClosedLoopConfig();
    closedLoopConfig.maxMotion.maxAcceleration(2000);
    closedLoopConfig.maxMotion.maxVelocity(600);
    closedLoopConfig.maxMotion.positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal);
    closedLoopConfig.maxMotion.allowedClosedLoopError(0.01);
    closedLoopConfig.feedbackSensor(FeedbackSensor.kAlternateOrExternalEncoder);
    closedLoopConfig.pidf(0.2, 0, 0, 0);

    sparkMaxConfig.alternateEncoder.setSparkMaxDataPortConfig();
    sparkMaxConfig.alternateEncoder.countsPerRevolution(8192);

    sparkMaxConfig.idleMode(IdleMode.kCoast).smartCurrentLimit(40).apply(closedLoopConfig);

    // leftFlyWheel.configure(sparkMaxConfig, null, null);
    // rightFlyWheel.configure(sparkMaxConfig, null, null);
    algaeAngleMotor.configure(sparkMaxConfig, null, null);
  }

  @Override
  public void updateInputs(AngleEncoderIOInputs inputs) {
    // BaseStatusSignal.refreshAll(positionRot, velocityRotPerSec, appliedVolts, currentAmps);

    // inputs.positionRad = Units.rotationsToRadians(positionRot.getValueAsDouble());
    // inputs.velocityRadPerSec = Units.rotationsToRadians(velocityRotPerSec.getValueAsDouble());
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

  public void setAlgaeAngle(algaeAngle angle) {
    double anglePosition = 0;
    switch (angle) {
      case STOWED:
        anglePosition = AlgaeAcquirerConstants.stowedAngle;
        break;
      case REEF_ACQUIRE:
        anglePosition = AlgaeAcquirerConstants.reefAcquireAngle;
        break;
      case FLOOR_ACQUIRE:
        anglePosition = AlgaeAcquirerConstants.floorAcquireAngle;
        break;
      case BARGE_SHOOT:
        anglePosition = AlgaeAcquirerConstants.bargeShootAngle;
        break;
      case PROCESSOR_SHOOT:
        anglePosition = AlgaeAcquirerConstants.processorShootAngle;
        break;
    }
    algaeAngleMotor.getClosedLoopController().setReference(anglePosition, null);
  }
}
