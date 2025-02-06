package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.algaeAcquirer.*;
import frc.robot.subsystems.coralClaw.*;
import frc.robot.subsystems.elevator.*;
import frc.robot.subsystems.vision.*;


public class ButtonBoard  {
    public static final double FINE_STRAFE_DISTANCE_CM = 3.0;

    private enum ButtonBoardOperationMode {
        Drive,
        Camera
    }

    private final int kJoystickChannel1 = 1;
    private final int kJoystickChannel2 = 2;

    private Joystick m_joystick1;
    private Joystick m_joystick2;

    private ButtonBoardOperationMode m_operationMode;

    private static ButtonBoard mInstance = null;

    public static ButtonBoard getInstance() {
        if (mInstance == null) {
            mInstance = new ButtonBoard();
        }
        return mInstance;
    }

    private ButtonBoard() {
        m_joystick1 = new Joystick(kJoystickChannel1);
        m_joystick2 = new Joystick(kJoystickChannel2);
    }
    
    // Joystick #1 Buttons

    private JoystickButton lowerElevatorButton() {
        return new JoystickButton(m_joystick2, 5);
    }

    private JoystickButton raiseElevatorButton() {
        return new JoystickButton(m_joystick2, 3);
    }

    private JoystickButton climbButton() {
        return new JoystickButton(m_joystick2, 4);
    }

    private JoystickButton descendButton() {
        return new JoystickButton(m_joystick2, 6);
    }

    private JoystickButton acquireAlgaeButton() {
        return new JoystickButton(m_joystick2, 2);
    }

    private JoystickButton shootAlgaeButton() {
        return new JoystickButton(m_joystick2, 1);
    }

    private JoystickButton acquireCoralButton() {
        return new JoystickButton(m_joystick2, 8);
    }

    private JoystickButton shootCoralButton() {
        return new JoystickButton(m_joystick2, 7);
    }

    // Joystick #2 Buttons

    private JoystickButton prepCoralLevel1Button() {
        return new JoystickButton(m_joystick1, 1);
    }

    private JoystickButton prepCoralLevel2Button() {
        return new JoystickButton(m_joystick1, 2);
    }

    private JoystickButton prepCoralLevel3Button() {
        return new JoystickButton(m_joystick1, 3);
    }

    private JoystickButton prepCoralLevel4Button() {
        return new JoystickButton(m_joystick1, 4);
    }
    // vague buttons after this?
    private JoystickButton prepButton() {
        return new JoystickButton(m_joystick1, 6);
    }

    private JoystickButton lowerBenderButton() {
        return new JoystickButton(m_joystick1, 5);
    }

    private JoystickButton test1Button() {
        return new JoystickButton(m_joystick1, 7);
    }

    private JoystickButton test2Button() {
        return new JoystickButton(m_joystick1, 8);
    }

    public boolean isDriveOperationMode() {
        return (m_operationMode == ButtonBoardOperationMode.Drive);
    }

    private void setOperationMode() {
        if (driveCameraSwitch().getAsBoolean()) {
            m_operationMode = ButtonBoardOperationMode.Drive;
        } else {
            m_operationMode = ButtonBoardOperationMode.Camera;
        }
        System.out.println(m_operationMode);
    }

    public double getJoystickX() {
        return m_joystick2.getX();
    }

    public double getJoystickY() {
        return m_joystick2.getY();
    }

    public void configureButtonBindings() {

        raiseElevatorButton().onTrue(
                new InstantCommand(() -> {
                    System.out.println("raiseElevatorButton Pressed");
                    // do something
                }));
        
        lowerElevatorButton().onTrue(
                new InstantCommand(() -> {
                    System.out.println("lowerElevatorButton Pressed");
                    // do something
                }));

        climbButton().onTrue(
                new InstantCommand(() -> {
                    System.out.println("climbButton Pressed");
                    // do something
                }));

        descendButton().onTrue(
                new InstantCommand(() -> {
                    System.out.println("descendButton Pressed");
                    // do something
            }));
        
        prepCoralLevel1Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("prepCoralLevel1Button Pressed"); 
                    // do something
                }));

        prepCoralLevel2Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("prepCoralLevel2Button Pressed");
                    // do something
                }));
        
        prepCoralLevel3Button().onTrue(
            new InstantCommand(() -> {
                System.out.println("prepCoralLevel3Button Pressed");
                // do something
            }));

        prepCoralLevel4Button().onTrue(
            new InstantCommand(() -> {
                System.out.println("prepCoralLevel4Button Pressed");
                // do something
            }));

        acquireAlgaeButton().onTrue(
            new InstantCommand(() -> {
                System.out.println("acquireAlgaeButton Pressed");
                // do something
            }));

        shootAlgaeButton().onTrue(
            new InstantCommand(() -> {
                System.out.println("shootAlgaeButton Pressed");
                // do something
            }));

        acquireCoralButton().onTrue(
            new InstantCommand(() -> {
                System.out.println("acquireCoralButton Pressed");
                // do something
            }));

        shootCoralButton().onTrue(
            new InstantCommand(() -> {
                System.out.println("shootCoralButton Pressed");
                // do something
            }))
    }    

}