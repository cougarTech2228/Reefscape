package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.climber.Climber.ClimberPosition;
import frc.robot.subsystems.climber.Climber.ServoLockPosition;

public interface ClimberIO {

    @AutoLog
    public static class ClimberIOInputs {

        public double climberMotorAppliedVoltage = 0.0;
        public double climberMotorPosition = 0.0;
        public double climberMotorVelocity = 0.0;
        public double climberMotorCurrentAmps = 0.0;
        // public double climberMotorEncoderVelocity = 0.0;
        public double climberMotorEncoderPosition = 0.0;

        // public boolean bottomLimit = false;
        // public boolean upperLimit = false;
        public boolean climberMotorIsAtSetPosition = false;

    }

    /** Update the set of loggable inputs. */
    public default void updateInputs(ClimberIOInputs inputs) {
    };

    public default void setClimberPosition(ClimberPosition climberPosition) {
    };

    public default void setServoPosition(ServoLockPosition servoLockPosition) {
    };

    // Only used for sysid runCharacterization
    public default void setVoltage(double output) {
    };

    public default void simulationPeriodic() {
    };

    public default void setBrakeMode() {
    };
}