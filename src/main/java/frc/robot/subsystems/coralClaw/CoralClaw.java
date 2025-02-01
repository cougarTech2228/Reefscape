package frc.robot.subsystems.coralClaw;
import frc.robot.generated.subsystems.angleEncoder.CoralClawIOInputsAutoLogged;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralClaw extends SubsystemBase {
  private final CoralClawIO io;
  private CoralClawIOInputsAutoLogged inputs = new CoralClawIOInputsAutoLogged();

  public CoralClaw(CoralClawIO io) {
    this.io = io;
  }

  public void setPosition(double position) {
    io.setPosition(position);
  }
}
