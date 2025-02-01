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
import static frc.robot.util.PhoenixUtil.*;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.HardwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ForwardLimitValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.ReverseLimitValue;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * This Elevator implementation is for a Talon FX driving a motor like the Falon 500 or Kraken X60.
 */
public class ElevatorIOTalonFX implements ElevatorIO {
  private final TalonFX elevator = new TalonFX(ElevatorCanId);
  private final StatusSignal<Angle> positionRot = elevator.getPosition();
  private final StatusSignal<AngularVelocity> velocityRotPerSec = elevator.getVelocity();
  private final StatusSignal<Voltage> appliedVolts = elevator.getMotorVoltage();
  private final StatusSignal<Current> currentAmps = elevator.getSupplyCurrent();

  // private final VoltageOut voltageRequest = new VoltageOut(0.0);
  private final MotionMagicExpoVoltage motionMagic = new MotionMagicExpoVoltage(0);

  DigitalInput topLimitSwitch = new DigitalInput(topLimitSwitchDI);
  DigitalInput bottomLimitSwitch = new DigitalInput(bottomLimitSwitchDIO);

  public ElevatorIOTalonFX() {

    // in init function
    var talonFXConfigs = new TalonFXConfiguration();

    // set slot 0 gains
    var slot0Configs = talonFXConfigs.Slot0;
    slot0Configs.kS = 0; // Add 0.25 V output to overcome static friction
    slot0Configs.kV = 0; // A velocity target of 1 rps results in 0.12 V output
    slot0Configs.kA = 0; // An acceleration of 1 rps/s requires 0.01 V output
    slot0Configs.kP = 10; // A position error of 2.5 rotations results in 12 V output
    slot0Configs.kI = 0; // no output for integrated error
    slot0Configs.kD = 0; // A velocity error of 1 rps results in 0.1 V output

    // set Motion Magic Expo settings
    var motionMagicConfigs = talonFXConfigs.MotionMagic;
    motionMagicConfigs.MotionMagicCruiseVelocity = 0; // Unlimited cruise velocity
    if (true) {
      motionMagicConfigs.MotionMagicExpo_kV =
          ElevatorConstants.velocitySlow; // kV is around 0.12 V/rps
      motionMagicConfigs.MotionMagicExpo_kA =
          ElevatorConstants.accelerationSlow; // Use a slower kA of 0.1 V/(rps/s)
    } else {
      motionMagicConfigs.MotionMagicExpo_kV =
          ElevatorConstants.velocityFast; // kV is around 0.12 V/rps
      motionMagicConfigs.MotionMagicExpo_kA =
          ElevatorConstants.accelerationFast; // Use a slower kA of 0.1 V/(rps/s)
    }

    var hardwareLimitSwitchConfig = new HardwareLimitSwitchConfigs();
    hardwareLimitSwitchConfig.ForwardLimitAutosetPositionEnable = true;
    hardwareLimitSwitchConfig.ForwardLimitAutosetPositionValue = ElevatorConstants.topPosition;
    hardwareLimitSwitchConfig.ReverseLimitAutosetPositionEnable = true;
    hardwareLimitSwitchConfig.ReverseLimitAutosetPositionValue = ElevatorConstants.bottomPosition;
    hardwareLimitSwitchConfig.ForwardLimitEnable = true;
    hardwareLimitSwitchConfig.ReverseLimitEnable = true;

    // -------------
    // ---------------
    // var config = new TalonFXConfiguration();
    talonFXConfigs.CurrentLimits.SupplyCurrentLimit = currentLimit;
    talonFXConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
    talonFXConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    elevator.getConfigurator().apply(talonFXConfigs);
    elevator.getConfigurator().apply(hardwareLimitSwitchConfig);

    // tryUntilOk(5, () -> elevator.getConfigurator().apply(config, 0.25));

    BaseStatusSignal.setUpdateFrequencyForAll(
        50.0, positionRot, velocityRotPerSec, appliedVolts, currentAmps);
    elevator.optimizeBusUtilization();
  }

  @Override
  public void updateInputs(ElevatorIOInputs inputs) {
    BaseStatusSignal.refreshAll(positionRot, velocityRotPerSec, appliedVolts, currentAmps);

    inputs.positionRad = Units.rotationsToRadians(positionRot.getValueAsDouble());
    inputs.velocityRadPerSec = Units.rotationsToRadians(velocityRotPerSec.getValueAsDouble());
    inputs.appliedVolts = appliedVolts.getValueAsDouble();
    inputs.currentAmps = currentAmps.getValueAsDouble();

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
    elevator.setControl(motionMagic.withPosition(position));
  }

  @Override
  public double getPosition() {
    return elevator.getPosition().getValueAsDouble();
  }

  @Override
  public boolean isAtBottomLimit() {
    return elevator.getReverseLimit().getValue() == ReverseLimitValue.ClosedToGround;
  }

  @Override
  public boolean isAtTopLimit() {
    return elevator.getForwardLimit().getValue() == ForwardLimitValue.ClosedToGround;
  }
}
