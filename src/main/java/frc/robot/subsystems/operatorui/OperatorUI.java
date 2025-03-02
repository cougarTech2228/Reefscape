package frc.robot.subsystems.operatorui;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.BargeCommand;
import frc.robot.commands.CollapseCommand;
import frc.robot.commands.FireAlgaeCommand;
import frc.robot.commands.FireCoralCommand;
import frc.robot.commands.LoadAlgaeAutoCommand;
import frc.robot.commands.LoadAlgaeCommand;
import frc.robot.commands.LoadCoralAutoCommand;
import frc.robot.commands.LoadCoralCommand;
import frc.robot.commands.PlaceCoralCommand;
import frc.robot.commands.PrepProcessorCommand;
import frc.robot.commands.ScoreProcessorCommand;
import frc.robot.commands.pathplanner.PrepPlaceCoralCommand;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.climber.Climber;
import frc.robot.subsystems.climber.ClimberConstants;
import frc.robot.subsystems.climber.Climber.ClimberPosition;
import frc.robot.subsystems.climber.Climber.ServoLockPosition;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.elevator.Elevator;
import frc.robot.util.CTSequentialCommandGroup;
import frc.robot.util.Enums.*;

import java.util.EnumSet;

public class OperatorUI extends SubsystemBase {
    private NetworkTable table;

    private final Elevator elevator;
    private final AlgaeAcquirer algaeAcquirer;
    private final CoralCone coralCone;
    private final Drive drive;
    private final Climber climber;

    private Command activeCommand = null;

    // outgoing status topics
    private static final String coralLoadedTopic = "coralLoaded";
    private static final String algaeLoadedTopic = "algaeLoaded";
    private static final String climberAngleTopic = "climberAngle";
    private static final String climberStateTopic = "climberState";
    private static final String climberAngleMinTopic = "climberAngleMin";
    private static final String climberAngleMaxTopic = "climberAngleMax";
    private static final String climberLockedTopic = "climberLocked";

    // incoming control topics
    private static final String executeCommandTopic = "executeCommand";
    private static final String modeTopic = "mode";
    private static final String destinationTopic = "dest";
    private static final String reefSegmentTopic = "reefSegment";
    private static final String reefPostTopic = "reefPost";
    private static final String autoAlignTopic = "autoAlign";

    private static final String MODE_ALGAE = "algae";
    private static final String MODE_CORAL = "coral";
    private static final String MODE_CORAL_AND_ALGAE = "coralAndAlgae";

    private static final String DESTINATION_REEF = "reef";
    private static final String DESTINATION_CORAL_STATION = "coralStation";
    private static final String DESTINATION_PROCESSOR = "processor";
    private static final String DESTINATION_BARGE = "barge";
    private static final String DESTINATION_FLOOR_ALGAE = "floorAlgae";
    private static final String DESTINATION_ALGAE_ON_CORAL = "algaeOnCoral";

    private String getMode() {
        return table.getEntry(modeTopic).getString("");
    }
    private String getDestination() {
        return table.getEntry(destinationTopic).getString("");
    }
    private int getReefSegment() {
        return (int)table.getEntry(reefSegmentTopic).getInteger(0);
    }
    private String getReefPost() {
        return table.getEntry(reefPostTopic).getString("");
    }
    private boolean getAutoAlign() {
        return table.getEntry(autoAlignTopic).getBoolean(false);
    }

    public OperatorUI(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralCone, Drive drive, Climber climber) {
        this.elevator = elevator;
        this.algaeAcquirer = algaeAcquirer;
        this.coralCone = coralCone;
        this.drive = drive;
        this.climber = climber;

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        table = inst.getTable("ReefControl");
        table.getEntry(".type").setString("ReefControl");
        table.getEntry(coralLoadedTopic).setBoolean(false);
        table.getEntry(algaeLoadedTopic).setBoolean(false);
        table.getEntry(executeCommandTopic).setBoolean(false);

        table.getEntry(destinationTopic).setString("");
        table.getEntry(modeTopic).setString("");
        table.getEntry(reefSegmentTopic).setInteger(0);
        table.getEntry(reefPostTopic).setString("");
        table.getEntry(climberStateTopic).setString("");
        table.getEntry(climberAngleMinTopic).setDouble(ClimberConstants.angleRetracted);
        table.getEntry(climberAngleMaxTopic).setDouble(ClimberConstants.angleExtended);
        table.getEntry(climberLockedTopic).setBoolean(false);

        table.addListener(
            executeCommandTopic,
            EnumSet.of(NetworkTableEvent.Kind.kValueRemote),
            (NetworkTable table, String key, NetworkTableEvent event) -> {
                System.out.println(
                    "Network Table change: " + key + " value: " + event.valueData.value.getBoolean());
                if (event.valueData.value.getBoolean()) {
                    processRequest();
                } else {
                    if (activeCommand != null) {
                    activeCommand.cancel();
                    table.getEntry(executeCommandTopic).setBoolean(false);
                    activeCommand = null;
                    }
                }
            }
        );
    }

    public void periodic() {
        if (activeCommand != null) {
            if (activeCommand.isFinished()) {
                System.out.println("Command finished " + activeCommand);
                table.getEntry(executeCommandTopic).setBoolean(false);
                activeCommand = null;
            }
        }

        table.getEntry(algaeLoadedTopic).setBoolean(algaeAcquirer.isLoaded());
        table.getEntry(coralLoadedTopic).setBoolean(coralCone.isLoaded());
        table.getEntry(climberAngleTopic).setDouble(climber.getClimberAngle());
        table.getEntry(climberStateTopic).setString(climber.getClimberState());
        table.getEntry(climberLockedTopic).setBoolean(climber.isLocked());
    }

    // The driver made their selections, and pressed "GO" figure out what we should be doing now
    private void processRequest() {
        // "fire" mode is a special case where we just abort any command in progress, and fire whatever mechenism is loaded
        if (getMode().equals("fire")){
            // if we have both an algae and a coral, fire the algae first
            if(algaeAcquirer.isLoaded()) {
                Command cmd = new CTSequentialCommandGroup(
                        new FireAlgaeCommand(algaeAcquirer),
                        new CollapseCommand(elevator, algaeAcquirer, coralCone)
                    );
                startCommand(cmd);
            } else if (coralCone.isLoaded()) {
                boolean isFast = false;
                if (getReefPost().equals("L1")) {
                    isFast = true;
                }
                Command cmd = new CTSequentialCommandGroup(
                    new FireCoralCommand(coralCone, isFast),
                    new CollapseCommand(elevator, algaeAcquirer, coralCone)
                );
                startCommand(cmd);
            }
            return;
        }
        if (getMode().equals("cage-extend")){
            if (climber.getClimberState().equals("EXTENDING")){
                climber.stop();
            } else {
                climber.setClimberPosition(ClimberPosition.UP);
            }
            table.getEntry(executeCommandTopic).setBoolean(false);
            return;
        }
        if (getMode().equals("cage-retract")){
            if (climber.getClimberState().equals("RETRACTING")){
                climber.stop();
            } else {
                climber.setClimberPosition(ClimberPosition.DOWN);
            }
            table.getEntry(executeCommandTopic).setBoolean(false);
            return;
        }
        if (getMode().equals("cage-lock")){
            climber.setServoPosition(ServoLockPosition.LOCKED);
            table.getEntry(executeCommandTopic).setBoolean(false);
            return;
        }
        if (getMode().equals("cage-unlock")){
            climber.setServoPosition(ServoLockPosition.UNLOCKED);
            table.getEntry(executeCommandTopic).setBoolean(false);
            return;
        }
        if (getMode().equals("collapse")){
            startCommand(new CollapseCommand(elevator, algaeAcquirer, coralCone));
            return;
        }

        switch (getDestination()) {
            case DESTINATION_CORAL_STATION:
                handleCoralStation();
            break;
            case DESTINATION_PROCESSOR:
                handleProcessor();
            break;
            case DESTINATION_BARGE:
                handleBarge();
            break;
            case DESTINATION_REEF:
                ReefSegment segment = ReefSegment.Segment_1;
                switch(getReefSegment()) {
                    case 1:
                        segment = ReefSegment.Segment_1;
                    break;
                    case 2:
                        segment = ReefSegment.Segment_2;
                        break;
                    case 3:
                        segment = ReefSegment.Segment_3;
                        break;
                    case 4:
                        segment = ReefSegment.Segment_4;
                        break;
                    case 5:
                        segment = ReefSegment.Segment_5;
                        break;
                    case 6:
                        segment = ReefSegment.Segment_6;
                        break;
                }
                handleReef(segment);
            break;
            case DESTINATION_FLOOR_ALGAE:
                handleFloorAlgae();
                break;
            case DESTINATION_ALGAE_ON_CORAL:
                handleAlgaeOnCoral();
                break;
        }
    }
    
    private void startCommand(Command command){
        activeCommand = command;
        CommandScheduler.getInstance().schedule(activeCommand);
    }

    private void handleCoralStation() {
        System.out.println("handleCoralStation()");
        boolean autoAlign = getAutoAlign();
        if (autoAlign) {
            startCommand( new LoadCoralAutoCommand(elevator, algaeAcquirer, coralCone, drive));
        } else {
            startCommand( new LoadCoralCommand(elevator, algaeAcquirer, coralCone));
        }
    }

    private void handleProcessor() {
        boolean autoAlign = getAutoAlign();
        System.out.println("handleProcessor()");
        if (autoAlign) {
            startCommand( new ScoreProcessorCommand(elevator, algaeAcquirer, coralCone, drive));
        } else {
            startCommand( new PrepProcessorCommand(elevator, algaeAcquirer, coralCone));
        }
    }

    private void handleBarge() {
        System.out.println("handleBarge()");
        startCommand( new BargeCommand(elevator, algaeAcquirer, coralCone));
    }

    private void handleFloorAlgae() {
        System.out.println("handleFloorAlgae()");
        startCommand(new LoadAlgaeCommand(true, AlgaeHeight.Floor, elevator, algaeAcquirer, coralCone));
    }
    private void handleAlgaeOnCoral() {
        System.out.println("handleAlgaeOnCoral()");
        startCommand(new LoadAlgaeCommand(true, AlgaeHeight.FloorOnCoral, elevator, algaeAcquirer, coralCone));
    }

    private void handleReef(ReefSegment segment) {
        boolean autoAlign = getAutoAlign();

        switch (getMode()){
            case MODE_ALGAE:
                // pickup an algae from the reef
                System.out.println("handleReef - Algae");
                if (segment == ReefSegment.Segment_1 || segment == ReefSegment.Segment_3 || segment == ReefSegment.Segment_5) {
                    if (autoAlign){
                        startCommand(new LoadAlgaeAutoCommand(segment, AlgaeHeight.REEF_HIGH, elevator, algaeAcquirer, coralCone, drive));
                    } else {
                        startCommand(new LoadAlgaeCommand(true, AlgaeHeight.REEF_HIGH, elevator, algaeAcquirer, coralCone));
                    }
                } else if (segment == ReefSegment.Segment_2 || segment == ReefSegment.Segment_4 || segment == ReefSegment.Segment_6) {
                    if (autoAlign) {
                        startCommand(new LoadAlgaeAutoCommand(segment, AlgaeHeight.REEF_LOW, elevator, algaeAcquirer, coralCone, drive));
                    } else {
                        startCommand(new LoadAlgaeCommand(true, AlgaeHeight.REEF_LOW, elevator, algaeAcquirer, coralCone));
                    }
                }
            break;
            case MODE_CORAL:
                // place a coral on the reef
                System.out.println("handleReef - Coral");
                String post = getReefPost();
                switch(post) {
                    case "L1":
                        if (autoAlign) {
                            startCommand(new PlaceCoralCommand(segment, ReefLocation.L1, elevator, algaeAcquirer, coralCone, drive));
                        } else {
                            startCommand(new PrepPlaceCoralCommand(segment, ReefLocation.L1, elevator, algaeAcquirer, coralCone));
                        }
                        break;
                    case "L2_L":
                        if (autoAlign) {
                            startCommand(new PlaceCoralCommand(segment, ReefLocation.L2_L, elevator, algaeAcquirer, coralCone, drive));
                        } else {
                            startCommand(new PrepPlaceCoralCommand(segment, ReefLocation.L2_L, elevator, algaeAcquirer, coralCone));
                        }
                        break;
                    case "L2_R":
                        if (autoAlign) {
                            startCommand(new PlaceCoralCommand(segment, ReefLocation.L2_R, elevator, algaeAcquirer, coralCone, drive));
                        } else {
                            startCommand(new PrepPlaceCoralCommand(segment, ReefLocation.L2_R, elevator, algaeAcquirer, coralCone));
                        }
                        break;
                    case "L3_L":
                        if (autoAlign) {
                            startCommand(new PlaceCoralCommand(segment, ReefLocation.L3_L, elevator, algaeAcquirer, coralCone, drive));
                        } else {
                            startCommand(new PrepPlaceCoralCommand(segment, ReefLocation.L3_L, elevator, algaeAcquirer, coralCone));
                        }
                        break;
                    case "L3_R":
                        if (autoAlign) {
                            startCommand(new PlaceCoralCommand(segment, ReefLocation.L3_R, elevator, algaeAcquirer, coralCone, drive));
                        } else {
                            startCommand(new PrepPlaceCoralCommand(segment, ReefLocation.L3_R, elevator, algaeAcquirer, coralCone));
                        }
                        break;
                    case "L4_L":
                        if (autoAlign) {
                            startCommand(new PlaceCoralCommand(segment, ReefLocation.L4_L, elevator, algaeAcquirer, coralCone, drive));
                        } else {
                            startCommand(new PrepPlaceCoralCommand(segment, ReefLocation.L4_L, elevator, algaeAcquirer, coralCone));
                        }
                        break;
                    case "L4_R":
                        if (autoAlign) {
                            startCommand(new PlaceCoralCommand(segment, ReefLocation.L4_R, elevator, algaeAcquirer, coralCone, drive));
                        } else {
                            startCommand(new PrepPlaceCoralCommand(segment, ReefLocation.L4_R, elevator, algaeAcquirer, coralCone));
                        }
                        break;
                    default:
                        System.out.println("Error - Invalid reef post: " + post);
                        break;
                }
            break;
            case MODE_CORAL_AND_ALGAE:
                // place a coral and pickup an algae from the reef
                System.out.println("handleReef - Coral and Algae");
                ReefLocation reefLocation;
                if (segment == ReefSegment.Segment_1 || segment == ReefSegment.Segment_3 || segment == ReefSegment.Segment_5) {
                    reefLocation = ReefLocation.L3_R;
                } else {
                    reefLocation = ReefLocation.L2_R;
                }

                if (autoAlign) {
                    startCommand(new PlaceCoralCommand(true, segment, reefLocation, elevator, algaeAcquirer, coralCone, drive));
                } else {
                    startCommand(new PrepPlaceCoralCommand(true, segment, reefLocation, elevator, algaeAcquirer, coralCone));
                }
            break;
        }
    }
}
