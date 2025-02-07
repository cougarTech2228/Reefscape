package frc.robot.subsystems.operatorui;

import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.LoadCoralCommand;
import frc.robot.subsystems.algaeAcquirer.AlgaeAcquirer;
import frc.robot.subsystems.coralCone.CoralCone;
import frc.robot.subsystems.elevator.Elevator;
import java.util.EnumSet;

public class OperatorUI extends SubsystemBase {
    private NetworkTable table;
    private BooleanSubscriber executeSubscriber;
    private boolean lastExecuteState = false;

    private final Elevator elevator;
    private final LoadCoralCommand loadCoralCommand;
    private Command activeCommand = null;

    // outgoing status topics
    private static final String coralLoadedTopic = "coralLoaded";
    private static final String algaeLoadedTopic = "algaeLoaded";

    // incoming control topics
    private static final String executeCommandTopic = "executeCommand";
    private static final String modeTopic = "mode";
    private static final String destinationTopic = "dest";
    private static final String reefSegmentTopic = "reefSegment";
    private static final String reefPostTopic = "reefPost";

    private static final String MODE_ALGAE = "algae";
    private static final String MODE_CORAL = "coral";
    private static final String MODE_CORAL_AND_ALGAE = "coral_and_algae";

    private static final String DESTINATION_REEF = "reef";
    private static final String DESTINATION_CORAL_STATION = "coralStation";
    private static final String DESTINATION_PROCESSOR = "processor";
    private static final String DESTINATION_BARGE = "barge";
    private static final String DESTINATION_FLOOR_ALGAE = "floorAlgae";

    private String getMode() {
        return table.getEntry(modeTopic).getString("");
    }
    private String getDestination() {
        return table.getEntry(destinationTopic).getString("");
    }
    private long getReefSegment() {
        return table.getEntry(reefSegmentTopic).getInteger(0);
    }
    private String getReefPost() {
        return table.getEntry(reefPostTopic).getString("");
    }

    public OperatorUI(Elevator elevator, AlgaeAcquirer algaeAcquirer, CoralCone coralClaw) {
        this.elevator = elevator;
        this.loadCoralCommand = new LoadCoralCommand(elevator, algaeAcquirer, coralClaw);

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
        executeSubscriber = table.getBooleanTopic(executeCommandTopic).subscribe(false);
    }

    public void periodic() {
        if (activeCommand != null) {
            if (activeCommand.isFinished()) {
                System.out.println("Command finished");
                table.getEntry(executeCommandTopic).setBoolean(false);
                activeCommand = null;
            } else {
                System.out.println("Command running");
            }
        }
    }

    // The driver made their selections, and pressed "GO" figure out what we should be doing now
    private void processRequest() {
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
                handleReef();
            break;
            case DESTINATION_FLOOR_ALGAE:
                handleFloorAlgae();
        }
    }
    
    private void startCommand(Command command){
        activeCommand = command;
        CommandScheduler.getInstance().schedule(activeCommand);
    }

    private void handleCoralStation() {
        System.out.println("handleCoralStation()");
        startCommand(loadCoralCommand);
    }

    private void handleProcessor() {
        System.out.println("handleProcessor()");
    }

    private void handleBarge() {
        System.out.println("handleBarge()");
    }

    private void handleFloorAlgae() {
        System.out.println("handleFloorAlgae()");
    }

    private void handleReef() {
        switch (getMode()){
            case MODE_ALGAE:
                // pickup an algae from the reef
                System.out.println("handleReef - Algae");
            break;
            case MODE_CORAL:
                // place a coral on the reef
                System.out.println("handleReef - Coral");
            break;
            case MODE_CORAL_AND_ALGAE:
                // place a coral and pickup an algae from the reef
                System.out.println("handleReef - Coral and Algae");
            break;
        }
    }
}
