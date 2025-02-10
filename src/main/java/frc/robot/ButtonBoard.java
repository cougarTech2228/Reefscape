package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

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
        return new JoystickButton(m_joystick2, 11);
    }

    private JoystickButton raiseElevatorButton() {
        return new JoystickButton(m_joystick2, 10);
    }

    private JoystickButton climbButton() {
        return new JoystickButton(m_joystick2, 9);
    }

    private JoystickButton descendButton() {
        return new JoystickButton(m_joystick2, 12);
    }

    private JoystickButton acquireAlgaeButton() {
        return new JoystickButton(m_joystick2, 8);
    }

    private JoystickButton shootAlgaeButton() {
        return new JoystickButton(m_joystick2, 7);
    }

    private JoystickButton prepAlgaeNetButton() {
        return new JoystickButton(m_joystick2, 3);
    }

    private JoystickButton test6Button() {
        return new JoystickButton(m_joystick2, 6);
    }    

    private JoystickButton test5Button() {
        return new JoystickButton(m_joystick2, 5);
    }    

    private JoystickButton test4Button() {
        return new JoystickButton(m_joystick2, 4);
    }    

    


    // Joystick #2 Buttons

    private JoystickButton acquireCoralButton() {
        return new JoystickButton(m_joystick1, 1);
    }

    private JoystickButton shootCoralButton() {
        return new JoystickButton(m_joystick1, 5);
    }

    private JoystickButton prepCoralLevel1Button() {
        return new JoystickButton(m_joystick1, 8);
    }

    private JoystickButton prepCoralLevel2Button() {
        return new JoystickButton(m_joystick1, 7);
    }   

    private JoystickButton prepCoralLevel3Button() {
        return new JoystickButton(m_joystick1, 6);
    }

    private JoystickButton prepCoralLevel4Button() {
        return new JoystickButton(m_joystick1, 4);
    }    

    private JoystickButton test2Button() {
        return new JoystickButton(m_joystick1, 2);
    }    

    private JoystickButton test3Button() {
        return new JoystickButton(m_joystick1, 3);
    }    

    private JoystickButton test9Button() {
        return new JoystickButton(m_joystick1, 4);
    }   
    
    private JoystickButton test10Button() {
        return new JoystickButton(m_joystick1, 4);
    }        

    private JoystickButton test11Button() {
        return new JoystickButton(m_joystick1, 4);
    }    

    private JoystickButton test12Button() {
        return new JoystickButton(m_joystick1, 4);
    }    


    public boolean isDriveOperationMode() {
        return (m_operationMode == ButtonBoardOperationMode.Drive);
    }

    // private void setOperationMode() {
    //     if (driveCameraSwitch().getAsBoolean()) {
    //         m_operationMode = ButtonBoardOperationMode.Drive;
    //     } else {
    //         m_operationMode = ButtonBoardOperationMode.Camera;
    //     }
    //     System.out.println(m_operationMode);
    // }

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
            }));

        prepAlgaeNetButton().onTrue(
            new InstantCommand(() -> {
                System.out.println("prepAlgaeNetButton Pressed");
                // do something
            }));

        test2Button().onTrue(
            new InstantCommand(() -> {
                System.out.println("test2Button Pressed");
                // do something
            }));
        
        test3Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("test3Button Pressed");
                    // do something
                }));

        test4Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("test4Button Pressed");
                    // do something
                }));

        test5Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("test5Button Pressed");
                    // do something
                }));

        test6Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("test6Button Pressed");
                    // do something
                }));

        test9Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("test9Button Pressed");
                    // do something
                }));

        test10Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("test10Button Pressed");
                    // do something
                }));

        test11Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("test11Button Pressed");
                    // do something
                }));

        test12Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("test12Button Pressed");
                    // do something
                }));
    }    

}