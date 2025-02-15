package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

<<<<<<< Updated upstream
public interface ClimberIO {
    
}
=======
public interface climberIO {
    
    @AutoLog
    public static class ClimberIOInputs {
        public double climberPosition = 0.0;
        public double climberVelocity = 0.0;
        public double climberAppliedVolts = 0.0;
        public double[] climberCurrentAmps = new double[] {};
    }
    // Updates set of loggable inputs
    public default void updateInputs(ClimberIOInputs inputs) {}

    // Runs climber motor at specified voltage
    public default void setClimberVoltage(double volts) {}

    // Sets to neutral mode
    public default void setNeutralMode(NeutralModeValue neutralMode) {}
}
>>>>>>> Stashed changes
