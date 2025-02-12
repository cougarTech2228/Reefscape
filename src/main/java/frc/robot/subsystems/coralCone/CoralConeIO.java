package frc.robot.subsystems.coralCone;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.units.measure.Voltage;
import frc.robot.subsystems.coralCone.CoralCone.Position;
import frc.robot.subsystems.coralCone.CoralCone.WheelState;

public interface CoralConeIO {
    @AutoLog
    public static class CoralConeIOInputs {
        public double angleMotorVoltage = 0.0;
        public double angleMotorPosition = 0.0;
        public double angleMotorVelocity = 0.0;
        public double angleMotorCurrent = 0.0;
        public double angleMotorEncoderVelocity = 0.0;
        public double angleMotorEncoderPosition = 0.0;
        public boolean angleMotorIsAtSetPosition = false;
        public double wheelVoltage = 0.0;
        public boolean isLoaded = false;
    }

    /** Update the set of loggable inputs. */
    public default void updateInputs(CoralConeIOInputs inputs) {
    }

    /** Run open loop at the specified voltage. */
    public default void setPosition(Position position) {
    }

    public default void setWheel(WheelState state) {
    }

    public default void setAngleVoltage(double kManualUpVvoltage) {        
    }
}
