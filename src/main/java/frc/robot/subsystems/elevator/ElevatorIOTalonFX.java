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
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ForwardLimitValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.subsystems.elevator.Elevator.Position;

import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecondPerSecond;
import static edu.wpi.first.units.Units.Second;

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
    private final TorqueCurrentFOC torqueCurrentRequest = new TorqueCurrentFOC(0);


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
        slot0Configs.kS = 2; // Add 0.25 V output to overcome static friction
        slot0Configs.kV = 0; // A velocity target of 1 rps results in 0.12 V output
        slot0Configs.kA = 0; // An acceleration of 1 rps/s requires 0.01 V output
        slot0Configs.kP = 20; // A position error of 2.5 rotations results in 12 V output
        slot0Configs.kI = 0; // no output for integrated error
        slot0Configs.kD = 0.1; // A velocity error of 1 rps results in 0.1 V output
        slot0Configs.GravityType = GravityTypeValue.Elevator_Static;

        // set Motion Magic Expo settings
        talonFXConfigs.MotionMagic
            .withMotionMagicCruiseVelocity(RotationsPerSecond.of(20)) // 5 (mechanism) rotations per second cruise
            .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(0.5)) // Take approximately 0.5 seconds to reach max vel
            // Take approximately 0.1 seconds to reach max accel 
            .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(3500));

        talonFXConfigs.MotionMagic.MotionMagicExpo_kV = ElevatorConstants.velocity; // kV is around 0.12 V/rps
        talonFXConfigs.MotionMagic.MotionMagicExpo_kA = ElevatorConstants.acceleration; // Use a slower kA of 0.1
                                                                                        // V/(rps/s)
        // talonFXConfigs.MotionMagic.MotionMagicExpo_kV = ElevatorConstants.velocityFast; // kV is around 0.12 V/rps
        // talonFXConfigs.MotionMagic.MotionMagicExpo_kA = ElevatorConstants.accelerationFast; // Use a slower kA of 0.1

        talonFXConfigs.CurrentLimits.SupplyCurrentLimit = currentLimit;
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
        inputs.isAtSetPosition = elevatorA.getClosedLoopError().getValue() < ClosedLoopErrorThreshold;
        inputs.setPosition = elevatorA.getClosedLoopReference().getValueAsDouble();
        inputs.pidOutput = elevatorA.getClosedLoopOutput().getValueAsDouble();
    }

    @Override
    public void setPosition(Position position) {
        double motorPosition = 0;
        switch (position) {
            case TRANSIT:
                motorPosition = HEIGHT_TRANSIT;
            case ALGAE_BARGE:
                motorPosition = HEIGHT_ALGAE_BARGE;
                break;
            case ALGAE_FLOOR:
                motorPosition = HEIGHT_ALGAE_FLOOR;
                break;
            case ALGAE_PROCESSOR:
                motorPosition = HEIGHT_ALGAE_PROCESSOR;
                break;
            case ALGAE_REEF_HIGH:
                motorPosition = HEIGHT_ALGAE_REEF_HIGH;
                break;
            case ALGAE_REEF_LOW:
                motorPosition = HEIGHT_ALGAE_REEF_LOW;
                break;
            case CORAL_L1:
                motorPosition = HEIGHT_CORAL_L1;
                break;
            case CORAL_L2:
                motorPosition = HEIGHT_CORAL_L2;
                break;
            case CORAL_L3:
                motorPosition = HEIGHT_CORAL_L3;
                break;
            case CORAL_L4:
                motorPosition = HEIGHT_CORAL_L4;
                break;
            case CORAL_LOAD:
                motorPosition = HEIGHT_CORAL_LOAD;
                break;
            default:
                break;
        }
        elevatorA.setControl(motionMagic.withPosition(motorPosition));
    }

    @Override
    public void setVoltage(double output) {
        // elevatorA.setControl(torqueCurrentRequest.withOutput(output));
        elevatorA.setVoltage(output);

        System.out.println("Voltage: " + output);
    }
}
