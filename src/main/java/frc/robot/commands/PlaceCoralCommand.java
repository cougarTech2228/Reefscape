package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ReefLocation;
import frc.robot.Constants.ReefSegment;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class PlaceCoralCommand extends Command {
 
    private final ReefLocation location;
    private final ReefSegment segment;
    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;

    public PlaceCoralCommand(
        ReefSegment segment, ReefLocation location, Elevator elevator,
        AlgaeAcquirer algaeAcquirer, CoralCone coralCone)
    {
        this.segment = segment;
        this.location = location;
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
