package frc.robot.subsystems.algaeAcquirer;

import com.revrobotics.sim.SparkMaxSim;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.Position;

public class AlgaeAcquirerIOSim extends AlgaeAcquirerIONeo {
    SparkMaxSim angleMotorSim;
    private PIDController pidController = new PIDController(0.08, 0, 0);

    public AlgaeAcquirerIOSim() {
        angleMotorSim = new SparkMaxSim(super.algaeAngleMotor, DCMotor.getNeo550(1));
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
        pidController.setSetpoint(currentAngleSetPoint);
    }

    @Override
    public void setManualPosition(double position) {
        super.setManualPosition(position);
        pidController.setSetpoint(currentAngleSetPoint);
    }

    @Override
    public void updateInputs(AlgaeAcquirerIOInputs inputs) {
        double input = angleMotorSim.getAbsoluteEncoderSim().getPosition();
        double output = pidController.calculate(input);
        angleMotorSim.getAbsoluteEncoderSim().setPosition(input + output);
        super.updateInputs(inputs);
    }
}
