package frc.robot.subsystems.elevator;

import frc.robot.subsystems.elevator.Elevator.Position;

public class ElevatorIOSim extends ElevatorIOTalonFX {

    public ElevatorIOSim() {
        elevatorA.getSimState().setForwardLimit(true);
    }

    public void setPosition(Position position) {
        elevatorA.getSimState().setForwardLimit(false);
        super.setPosition(position);
        elevatorA.getSimState().setRawRotorPosition(currentSetPosition);
    }

    @Override
    public void setManualPosition(double position) {
        super.setManualPosition(position);
        elevatorA.getSimState().setRawRotorPosition(currentSetPosition);
    }

}
