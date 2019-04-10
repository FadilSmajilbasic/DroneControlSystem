package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import communication.*;
import java.util.ArrayList;
import java.util.List;
import settings.SettingsListener;
import settings.SettingsManager;

/**
 *
 * @author Fadil Smajilbasic
 */
public class DroneController extends Listener implements Runnable, SettingsListener {

    private final CommandManager COMMAND_MANAGER = new CommandManager();
    private final SettingsManager SETTINGS_MANAGER = new SettingsManager();
    private final FrameHelper FRAME_HELPER = new FrameHelper();
    private final Controller CONTROLLER = new Controller();
    private List<Float> deltas = new ArrayList<>();
    private float controllerSensibility;
    private float controllerDeltaPoints;
    private float controllerDegreesSensibility;
    private float movementDelay;
    private float deltaAverageMultiplier;
    private long lastMessageTimestamp = System.currentTimeMillis();

    public DroneController() {

        CONTROLLER.addListener(this);

    }

    public static void main(String[] args) {
        DroneController controller = new DroneController();
        controller.run();
    }

    @Override
    public void run() {

        System.out.println("reading");
//      disabled for testing
//        COMMAND_MANAGER.sendCommand(Commands.ENABLE_COMMANDS);
//      sleep required for testing without the simulator
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
        }

        while (CONTROLLER.isConnected()) {
            Frame frame = CONTROLLER.frame();
            FRAME_HELPER.setFrame(frame);
            if (FRAME_HELPER.getCurrentFrame().id() != FRAME_HELPER.getLastFrame().id()) {
                checkHeightControl();
                checkMovementControl();
            }
        }

        System.out.println("controller not connected");
    }

    @Override
    public void onConnect(Controller controller) {
        System.out.println("Connected leap");
        loadVariables();
    }

    private void loadVariables() {
        final float CONTROLLER_SENSIBILITY_DEFAULT_VALUE = 2;
        final float CONTROLLER_HEIGHT_DELTA_POINTS = 20;
        final float CONTROLLER_DEGREES_SENSIBILITY_DEFAULT_VALUE = 5;
        final float MOVEMENT_DELAY_DEFAULT_VALUE = 500;
        final float DELTA_AVERAGE_MULTIPLIER = 1.5f;

        this.controllerSensibility = getFloatValueFromSetting("sensibility", CONTROLLER_SENSIBILITY_DEFAULT_VALUE);
        this.controllerDeltaPoints = getFloatValueFromSetting("height_points_number", CONTROLLER_HEIGHT_DELTA_POINTS);
        this.controllerDegreesSensibility = getFloatValueFromSetting("degrees_sensibility", CONTROLLER_DEGREES_SENSIBILITY_DEFAULT_VALUE);
        this.movementDelay = getFloatValueFromSetting("movementDelay", MOVEMENT_DELAY_DEFAULT_VALUE);
        this.deltaAverageMultiplier = getFloatValueFromSetting("deltaAverageMultiplier", DELTA_AVERAGE_MULTIPLIER);

        System.out.println("===========================");
        System.out.println("controllerSensibility: " + controllerSensibility);
        System.out.println("controllerDeltaPoints: " + controllerDeltaPoints);
        System.out.println("degrees_sensibility: " + controllerDegreesSensibility);
        System.out.println("movementDelay: " + movementDelay);
        System.out.println("deltaAverageMultiplier: " + deltaAverageMultiplier);
        System.out.println("===========================");

    }

    /**
     * returns the average of the delta values of the y axis. This method is
     * used to determine whether the last Y value of the left hand is normal and
     * can be used as a command or is it abnormal and to be refused
     *
     * @return the average delta of the last n values, where 
     */
    private float getAverageDeltas() {
        float tot = 0;
        tot = deltas.stream().map((delta) -> Math.abs(delta)).reduce(tot, (accumulator, _item) -> accumulator + _item);
        return tot / (float) deltas.size();
    }

    
    private void shiftDeltas() {
        for (int i = deltas.size() - 1; i > 0; i--) {
            deltas.set(i, deltas.get(i - 1));
        }
    }

    private void addDeltasValue(float value) {
        if (deltas.size() < 20) {
            deltas.add(value);
        } else {
            shiftDeltas();
            deltas.set(0, value);
        }
    }

    private void checkHeightControl() {

        float lastY = FRAME_HELPER.getDeltaY();
        float handSpeed = Math.abs(FRAME_HELPER.getHandSpeedY(FRAME_HELPER.getLeftHand(null)) / 36);

        if ((handSpeed > this.controllerSensibility) && lastY != 0.0) {

            float average = getAverageDeltas();
            int yPos = (int) lastY;

            if (Math.abs(yPos) < average * deltaAverageMultiplier) {
                if (yPos != 0) {
                    String message = yPos > 0 ? Commands.up(yPos) : Commands.down(Math.abs(yPos));

//                COMMAND_MANAGER.sendCommand(message);
                    System.out.println(message);

                }
            }

        }

        addDeltasValue(lastY);

    }

    private void checkMovementControl() {
        String[] commands = new String[3];

        if ((System.currentTimeMillis() - lastMessageTimestamp) > movementDelay) {
            float pitchValue = FRAME_HELPER.getPitch(FRAME_HELPER.getRightHand(null)) / 10;
            float rollValue = FRAME_HELPER.getRoll(FRAME_HELPER.getRightHand(null)) / 10;
            float yawValue = FRAME_HELPER.getYaw(FRAME_HELPER.getLeftHand(null)) / 10;

            if (Math.abs(rollValue) > controllerDegreesSensibility) {

                if (((int) rollValue - controllerDegreesSensibility) != 0) {
                    String message = rollValue < 0
                            ? Commands.right((int) Math.abs(rollValue - controllerDegreesSensibility))
                            : Commands.left((int) (rollValue - controllerDegreesSensibility));
                    commands[0] = message;
                    System.out.println(message);
                }

            }

            if (Math.abs(pitchValue) > controllerDegreesSensibility) {
                if (((int) pitchValue - controllerDegreesSensibility) != 0) {
                    String message = pitchValue > 0
                            ? Commands.back((int) (pitchValue - controllerDegreesSensibility))
                            : Commands.forward((int) Math.abs(pitchValue - controllerDegreesSensibility));
                    commands[1] = message;
                    System.out.println(message);

                }
            }

            if (Math.abs(yawValue) > controllerDegreesSensibility) {
                if (((int) yawValue - controllerDegreesSensibility) != 0) {
                    String message = yawValue > 0
                            ? Commands.rotateCounterClockwise((int) (yawValue - controllerDegreesSensibility))
                            : Commands.rotateClockwise((int) Math.abs(yawValue - controllerDegreesSensibility));
                    commands[1] = message;
                    System.out.println(message);

                }
            }
            lastMessageTimestamp = System.currentTimeMillis();
        }

//        COMMAND_MANAGER.sendCommands(commands);
    }

    /**
     * This method uses the SETTINGS_MANAGER in order to read the settings values
 from the config file
     *
     * @param settingName The name of the setting to search
     * @param defaultValue The default value of that setting
     * @return the value read from the file
     */
    private float getFloatValueFromSetting(String settingName, float defaultValue) {
        try {
            return Float.parseFloat(SETTINGS_MANAGER.getSetting(settingName));
        } catch (NumberFormatException ex) {
            System.err.println("[Parse error] Can't parse '" + settingName + "' value from settings, set the default one.");
            return defaultValue;
        } catch (IllegalArgumentException ex) {
            System.err.println("[Settings name error] Can't get setting value with name: '" + settingName + "'");
            return defaultValue;
        }
    }

    /**
     * Method called when the user updates the settings from the GUI
     */
    @Override
    public void settingsChanged() {
        loadVariables();
    }
}
