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

package frc.robot.subsystems.elevator;

import static frc.robot.subsystems.elevator.ElevatorConstants.*;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.LimitSwitchConfig;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * This Elevator implementation is for a Talon FX driving a motor like the Falon 500 or Kraken X60.
 */
public class ElevatorIONeo implements ElevatorIO {
  private final SparkMax elevator = new SparkMax(ElevatorCanId, MotorType.kBrushless);
  private final SparkMaxConfig sparkMaxConfig = new SparkMaxConfig();
  // private final StatusSignal<Angle> positionRot = elevator.getPosition();
  // private final StatusSignal<AngularVelocity> velocityRotPerSec = elevator.getVelocity();
  // private final StatusSignal<Voltage> appliedVolts = elevator.getMotorVoltage();
  // private final StatusSignal<Current> currentAmps = elevator.getSupplyCurrent();

  // private final VoltageOut voltageRequest = new VoltageOut(0.0);
  // private final MotionMagicExpoVoltage motionMagic = new MotionMagicExpoVoltage(0);

  DigitalInput topLimitSwitch = new DigitalInput(topLimitSwitchDI);
  DigitalInput bottomLimitSwitch = new DigitalInput(bottomLimitSwitchDIO);

  public ElevatorIONeo() {
    LimitSwitchConfig limitSwitchConfig = new LimitSwitchConfig();
    limitSwitchConfig.forwardLimitSwitchEnabled(true);
    limitSwitchConfig.reverseLimitSwitchEnabled(true);
    ClosedLoopConfig closedLoopConfig = new ClosedLoopConfig();
    closedLoopConfig.maxMotion.maxAcceleration(5000);
    closedLoopConfig.maxMotion.maxVelocity(6000);
    closedLoopConfig.maxMotion.positionMode(MAXMotionPositionMode.kMAXMotionTrapezoidal);
    closedLoopConfig.maxMotion.allowedClosedLoopError(2);
    closedLoopConfig.pidf(0.5, 0, 0, 0);

    sparkMaxConfig
        .idleMode(IdleMode.kCoast)
        .smartCurrentLimit(40)
        .apply(limitSwitchConfig)
        .apply(closedLoopConfig);

    elevator.configure(sparkMaxConfig, null, null);
  }

  @Override
  public void updateInputs(ElevatorIOInputs inputs) {
    // BaseStatusSignal.refreshAll(positionRot, velocityRotPerSec, appliedVolts, currentAmps);

    // inputs.positionRad = Units.rotationsToRadians(positionRot.getValueAsDouble());
    // inputs.velocityRadPerSec = Units.rotationsToRadians(velocityRotPerSec.getValueAsDouble());
    // inputs.appliedVolts = appliedVolts.getValueAsDouble();
    // inputs.currentAmps = currentAmps.getValueAsDouble();

    inputs.topLimit = isAtTopLimit();
    inputs.bottomLimit = isAtBottomLimit();
    if (inputs.topLimit) {
      System.out.println("At top: " + inputs.topLimit);
    } else if (inputs.bottomLimit) {
      System.out.println("At bottom: " + inputs.bottomLimit);
    }
  }

  @Override
  public void setPosition(double position) {
    // elevator.setControl(motionMagic.withPosition(position));
    elevator
        .getClosedLoopController()
        .setReference(position, ControlType.kMAXMotionPositionControl);
  }

  @Override
  public double getPosition() {
    return elevator.getEncoder().getPosition();
  }

  @Override
  public boolean isAtBottomLimit() {
    return elevator.getReverseLimitSwitch().isPressed();
  }

  @Override
  public boolean isAtTopLimit() {
    return elevator.getForwardLimitSwitch().isPressed();
  }

  // public void
}
