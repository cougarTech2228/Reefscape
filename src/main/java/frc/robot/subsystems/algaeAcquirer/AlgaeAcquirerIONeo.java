package frc.robot.subsystems.algaeAcquirer;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import java.io.OutputStream;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerIO;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerIO.AngleEncoderIOInputs;

public class AlgaeAcquirerIONeo implements AlgaeAcquirerIO {
  private final SparkMax leftFlyWheel = new SparkMax(AlgaeAcquirerConstants.algaeFlywheelLeftNeoCanID, MotorType.kBrushless);
  private final SparkMax rightFlyWheel = new SparkMax(AlgaeAcquirerConstants.algaeFlywheelRightNeoCanID, MotorType.kBrushless);
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

    leftFlyWheel.configure(sparkMaxConfig, null, null);
    rightFlyWheel.configure(sparkMaxConfig, null, null);
  }

  @Override
  public void updateInputs(AngleEncoderIOInputs inputs) {
    // BaseStatusSignal.refreshAll(positionRot, velocityRotPerSec, appliedVolts, currentAmps);

    // inputs.positionRad = Units.rotationsToRadians(positionRot.getValueAsDouble());
    // inputs.velocityRadPerSec = Units.rotationsToRadians(velocityRotPerSec.getValueAsDouble());
    // inputs.appliedVolts = appliedVolts.getValueAsDouble();
    // inputs.currentAmps = currentAmps.getValueAsDouble();
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

  public void acquireAlgae() {
    setVoltageLeft(AlgaeAcquirerConstants.acquireVoltageLeft);
    setVoltageRight(AlgaeAcquirerConstants.acquireVoltageRight);
  }

  public void shootAlgae() {
    setVoltageLeft(AlgaeAcquirerConstants.shootingVoltageLeft);
    setVoltageRight(AlgaeAcquirerConstants.shootingVoltageRight);
  }

  // @Override
  // public double getPosition() {
  //   return motor.getAlternateEncoder().getPosition();
  // }

  // @Override
  // public double set

}
