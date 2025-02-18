package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.coralCone.CoralCone;

public class SetCoralAngleCommand extends Command {
    private CoralCone coralCone;
    private CoralCone.Position position;

    public SetCoralAngleCommand(CoralCone coralCone, CoralCone.Position position) {
        this.coralCone = coralCone;
        this.position = position;
        addRequirements(coralCone);
    }

    @Override
    public void initialize() {
        coralCone.setPosition(position);
    }

    @Override
    public boolean isFinished() {
        return coralCone.isAtSetPosition();
    }
}