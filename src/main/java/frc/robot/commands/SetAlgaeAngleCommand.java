package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;

public class SetAlgaeAngleCommand extends Command {
    private AlgaeAcquirer algaeAcquirer;
    private AlgaeAcquirer.Position position;

    public SetAlgaeAngleCommand(AlgaeAcquirer algaeAcquirer, AlgaeAcquirer.Position position) {
        this.algaeAcquirer = algaeAcquirer;
        this.position = position;
        addRequirements(algaeAcquirer);
    }

    @Override
    public void initialize() {
        algaeAcquirer.setPosition(position);
    }

    @Override
    public boolean isFinished() {
        return algaeAcquirer.isAtSetPosition();
    }
}
