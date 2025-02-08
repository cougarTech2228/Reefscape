package frc.robot.subsystems.coralClaw;
import frc.robot.generated.subsystems.angleEncoder.CoralClawIOInputsAutoLogged;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralClaw extends SubsystemBase {
  private final CoralClawIO io;
  private CoralClawIOInputsAutoLogged inputs = new CoralClawIOInputsAutoLogged();

  public enum Angle {
    LOAD_ANGLE,
    L1_SHOOT_ANGLE,
    L2_SHOOT_ANGLE,
    L3_SHOOT_ANGLE,
    L4_SHOOT_ANGLE
  };

  public enum WheelState {
    // LOAD,
    // TRANSIT,
    // SHOOT
    FORWARD,
    REVERSE,
    TRANSIT
  }

  public CoralClaw(CoralClawIO io) {
    this.io = io;
  }

  public void setAnglePosition(Angle angle) {
    io.setAnglePosition(angle);
  }

  public void setWheel(WheelState state) {
    io.setWheel(state);
  }
}
