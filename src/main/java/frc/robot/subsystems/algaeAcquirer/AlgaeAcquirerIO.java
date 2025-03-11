package frc.robot.subsystems.algaeAcquirer;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.FlywheelState;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer.Position;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerConstants.FlyMode;

public interface AlgaeAcquirerIO {
    @AutoLog
    public static class AlgaeAcquirerIOInputs {
        public double anglePosition = 0.0;
        public double angleEncoderPosition = 0.0;
        public double angleEncoderVelocity = 0.0;
        public double angleSetPoition = 0.0;
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

        public boolean isLoaded = false;
        public String flyState = "";
    }
    /** Update the set of loggable inputs. */
    public default void updateInputs(AlgaeAcquirerIOInputs inputs) {}

    public default void setFlyVoltage(double voltage) {}

    public default void setAlgaeAcquirer(FlywheelState state) {}

    public default void setPosition(Position position) {}

    public default void setAngleVoltage(double voltage) {}

    public default void setManualPosition(double position){};
}
