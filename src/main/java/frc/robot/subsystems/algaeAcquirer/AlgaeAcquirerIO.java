package frc.robot.subsystems.algaeAcquirer;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.acquirerState;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.Position;

public interface AlgaeAcquirerIO {
    @AutoLog
    public static class AlgaeAcquirerIOInputs {
        public double anglePosition = 0.0;
        public double angleVelocity = 0.0;
        public double angleAppliedVolts = 0.0;
        public double angleCurrentAmps = 0.0;
        public boolean angleIsAtSetPosition = false;
        
        public double flyAppliedVoltsLeft = 0.0;
        public double flyVelocityLeft = 0.0;
        public double flyCurrentAmpsLeft = 0.0;

        public double flyAppliedVoltsRight = 0.0;
        public double flyVelocityRight = 0.0;
        public double flyCurrentAmpsRight = 0.0;
    }
    /** Update the set of loggable inputs. */
    public default void updateInputs(AlgaeAcquirerIOInputs inputs) {}

    public default void setFlyVoltage(double voltage) {}

    public default void setAlgaeAcquirer(acquirerState state) {}

    public default void setPosition(Position position) {}

    public default void setAngleVoltage(double voltage) {}
}
