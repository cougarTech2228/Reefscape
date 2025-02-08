// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.subsystems.coralClaw;

import frc.robot.subsystems.coralClaw.CoralClawConstants;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.signals.ControlModeValue;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.robot.subsystems.coralClaw.CoralClaw.Angle;
import frc.robot.subsystems.coralClaw.CoralClaw.WheelState;

/**
 * This angleEncoder implementation is for a Talon FX driving a motor like the Falon 500 or Kraken
 * X60.
 */
public class CoralClawIONeo implements CoralClawIO {
  private final SparkMax wheelMotor = new SparkMax(Constants.coralFlywheelCanID, MotorType.kBrushless);
  private final SparkMax angleMotor = new SparkMax(Constants.coralRotateCanID, MotorType.kBrushless);
  private final SparkMaxConfig sparkMaxConfig = new SparkMaxConfig();
  // private final StatusSignal<Angle> positionRot = angleEncoder.getPosition();
  // private final StatusSignal<AngularVelocity> velocityRotPerSec = angleEncoder.getVelocity();
  // private final StatusSignal<Voltage> appliedVolts = angleEncoder.getMotorVoltage();
  // private final StatusSignal<Current> currentAmps = angleEncoder.getSupplyCurrent();

  // private final VoltageOut voltageRequest = new VoltageOut(0.0);
  // private final MotionMagicExpoVoltage motionMagic = new MotionMagicExpoVoltage(0);

  DigitalInput topLimitSwitch = new DigitalInput(CoralClawConstants.topLimitSwitchDI);
  DigitalInput bottomLimitSwitch = new DigitalInput(CoralClawConstants.bottomLimitSwitchDIO);

  public CoralClawIONeo() {
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

    // wheelMotor.configure(sparkMaxConfig, null, null);
    angleMotor.configure(sparkMaxConfig, null, null);
  }

  @Override
  public void updateInputs(AngleEncoderIOInputs inputs) {
    // BaseStatusSignal.refreshAll(positionRot, velocityRotPerSec, appliedVolts, currentAmps);

    // inputs.positionRad = Units.rotationsToRadians(positionRot.getValueAsDouble());
    // inputs.velocityRadPerSec = Units.rotationsToRadians(velocityRotPerSec.getValueAsDouble());
    // inputs.appliedVolts = appliedVolts.getValueAsDouble();
    // inputs.currentAmps = currentAmps.getValueAsDouble();
  }

  // @Override
  // public void setPosition(double position) {
  //   // angleEncoder.setControl(motionMagic.withPosition(position));
  //   wheelMotor.getClosedLoopController().setReference(position, ControlType.kMAXMotionPositionControl);
  // }

  public void setAnglePosition(Angle angle) {
    double anglePosition = 0;
    switch (angle) {
      case LOAD_ANGLE:
        anglePosition = CoralClawConstants.loadAngle;
        break;
      case L1_SHOOT_ANGLE:
        anglePosition = CoralClawConstants.L1Angle;
        break;
      case L2_SHOOT_ANGLE:
        anglePosition = CoralClawConstants.L2Angle;
        break;
      case L3_SHOOT_ANGLE:
        anglePosition = CoralClawConstants.L3Angle;
        break;
      case L4_SHOOT_ANGLE:
        anglePosition = CoralClawConstants.L4Angle;
        break;
    }
    angleMotor.getClosedLoopController().setReference(anglePosition, ControlType.kMAXMotionPositionControl);
  }

  @Override
  public double getAngle() {
    return angleMotor.getEncoder().getPosition();
  }
  
  public void setWheel(WheelState state) {
    double motorVoltage = 0;
    switch (state) {
      case FORWARD:
        motorVoltage = CoralClawConstants.forwardVoltage;
        break;
      case REVERSE:
        motorVoltage = CoralClawConstants.reverseVoltage;
        break;
      case TRANSIT:
        motorVoltage = CoralClawConstants.transitVoltage;
        break;
    }
    wheelMotor.setVoltage(motorVoltage);
  }
  
  public double getWheelVoltage() {
    return wheelMotor.getAppliedOutput();
  }


}
