package frc.robot.subsystems.coralCone;

import com.revrobotics.sim.SparkMaxSim;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.subsystems.coralCone.CoralCone.Position;

public class CoralConeIOSim extends CoralConeIONeo {
    SparkMaxSim angleMotorSim;

    public CoralConeIOSim() {
        angleMotorSim = new SparkMaxSim(super.angleMotor, DCMotor.getNeo550(1));
    }

    @Override
    public void setPosition(Position angle) {
        super.setPosition(angle);
        angleMotorSim.getAbsoluteEncoderSim().setPosition(currentAngleSetPoint);
    }

    @Override
    public void setManualPosition(double position) {
        super.setManualPosition(position);
        angleMotorSim.getAbsoluteEncoderSim().setPosition(currentAngleSetPoint);
    }
}
