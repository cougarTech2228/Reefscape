package frc.robot.subsystems.climber;

import static frc.robot.subsystems.climber.ClimberConstants.angleExtended;
import static frc.robot.subsystems.climber.ClimberConstants.angleRetracted;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private final ClimberIO io;
    private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();

    private static final double kP = 100;
    private static final double kI = 0.0;
    private static final double kD = 0.0;
    private static final PIDController pidController = new PIDController(kP, kI, kD, 0.02);
    private boolean m_enabled = false;
    double setpoint = 0;

    public enum ClimberPosition {
        UP,
        DOWN
    };

    public enum ServoLockPosition {
        LOCKED,
        UNLOCKED
    };

    public Climber(ClimberIO io) {

        this.io = io;

        pidController.setTolerance(ClimberConstants.climberAngleThreshold);
        pidController.setIZone(ClimberConstants.kIZone);
        setpoint = angleRetracted;
        // setSetpoint(setpoint);
    }


    public void setClimberPosition(ClimberPosition climberPosition) {
        // io.setClimberPosition(climberPosition);
        switch (climberPosition) {
            case DOWN:
                setpoint = angleRetracted;
                break;
            case UP:
                setpoint = angleExtended;
                break;
        }
        m_enabled = true;
    }

    public void setServoPosition(ServoLockPosition servoLockPosition) {
        io.setServoPosition(servoLockPosition);
    }

    public void setVoltage(double output) {
        io.setVoltage(output);
    }

    public void manualClimberUp() {
        io.setVoltage(ClimberConstants.RAISE_VOLTAGE);
    }

    public void manualClimberDown() {
        io.setVoltage(ClimberConstants.LOWER_VOLTAGE);
    }

    public void stop() {
        io.setVoltage(0);
        m_enabled = false;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Climber", inputs);

        if (m_enabled) {
            double voltage = pidController.calculate(inputs.climberMotorEncoderPosition, setpoint);
            inputs.pidSetpoint = setpoint;
            inputs.pidOutput = voltage;
            double clamp = 8;
            if (voltage > clamp) {
                voltage = clamp;
            }
            if (voltage < -clamp) {
                voltage = -clamp;
            }
            inputs.pidOutputClamped = voltage;
            setVoltage(voltage);
        }
    }

      /** Enables the PID control. Resets the controller. */
    public void enable() {
        m_enabled = true;
        pidController.reset();
    }

  /** Disables the PID control. Sets output to zero. */
    public void disable() {
        m_enabled = false;
        setVoltage(0);
    }
}
