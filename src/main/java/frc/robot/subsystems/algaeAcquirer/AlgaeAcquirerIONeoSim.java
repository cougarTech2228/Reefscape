package frc.robot.subsystems.algaeAcquirer;

import com.revrobotics.sim.SparkMaxSim;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.Position;

public class AlgaeAcquirerIONeoSim extends AlgaeAcquirerIONeo {
    private final SparkMaxSim angleSim;
    private final SparkMaxSim rightFlySim;
    private final SparkMaxSim leftFlySim;
    
    public AlgaeAcquirerIONeoSim() {
        super();
        angleSim = new SparkMaxSim(algaeAngleMotor, DCMotor.getNeo550(1));
        rightFlySim = new SparkMaxSim(rightFlyWheel, DCMotor.getNeo550(1));
        leftFlySim = new SparkMaxSim(leftFlyWheel, DCMotor.getNeo550(1));
    }

    public void setPosition(Position position) {

        switch (position) {
            case STOWED:
                currentAngleSetPoint = AlgaeAcquirerConstants.stowedAngle;
                break;
            case REEF_ACQUIRE:
                currentAngleSetPoint = AlgaeAcquirerConstants.reefAcquireAngle;
                break;
            case FLOOR_ACQUIRE:
                currentAngleSetPoint = AlgaeAcquirerConstants.floorAcquireAngle;
                break;
            case BARGE_SHOOT:
                currentAngleSetPoint = AlgaeAcquirerConstants.bargeShootAngle;
                break;
            case PROCESSOR_SHOOT:
                currentAngleSetPoint = AlgaeAcquirerConstants.processorShootAngle;
                break;
        }
        
        // algaeAngleMotor.getClosedLoopController().setReference(currentAngleSetPoint, null);
    }

    @Override
    public void setAngleVoltage(double voltage) {
        algaeAngleMotor.setVoltage(voltage);
    }
}
