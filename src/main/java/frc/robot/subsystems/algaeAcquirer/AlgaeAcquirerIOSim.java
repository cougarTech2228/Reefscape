package frc.robot.subsystems.algaeAcquirer;

import com.revrobotics.sim.SparkMaxSim;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.Position;

public class AlgaeAcquirerIOSim extends AlgaeAcquirerIONeo {
        SparkMaxSim angleMotorSim;

    public AlgaeAcquirerIOSim() {
        angleMotorSim = new SparkMaxSim(super.algaeAngleMotor, DCMotor.getNeo550(1));
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
        angleMotorSim.getAbsoluteEncoderSim().setPosition(currentAngleSetPoint);
    }

    @Override
    public void setManualPosition(double position) {
        super.setManualPosition(position);
        angleMotorSim.getAbsoluteEncoderSim().setPosition(currentAngleSetPoint);
    }
}
