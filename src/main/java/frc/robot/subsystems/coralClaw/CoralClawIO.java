package frc.robot.subsystems.coralClaw;

import org.littletonrobotics.junction.AutoLog;

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

  public default void setPosition(double position) {}

  public default double getPosition() {
    return 0;
  }

  /** Run open loop at the specified voltage. */
  public default void setAngle(double angle) {}

  public default double getAngle() {
    return 0;
  }

  public default boolean isAtBottomLimit() {
    return false;
  }

  public default boolean isAtTopLimit() {
    return false;
  }
}
