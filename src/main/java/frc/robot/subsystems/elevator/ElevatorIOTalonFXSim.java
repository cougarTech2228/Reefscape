package frc.robot.subsystems.elevator;

import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.Voltage;

public class ElevatorIOTalonFXSim extends ElevatorIOTalonFX {

    public ElevatorIOTalonFXSim() {
        super();
        elevatorA.getSimState().setSupplyVoltage(Voltage.ofBaseUnits(12, Volts));
        elevatorB.getSimState().setSupplyVoltage(Voltage.ofBaseUnits(12, Volts));
    }

    @Override
    public void simulationPeriodic() {
        
    }
}
