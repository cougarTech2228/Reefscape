package frc.robot.subsystems.algaeAcquirer;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.acquirerState;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.algaeAngle;

public interface AlgaeAcquirerIO {
    @AutoLog
    public static class AngleEncoderIOInputs {
        public double positionRad = 0.0;
        public double velocityRadPerSec = 0.0;
        public double appliedVolts = 0.0;
        public double currentAmps = 0.0;
        public boolean bottomLimit = false;
        public boolean topLimit = false;
      }
        /** Update the set of loggable inputs. */
        public default void updateInputs(AngleEncoderIOInputs inputs) {}

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

        public default void setAlgaeAngle(algaeAngle angle) {}








        // public default double getPosition() {
        //     return 0;
        // }

        // /** Run open loop at the specified voltage. */
        // public default void setAngle(double angle) {}

        // public default double getAngle() {
        //     return 0;
        // }

        public default boolean isAtBottomLimit() {
            return false;
        }

        public default boolean isAtTopLimit() {
            return false;
        }
}
