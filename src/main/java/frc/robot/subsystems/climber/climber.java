package frc.robot.subsystems.climber;

import java.util.Optional;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import frc.robot.Constants;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;
import static frc.robot.subsystems.climber.ClimberConstants.*;

import org.littletonrobotics.junction.Logger;

public class Climber extends PIDSubsystem {
    private final ClimberIO io;
    private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();
    // private final SysIdRoutine sysId;

    private static final double kP = 0.1;
    private static final double kI = 0.1;
    private static final double kD = 0.01;
    private static final double kDt = 0.01;
    private static final PIDController pidController = new PIDController(kP, kI, kD, kDt);

    public enum ClimberPosition {
        UP,
        DOWN
    };

    public enum ServoLockPosition {
        LOCKED,
        UNLOCKED
    };

    private Climber(ClimberIO io) {
        super(pidController, 0);

        this.io = io;
        io.setBrakeMode();
        //

        pidController.setTolerance(ClimberConstants.climberAngleThreshold);
        pidController.setIZone(ClimberConstants.kIZone);

        io.setBrakeMode();
    }

    @Override
    protected double getMeasurement() {
        return inputs.currentAngle;
    }

    public void setPosition(ClimberPosition climberPosition) {
        io.setClimberPosition(climberPosition);
    }

    public void manualUp() {
        io.setVoltage(RAISE_VOLTAGE);
    }

    public void manualDown() {
        io.setVoltage(LOWER_VOLTAGE);
    }

    public void stop() {
        io.setVoltage(0);
    }

    @Override
    protected void useOutput(double output, double setpoint) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'useOutput'");
    }

    // @Override
    // protected void useOutput(double output, double setpoint) {
    // // clamp the output to a sane range
    // Logger.recordOutput("ShooterAngleSubsystem/PID/output", output);
    // double val;
    // if (output < 0) {
    // val = Math.max(-kMotorVoltageLimit, output);
    // } else {
    // val = Math.min(kMotorVoltageLimit, output);
    // }
    // if (inputs.isShooterAtTop && val < 0) {
    // mIO.setOutputPercentage(0);
    // } else {
    // mIO.setOutputPercentage(val);
    // }
    // }

    // @AutoLogOutput
    // public ClimberPosition mCurrentPosition = ClimberPosition.DOWN;

    // @AutoLogOutput
    // public ServoLockPosition m_currentServoLockPosition =
    // ServoLockPosition.UNLOCKED;

    // @AutoLogOutput
    // public double mClimberVoltage = 0;

    // @AutoLogOutput
    // public double mServoAngle = 0;

    // public Climber(ClimberIO io) {
    // this.io = io;

    // // Configure SysId
    // }
}
