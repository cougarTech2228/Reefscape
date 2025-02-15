package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;
import static frc.robot.subsystems.climber.ClimberConstants.*;

import org.littletonrobotics.junction.Logger;

public class Climber extends SubsystemBase {
    // private final ClimberIO io;
    private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();
    // private final SysIdRoutine sysId;

    public enum ClimberPosition {
        UP,
        DOWN
    };

    // public Climber(ClimberIO io) {
    // this.io = io;

    // // Configure SysId
    // }
}
