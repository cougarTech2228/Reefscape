package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;

public class ButtonBoard {

    private final Joystick m_joystick1;
    private final Joystick m_joystick2;
    private final Elevator elevator;
    private final CoralCone coralCone;
    private final AlgaeAcquirer algaeAcquirer;

    public ButtonBoard(Joystick channel1, Joystick channel2, Elevator elevator, CoralCone coralCone,
            AlgaeAcquirer algaeAcquirer) {
        m_joystick1 = channel1;
        m_joystick2 = channel2;
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
    }

    // Joystick #1 Buttons
    private JoystickButton lowerElevatorButton() {
        return new JoystickButton(m_joystick1, 11);
    }

    private JoystickButton raiseElevatorButton() {
        return new JoystickButton(m_joystick1, 10);
    }

    private JoystickButton climbButton() {
        return new JoystickButton(m_joystick1, 9);
    }

    private JoystickButton descendButton() {
        return new JoystickButton(m_joystick1, 12);
    }

    private JoystickButton acquireAlgaeButton() {
        return new JoystickButton(m_joystick1, 8);
    }

    private JoystickButton shootAlgaeButton() {
        return new JoystickButton(m_joystick1, 7);
    }

    private JoystickButton prepAlgaeNetButton() {
        return new JoystickButton(m_joystick1, 3);
    }

    private JoystickButton manualAlgaeUpButton() {
        return new JoystickButton(m_joystick1, 6);
    }

    private JoystickButton test5Button() {
        return new JoystickButton(m_joystick1, 5);
    }

    private JoystickButton manualAlgaeDownButton() {
        return new JoystickButton(m_joystick1, 4);
    }

    // Joystick #2 Buttons

    private JoystickButton acquireCoralButton() {
        return new JoystickButton(m_joystick2, 1);
    }

    private JoystickButton shootCoralButton() {
        return new JoystickButton(m_joystick2, 5);
    }

    private JoystickButton prepCoralLevel1Button() {
        return new JoystickButton(m_joystick2, 8);
    }

    private JoystickButton prepCoralLevel2Button() {
        return new JoystickButton(m_joystick2, 7);
    }

    private JoystickButton prepCoralLevel3Button() {
        return new JoystickButton(m_joystick2, 6);
    }

    private JoystickButton prepCoralLevel4Button() {
        return new JoystickButton(m_joystick2, 4);
    }

    private JoystickButton manualUpCoralButton() {
        return new JoystickButton(m_joystick2, 2);
    }

    private JoystickButton manualDownCoralButton() {
        return new JoystickButton(m_joystick2, 3);
    }

    private JoystickButton manualUpClimberButton() {
        return new JoystickButton(m_joystick2, 10);
    }

    private JoystickButton manualDownClimberButton() {
        return new JoystickButton(m_joystick2, 11);
    }

    private JoystickButton test11Button() {
        return new JoystickButton(m_joystick2, 4);
    }

    private JoystickButton test12Button() {
        return new JoystickButton(m_joystick2, 4);
    }

    public double getJoystickX() {
        return m_joystick2.getX();
    }

    public double getJoystickY() {
        return m_joystick2.getY();
    }

    public void configureButtonBindings() {

        raiseElevatorButton()
                .onTrue(
                        new InstantCommand(() -> {
                            System.out.println("lowerElevatorButton Pressed");
                            elevator.manualUp();

                        }))
                .onFalse(
                        new InstantCommand(() -> {
                            System.out.println("lowerElevatorButton Unpressed");
                            elevator.stop();

                        }));

        lowerElevatorButton()
                .onTrue(
                        new InstantCommand(() -> {
                            System.out.println("lowerElevatorButton Pressed");
                            elevator.manualDown();

                        }))
                .onFalse(new InstantCommand(() -> {
                    System.out.println("lowerElevatorButton Unpressed");
                    elevator.stop();

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

        manualUpCoralButton().onTrue(
                new InstantCommand(() -> {
                    System.out.println("manualUpCoralButton Pressed");
                    coralCone.manualUp();
                }));

        manualUpCoralButton().onFalse(
                new InstantCommand(() -> {
                    System.out.println("manualUpCoralButton Unpressed");
                    coralCone.stop();
                }));

        manualDownCoralButton().onTrue(
                new InstantCommand(() -> {
                    System.out.println("manualDownCoralButton Pressed");
                    coralCone.manualDown();
                }));

        manualDownCoralButton().onFalse(
                new InstantCommand(() -> {
                    System.out.println("manualDownCoralButton Unpressed");
                    coralCone.stop();
                }));

        manualAlgaeDownButton().onTrue(
                new InstantCommand(() -> {
                    System.out.println("manualAlgaeDownButton Pressed");
                    algaeAcquirer.manualDown();
                }));

        manualAlgaeDownButton().onFalse(
                new InstantCommand(() -> {
                    System.out.println("manualAlgaeDownButton Unpressed");
                    algaeAcquirer.stop();

                }));

        manualAlgaeUpButton()
                .onTrue(
                        new InstantCommand(() -> {
                            System.out.println("manualAlgaeUpButton Pressed");
                            algaeAcquirer.manualUp();
                        }));

        manualAlgaeUpButton()
                .onFalse(
                        new InstantCommand(() -> {
                            System.out.println("manualAlgaeUpButton Unpressed");
                            algaeAcquirer.stop();
                        }));

        test5Button().onTrue(
                new InstantCommand(() -> {
                    System.out.println("test5Button Pressed");
                    // do something
                }));

        manualUpClimberButton()
                .onTrue(
                        new InstantCommand(() -> {
                            System.out.println("manualUpClimberButton Pressed");
                            climber.manualUp();
                        }))
                .onFalse(
                        new InstantCommand(() -> {
                            System.out.println("manualUpClimberButton Pressed");
                            climber.stop();
                        }));

        manualDownClimberButton()
                .onTrue(
                        new InstantCommand(() -> {
                            System.out.println("manualClimberDownButton Pressed");
                            climber.manualUp();
                        }))
                .onFalse(
                        new InstantCommand(() -> {
                            System.out.println("manualClimberDownButton Unpressed");
                            climber.stop();
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