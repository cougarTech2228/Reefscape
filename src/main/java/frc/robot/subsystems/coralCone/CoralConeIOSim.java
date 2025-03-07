package frc.robot.subsystems.coralCone;

import com.revrobotics.sim.SparkMaxSim;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.subsystems.coralCone.CoralCone.Position;

public class CoralConeIOSim extends CoralConeIONeo {
    SparkMaxSim angleMotorSim;
    private PIDController pidController = new PIDController(0.08, 0, 0);

    public CoralConeIOSim() {
        angleMotorSim = new SparkMaxSim(super.angleMotor, DCMotor.getNeo550(1));
    }

    @Override
    public void setPosition(Position angle) {
        super.setPosition(angle);
        pidController.setSetpoint(currentAngleSetPoint);
    }

    @Override
    public void setManualPosition(double position) {
        super.setManualPosition(position);
        pidController.setSetpoint(currentAngleSetPoint);
    }

    @Override
    public void updateInputs(CoralConeIOInputs inputs) {
        double input = angleMotorSim.getAbsoluteEncoderSim().getPosition();
        double output = pidController.calculate(input);
        angleMotorSim.getAbsoluteEncoderSim().setPosition(input + output);
        super.updateInputs(inputs);
    }
}
