// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import static frc.robot.subsystems.vision.VisionConstants.*;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.FireAlgaeCommand;
import frc.robot.commands.FireCoralCommand;
import frc.robot.commands.LoadCoralCommand;
import frc.robot.commands.pathplanner.LoadAlgaeAutoCommand;
import frc.robot.commands.pathplanner.PrepLoadAlgaeCommand;
import frc.robot.commands.pathplanner.PrepPlaceCoralCommand;
import frc.robot.commands.pathplanner.PrepProcessorCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerIO;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirerIONeo;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.coralCone.CoralConeIO;
import frc.robot.subsystems.coralCone.CoralConeIONeo;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.subsystems.elevator.ElevatorIO;
import frc.robot.subsystems.elevator.ElevatorIOTalonFX;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.climber.ClimberIO;
import frc.robot.subsystems.climber.ClimberIOTalonFX;
import frc.robot.subsystems.operatorui.OperatorUI;
import frc.robot.subsystems.vision.Vision;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIOPhotonVision;
import frc.robot.subsystems.vision.VisionIOPhotonVisionSim;
import frc.robot.util.Enums.*;
import frc.robot.commands.CollapseCommand;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
        // Subsystems
        private final Drive drive;
        private final Elevator elevator;
        private final CoralCone coralCone;
        private final AlgaeAcquirer algaeAcquirer;
        private final Climber climber;

        @SuppressWarnings("unused")
        private final Vision vision;

        @SuppressWarnings("unused")
        private final OperatorUI operatorUI;

        // Controller
        private final CommandXboxController driverController = new CommandXboxController(0);
        // private final Joystick buttonBox1 = new Joystick(1);
        // private final Joystick buttonBox2 = new Joystick(2);
        // private final ButtonBoard buttonBoard;

        // Dashboard inputs
        private final LoggedDashboardChooser<Command> autoChooser;

        private double driverOverridePercentage = 1;
        private double currentPercentage = 1;

        /**
         * The container for the robot. Contains subsystems, OI devices, and commands.
         */
        public RobotContainer() {
                switch (Constants.currentMode) {
                        case REAL:
                                // Real robot, instantiate hardware IO implementations
                                drive = new Drive(
                                                new GyroIOPigeon2(),
                                                new ModuleIOTalonFX(TunerConstants.FrontLeft),
                                                new ModuleIOTalonFX(TunerConstants.FrontRight),
                                                new ModuleIOTalonFX(TunerConstants.BackLeft),
                                                new ModuleIOTalonFX(TunerConstants.BackRight));
                                vision = new Vision(
                                                drive::addVisionMeasurement,
                                                new VisionIOPhotonVision(camera1Name, robotToCamera1),
                                                new VisionIOPhotonVision(camera2Name, robotToCamera2),
                                                new VisionIOPhotonVision(camera3Name, robotToCamera3),
                                                new VisionIOPhotonVision(camera4Name, robotToCamera4));
                                elevator = new Elevator(new ElevatorIOTalonFX());
                                algaeAcquirer = new AlgaeAcquirer(new AlgaeAcquirerIONeo());
                                coralCone = new CoralCone(new CoralConeIONeo());
                                climber = new Climber(new ClimberIOTalonFX());
                                break;

                        case SIM:
                                // Sim robot, instantiate physics sim IO implementations
                                drive = new Drive(
                                                new GyroIO() {
                                                },
                                                new ModuleIOSim(TunerConstants.FrontLeft),
                                                new ModuleIOSim(TunerConstants.FrontRight),
                                                new ModuleIOSim(TunerConstants.BackLeft),
                                                new ModuleIOSim(TunerConstants.BackRight));
                                vision = new Vision(
                                                drive::addVisionMeasurement,
                                                new VisionIOPhotonVisionSim(camera1Name, robotToCamera1,
                                                                drive::getPose));
                                elevator = new Elevator(new ElevatorIOTalonFX());
                                algaeAcquirer = new AlgaeAcquirer(new AlgaeAcquirerIONeo());
                                coralCone = new CoralCone(new CoralConeIONeo());
                                climber = new Climber(new ClimberIOTalonFX());
                                break;

                        default:
                                // Replayed robot, disable IO implementations
                                drive = new Drive(
                                                new GyroIO() {
                                                },
                                                new ModuleIO() {
                                                },
                                                new ModuleIO() {
                                                },
                                                new ModuleIO() {
                                                },
                                                new ModuleIO() {
                                                });
                                vision = new Vision(drive::addVisionMeasurement, new VisionIO() {
                                });
                                elevator = new Elevator(new ElevatorIO() {
                                });
                                algaeAcquirer = new AlgaeAcquirer(new AlgaeAcquirerIO() {
                                });
                                coralCone = new CoralCone(new CoralConeIO() {
                                });
                                climber = new Climber(new ClimberIO() {
                                });
                }

                Command fireCoralCommand = new FireCoralCommand(coralCone, false);
                Command fireAlgaeCommand = new FireAlgaeCommand(algaeAcquirer);
                Command loadCoralCommand = new LoadCoralCommand(elevator, algaeAcquirer, coralCone);
                Command loadAlgaeCommand = new LoadAlgaeAutoCommand(elevator, algaeAcquirer, coralCone);
                Command prepLowAlgaeCommand = new PrepLoadAlgaeCommand(AlgaeHeight.REEF_LOW, elevator, algaeAcquirer,
                                coralCone);
                Command prepHighAlgaeCommand = new PrepLoadAlgaeCommand(AlgaeHeight.REEF_HIGH, elevator, algaeAcquirer,
                                coralCone);

                Command prepProcessorCommand = new PrepProcessorCommand(elevator, algaeAcquirer, coralCone);
                Command collapseCommand = new CollapseCommand(elevator, algaeAcquirer, coralCone);
                Command prepL1CoralCommand = new PrepPlaceCoralCommand(ReefSegment.Segment_1, ReefLocation.L1, elevator,
                                algaeAcquirer, coralCone);
                Command prepL2CoralCommand = new PrepPlaceCoralCommand(ReefSegment.Segment_1, ReefLocation.L2_L,
                                elevator,
                                algaeAcquirer, coralCone);
                Command prepL3CoralCommand = new PrepPlaceCoralCommand(ReefSegment.Segment_1, ReefLocation.L3_L,
                                elevator,
                                algaeAcquirer, coralCone);
                Command prepL4CoralCommand = new PrepPlaceCoralCommand(ReefSegment.Segment_1, ReefLocation.L4_L,
                                elevator,
                                algaeAcquirer, coralCone);

                NamedCommands.registerCommand("fireCoral", fireCoralCommand);
                NamedCommands.registerCommand("fireAlgae", fireAlgaeCommand);
                NamedCommands.registerCommand("loadCoral", loadCoralCommand);
                NamedCommands.registerCommand("loadAlgae", loadAlgaeCommand);
                NamedCommands.registerCommand("prepLowAlgae", prepLowAlgaeCommand);
                NamedCommands.registerCommand("prepHighAlgae", prepHighAlgaeCommand);
                NamedCommands.registerCommand("prepProcessor", prepProcessorCommand);
                NamedCommands.registerCommand("collapse", collapseCommand);
                NamedCommands.registerCommand("prepL1Coral", prepL1CoralCommand);
                NamedCommands.registerCommand("prepL2Coral", prepL2CoralCommand);
                NamedCommands.registerCommand("prepL3Coral", prepL3CoralCommand);
                NamedCommands.registerCommand("prepL4Coral", prepL4CoralCommand);

                // Set up auto routines
                autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

                operatorUI = new OperatorUI(elevator, algaeAcquirer, coralCone, drive, climber);

                // // Set up SysId routines
                // autoChooser.addOption(
                // "Drive Wheel Radius Characterization",
                // DriveCommands.wheelRadiusCharacterization(drive));
                // autoChooser.addOption(
                // "Drive Simple FF Characterization",
                // DriveCommands.feedforwardCharacterization(drive));
                // autoChooser.addOption(
                // "Drive SysId (Quasistatic Forward)",
                // drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
                // autoChooser.addOption(
                // "Drive SysId (Quasistatic Reverse)",
                // drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
                // autoChooser.addOption(
                // "Drive SysId (Dynamic Forward)",
                // drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
                // autoChooser.addOption(
                // "Drive SysId (Dynamic Reverse)",
                // drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));

                // autoChooser.addOption(
                // "Elevator SysId (Quasistatic Forward)",
                // elevator.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
                // autoChooser.addOption(
                // "Elevator SysId (Quasistatic Reverse)",
                // elevator.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
                // autoChooser.addOption(
                // "Elevator SysId (Dynamic Forward)",
                // elevator.sysIdDynamic(SysIdRoutine.Direction.kForward));
                // autoChooser.addOption(
                // "Elevator SysId (Dynamic Reverse)",
                // elevator.sysIdDynamic(SysIdRoutine.Direction.kReverse));

                SmartDashboard.putData(
                                "Test Command",
                                new InstantCommand(
                                                () -> {
                                                        System.out.println("Test Command");
                                                }));
                // Configure the button bindings
                configureButtonBindings();
                // buttonBoard = new ButtonBoard(buttonBox1, buttonBox2, elevator, coralCone,
                // algaeAcquirer, climber);
                // buttonBoard.configureButtonBindings();

        }

        /**
         * Use this method to define your button->command mappings. Buttons can be
         * created by
         * instantiating a {@link GenericHID} or one of its subclasses ({@link
         * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
         * it to a {@link
         * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
         */
        private void configureButtonBindings() {
                // Default command, normal field-relative drive
                drive.setDefaultCommand(
                                DriveCommands.joystickDrive(
                                                drive,
                                                () -> -driverController.getLeftY(),
                                                () -> -driverController.getLeftX(),
                                                () -> -driverController.getRightX()));

                // Lock to 0° when A button is held
                driverController
                                .a()
                                .whileTrue(
                                                DriveCommands.joystickDriveAtAngle(
                                                                drive,
                                                                () -> -driverController.getLeftY(),
                                                                () -> -driverController.getLeftX(),
                                                                () -> new Rotation2d()));

                // Switch to X pattern when X button is pressed
                driverController.x().onTrue(Commands.runOnce(drive::stopWithX, drive));

                // Reset gyro to 0° when B button is pressed
                driverController
                                .b()
                                .onTrue(
                                                Commands.runOnce(
                                                                () -> drive.setPose(
                                                                                new Pose2d(drive.getPose()
                                                                                                .getTranslation(),
                                                                                                new Rotation2d())),
                                                                drive)
                                                                .ignoringDisable(true));

                driverController
                                .leftBumper()
                                .onTrue(
                                                new InstantCommand(() -> {
                                                        elevator.manualUp();
                                                }));

                driverController
                                .leftTrigger()
                                .onTrue(
                                                new InstantCommand(() -> {
                                                        elevator.manualDown();
                                                }));

                driverController.rightBumper()
                                .onTrue(new InstantCommand(() -> {
                                        driverOverridePercentage = 0.3;
                                }))
                                .onFalse(new InstantCommand(() -> {
                                        driverOverridePercentage = 1.0;
                                }));
        }

        /**
         * Use this to pass the autonomous command to the main {@link Robot} class.
         *
         * @return the command to run in autonomous
         */
        public Command getAutonomousCommand() {
                // return new AutoAlignCommand(drive, Destination.REEF_2_LEFT);
                return autoChooser.get();
        }

        public void calculateMaxAcceleration() {
                double percentage = elevator.getCurrentHeightPercentage();
                // we need a minimum level of acceleration
                double minimumPercentage = 0.1;
                if (percentage < minimumPercentage) {
                        percentage = minimumPercentage;
                }

                double value = Math.min(percentage, driverOverridePercentage);
                if (Math.abs(currentPercentage - value) > 0.05) {
                        currentPercentage = value;
                        drive.setAccelerationPercentage(value);
                }
        }
}
