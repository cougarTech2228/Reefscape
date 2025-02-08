package frc.robot.subsystems.operatorui;

import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.ui.LoadCoralCommand;
import frc.robot.subsystems.elevator.Elevator;
import java.util.EnumSet;

public class OperatorUI extends SubsystemBase {
  private NetworkTable table;
  private BooleanSubscriber executeSubscriber;
  private boolean lastExecuteState = false;

  private final Elevator elevator;
  private final LoadCoralCommand loadCoralCommand;
  private Command activeCommand = null;

  public OperatorUI(Elevator elevator) {
    this.elevator = elevator;
    this.loadCoralCommand = new LoadCoralCommand(elevator);

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    table = inst.getTable("ReefControl");
    table.getEntry(".type").setString("ReefControl");
    table.getEntry("coralLoaded").setBoolean(false);
    table.getEntry("algaeLoaded").setBoolean(false);
    table.getEntry("executeCommand").setBoolean(false);

    table.getEntry("mode").setString("");
    table.getEntry("reefSegment").setInteger(0);
    table.getEntry("reefPost").setString("");

    table.addListener(
        "executeCommand",
        EnumSet.of(NetworkTableEvent.Kind.kValueRemote),
        (NetworkTable table, String key, NetworkTableEvent event) -> {
          System.out.println(
              "Network Table change: " + key + " value: " + event.valueData.value.getBoolean());
          if (event.valueData.value.getBoolean()) {
            activeCommand = loadCoralCommand;
            CommandScheduler.getInstance().schedule(activeCommand);
          }
        });
    executeSubscriber = table.getBooleanTopic("executeCommand").subscribe(false);
  }

  public void periodic() {
    if (activeCommand != null) {
      if (activeCommand.isFinished()) {
        System.out.println("Command finished");
        table.getEntry("executeCommand").setBoolean(false);
        activeCommand = null;
      } else {
        System.out.println("Command running");
      }
    }
  }
}
