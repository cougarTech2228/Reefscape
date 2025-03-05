package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.controls.VoltageOut;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.subsystems.elevator.Elevator.Position;

public class ElevatorIOSim extends ElevatorIOTalonFX {
    private PIDController pidController = new PIDController(0.1, 0, 0);

    public ElevatorIOSim() {
        elevatorA.getSimState().setForwardLimit(true);
    }

    public void setPosition(Position position) {
        elevatorA.getSimState().setForwardLimit(false);
        super.setPosition(position);
        pidController.setSetpoint(currentSetPosition);
    }

    @Override
    public void setManualPosition(double position) {
        elevatorA.getSimState().setForwardLimit(false);
        super.setManualPosition(position);
        pidController.setSetpoint(currentSetPosition);
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
        double input = elevatorA.getRotorPosition().getValueAsDouble();
        double output = pidController.calculate(input);
        elevatorA.setControl(new VoltageOut(output));
        if (output > 3) {
            output = 3;
        } else if (output < -3) {
            output = -3;
        }
        elevatorA.getSimState().addRotorPosition(output);
        super.updateInputs(inputs);
    }
}
