package frc.robot.subsystems.algaeAcquirer;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.acquirerState;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.Position;

public interface AlgaeAcquirerIO {
    @AutoLog
    public static class AlgaeAcquirerIOInputs {
        public double positionRad = 0.0;
        public double velocityRadPerSec = 0.0;
        public double appliedVolts = 0.0;
        public double currentAmps = 0.0;
        public boolean bottomLimit = false;
        public boolean topLimit = false;
    }
    /** Update the set of loggable inputs. */
    public default void updateInputs(AlgaeAcquirerIOInputs inputs) {}

    public default double getAlgaeAngle() {
        return 0;
    }

    public default double getVoltageLeft() {
        return 0;
    }

    public default double getVoltageRight() {
        return 0;
    }

    public default void setVoltageLeft(double voltage) {}

    public default void setVoltageRight(double voltage) {}

    public default void setAlgaeAcquirer(acquirerState state) {}

    public default void setPosition(Position position) {}

    public default boolean isAtSetPosition() { return false; }
}
