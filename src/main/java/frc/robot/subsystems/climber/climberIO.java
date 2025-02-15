package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.elevator.Elevator.Position;
import frc.robot.subsystems.elevator.ElevatorIO.ElevatorIOInputs;
import frc.robot.subsystems.climber.Climber.ClimberPosition;

public interface ClimberIO {

    @AutoLog
    public static class ClimberIOInputs {
        public double climberMotorVoltage = 0.0;
        public double climberMotorPosition = 0.0;
        public double climberMotorVelocity = 0.0;
        public double climberMotorCurrent = 0.0;
        public double climberMotorEncoderVelocity = 0.0;
        public double climberMotorEncoderPosition = 0.0;

        public boolean bottomLimit = false;
        public boolean upperLimit = false;
        public boolean climberMotorIsAtSetPosition = false;

    }

    /** Update the set of loggable inputs. */
    public default void updateInputs(ClimberIOInputs inputs) {
    };

    public default void setClimberPosition(ClimberPosition climberPosition) {
    };

    // Only used for sysid runCharacterization
    public default void setVoltage(double output) {
    };

    public default void simulationPeriodic() {
    };

}
