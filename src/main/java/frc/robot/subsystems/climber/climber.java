<<<<<<< Updated upstream
package frc.robot.subsystems.climber;

import static frc.robot.subsystems.climber.climberConstants.*;
=======
package frc.robot.subsystems.Climber;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

    enum State {
        STOPPED,
        RAISING
        LOWERING
    }


    @AutoLogOutput
    private State currentState = State.STOPPED;

    private final ClimberIO mIO;
    private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();

    private static Climber mInstance = null;

    public static Climber getInstance() {
        if (mInstance == null) {
            switch (ClimberConstants.currentMode) {
                case REAL:
                    mInstance = new Climber(new ClimberIOTalonFX());
                    break;
                case SIM:
                    mInstance = new Climber(new ClimberIOTalonFXSim());
                    break;
                default:
                    mInstance = new Climber(new ClimberIO() {});
                    break;
            }
        }
    }
}
>>>>>>> Stashed changes
