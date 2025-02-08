package frc.robot.subsystems.coralClaw;

import org.littletonrobotics.junction.AutoLog;

import frc.robot.subsystems.coralClaw.CoralClaw.WheelState;
import frc.robot.subsystems.coralClaw.CoralClaw.Angle;

public interface CoralClawIO {
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

  /** Run open loop at the specified voltage. */
  public default void setAnglePosition(Angle angle) {}

  public default double getAngle() {
    return 0;
  }

  public default void setWheel(WheelState state) {}

  public default double getWheelVoltage() {
    return 0;
  }

  public default boolean isAtBottomLimit() {
    return false;
  }

  public default boolean isAtTopLimit() {
    return false;
  }
}
