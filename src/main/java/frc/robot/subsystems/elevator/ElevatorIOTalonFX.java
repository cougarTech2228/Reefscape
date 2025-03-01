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
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.HardwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ForwardLimitValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.Constants;
import frc.robot.subsystems.elevator.Elevator.Position;

import static edu.wpi.first.units.Units.RotationsPerSecond;

/**
 * This Elevator implementation is for a Talon FX driving a motor like the Falon
 * 500 or Kraken X60.
 */
public class ElevatorIOTalonFX implements ElevatorIO {
    protected final TalonFX elevatorA = new TalonFX(elevatorACanID, "canivore");
    protected final TalonFX elevatorB = new TalonFX(elevatorBCanID, "canivore");

    private final StatusSignal<Angle> positionRotA = elevatorA.getPosition();
    private final StatusSignal<AngularVelocity> velocityRotPerSecA = elevatorA.getVelocity();
    private final StatusSignal<Voltage> appliedVoltsA = elevatorA.getMotorVoltage();
    private final StatusSignal<Current> currentAmpsA = elevatorA.getSupplyCurrent();
    private final StatusSignal<ForwardLimitValue> forwardLimitA = elevatorA.getForwardLimit();

    private final StatusSignal<Angle> positionRotB = elevatorB.getPosition();
    private final StatusSignal<AngularVelocity> velocityRotPerSecB = elevatorB.getVelocity();
    private final StatusSignal<Voltage> appliedVoltsB = elevatorB.getMotorVoltage();
    private final StatusSignal<Current> currentAmpsB = elevatorB.getSupplyCurrent();

    private final MotionMagicExpoVoltage motionMagic = new MotionMagicExpoVoltage(0);

    private double currentSetPosition = 0;

    public ElevatorIOTalonFX() {
        var hardwareLimitSwitchConfig = new HardwareLimitSwitchConfigs();

        //Note: Forward is actually down!
        hardwareLimitSwitchConfig.ForwardLimitAutosetPositionEnable = true;
        hardwareLimitSwitchConfig.ForwardLimitAutosetPositionValue = ElevatorConstants.bottomPosition;
        hardwareLimitSwitchConfig.ForwardLimitEnable = true;
        hardwareLimitSwitchConfig.ReverseLimitEnable = false;

        // in init function
        var talonFXConfigs = new TalonFXConfiguration();
        talonFXConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        // talonFXConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
        // talonFXConfigs.CurrentLimits.SupplyCurrentLimit = currentLimit;

        talonFXConfigs.TorqueCurrent.PeakForwardTorqueCurrent = ElevatorConstants.slipCurrent;
        talonFXConfigs.TorqueCurrent.PeakReverseTorqueCurrent = -ElevatorConstants.slipCurrent;
        talonFXConfigs.CurrentLimits.StatorCurrentLimit = ElevatorConstants.slipCurrent;
        talonFXConfigs.CurrentLimits.StatorCurrentLimitEnable = true;

        elevatorA.getConfigurator().apply(talonFXConfigs);
        elevatorA.getConfigurator().apply(hardwareLimitSwitchConfig);

        elevatorB.getConfigurator().apply(talonFXConfigs);
        elevatorB.setControl(new Follower(elevatorACanID, false));

        // set slot 0 gains
        var slot0Configs = talonFXConfigs.Slot0;
        slot0Configs.kS = 2;
        slot0Configs.kV = 0;
        slot0Configs.kA = 0;
        slot0Configs.kP = 30;
        slot0Configs.kI = 0;
        slot0Configs.kD = 0.1;
        slot0Configs.kG = 0;
        slot0Configs.GravityType = GravityTypeValue.Elevator_Static;

        // set Motion Magic Expo settings
        talonFXConfigs.MotionMagic
            .withMotionMagicCruiseVelocity(RotationsPerSecond.of(100)) // 5 (mechanism) rotations per second cruise
        
            .withMotionMagicExpo_kA(0.20) // lower is faster
            .withMotionMagicExpo_kV(0.01); // lower is faster

        talonFXConfigs.CurrentLimits.SupplyCurrentLimit = 100; // allow a spike of 80A
        talonFXConfigs.CurrentLimits.SupplyCurrentLowerLimit = 40; // typical current limit
        talonFXConfigs.CurrentLimits.SupplyCurrentLowerTime = 1; // max allowed spike durration (seconds)
        talonFXConfigs.CurrentLimits.SupplyCurrentLimitEnable = true;
        talonFXConfigs.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        elevatorA.getConfigurator().apply(talonFXConfigs);
        elevatorA.getConfigurator().apply(hardwareLimitSwitchConfig);

        BaseStatusSignal.setUpdateFrequencyForAll(50.0,
            positionRotA, velocityRotPerSecA, appliedVoltsA, currentAmpsA,
            positionRotB, velocityRotPerSecB, appliedVoltsB, currentAmpsB,
            forwardLimitA);
        elevatorA.optimizeBusUtilization();
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
        BaseStatusSignal.refreshAll(
            positionRotA, velocityRotPerSecA, appliedVoltsA, currentAmpsA,
            positionRotB, velocityRotPerSecB, appliedVoltsB, currentAmpsB,
            forwardLimitA);

        inputs.position_A = positionRotA.getValueAsDouble();
        inputs.velocity_A = velocityRotPerSecA.getValueAsDouble();
        inputs.appliedVolts_A = appliedVoltsA.getValueAsDouble();
        inputs.currentAmps_A = currentAmpsA.getValueAsDouble();

        inputs.position_B = positionRotB.getValueAsDouble();
        inputs.velocity_B = velocityRotPerSecB.getValueAsDouble();
        inputs.appliedVolts_B = appliedVoltsB.getValueAsDouble();
        inputs.currentAmps_B = currentAmpsB.getValueAsDouble();

        inputs.bottomLimit = forwardLimitA.getValue() == ForwardLimitValue.ClosedToGround;
        inputs.closedLoopError = elevatorA.getClosedLoopError().getValueAsDouble();
        inputs.setPosition = elevatorA.getClosedLoopReference().getValueAsDouble();
        if (Constants.currentMode == Constants.Mode.SIM) {
            inputs.isAtSetPosition = true;
        } else {
            inputs.isAtSetPosition = Math.abs(inputs.position_A - currentSetPosition) < ClosedLoopErrorThreshold;
        }
        inputs.pidOutput = elevatorA.getClosedLoopOutput().getValueAsDouble();
    }

    @Override
    public void setPosition(Position position) {
        switch (position) {
            case TRANSIT:
                currentSetPosition = HEIGHT_TRANSIT;
                break;
            case ALGAE_BARGE:
                currentSetPosition = HEIGHT_ALGAE_BARGE;
                break;
            case ALGAE_FLOOR:
                currentSetPosition = HEIGHT_ALGAE_FLOOR;
                break;
            case ALGAE_FLOOR_ON_CORAL:
                currentSetPosition = HEIGHT_ALGAE_ON_CORAL;
                break;
            case ALGAE_PROCESSOR:
                currentSetPosition = HEIGHT_ALGAE_PROCESSOR;
                break;
            case ALGAE_REEF_HIGH:
                currentSetPosition = HEIGHT_ALGAE_REEF_HIGH;
                break;
            case ALGAE_REEF_LOW:
                currentSetPosition = HEIGHT_ALGAE_REEF_LOW;
                break;
            case CORAL_L1:
                currentSetPosition = HEIGHT_CORAL_L1;
                break;
            case CORAL_L2:
                currentSetPosition = HEIGHT_CORAL_L2;
                break;
            case CORAL_L3:
                currentSetPosition = HEIGHT_CORAL_L3;
                break;
            case CORAL_L4:
                currentSetPosition = HEIGHT_CORAL_L4;
                break;
            case CORAL_LOAD:
                currentSetPosition = HEIGHT_CORAL_LOAD;
                break;
        }
        elevatorA.setControl(motionMagic.withPosition(currentSetPosition));
    }

    @Override
    public void setVoltage(double output) {
        // elevatorA.setControl(torqueCurrentRequest.withOutput(output));
        elevatorA.setVoltage(output);

        System.out.println("Voltage: " + output);
    }

    @Override
    public void setManualPosition(double position) {
        currentSetPosition = position;
        elevatorA.setControl(motionMagic.withPosition(currentSetPosition));
    }
}
