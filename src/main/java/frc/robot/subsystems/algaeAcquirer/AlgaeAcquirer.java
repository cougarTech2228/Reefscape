package frc.robot.subsystems.algaeAcquirer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.generated.subsystems.angleEncoder.CoralClawIOInputsAutoLogged;

public class AlgaeAcquirer extends SubsystemBase {
    private final AlgaeAcquirerIO io;
    private CoralClawIOInputsAutoLogged inputs = new CoralClawIOInputsAutoLogged();

    public AlgaeAcquirer(AlgaeAcquirerIO io) {
        this.io = io;
    }

    public void setPosition(double position) {
        io.setPosition(position);
    }
}
