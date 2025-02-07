package frc.robot.subsystems.coralCone;

import com.revrobotics.sim.SparkMaxSim;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.subsystems.coralCone.CoralCone.Position;

public class CoralConeIONeoSim extends CoralConeIONeo {
    private final SparkMaxSim angleSim;

    public CoralConeIONeoSim(){
        super();
        angleSim = new SparkMaxSim(angleMotor, DCMotor.getNeo550(1));
    }

    public void setPosition(Position angle) {
        switch (angle) {
            case LOAD:
                currentAngleSetPoint = CoralConeConstants.loadAngle;
                break;
            case L1_SHOOT:
                currentAngleSetPoint = CoralConeConstants.L1Angle;
                break;
            case L2_SHOOT:
                currentAngleSetPoint = CoralConeConstants.L2Angle;
                break;
            case L3_SHOOT:
                currentAngleSetPoint = CoralConeConstants.L3Angle;
                break;
            case L4_SHOOT:
                currentAngleSetPoint = CoralConeConstants.L4Angle;
                break;
        }
        // angleMotor.getClosedLoopController().setReference(currentAngleSetPoint, ControlType.kMAXMotionPositionControl);
    }
}
