package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import communication.*;
import java.util.ArrayList;
import java.util.List;
import settings.SettingsManager;

/**
 *
 * @author Fadil Smajilbasic
 */
public class DroneController extends Listener implements Runnable {

    private static CommandManager commandManager = new CommandManager();;
    public static SettingsManager settingsManager = new SettingsManager();
    private FrameHelper helper = new FrameHelper();
    private Controller controller = new Controller();
    private List<Float> deltas = new ArrayList<Float>();
    private float controllerSensibility;
    private float controllerDeltaPoints;
    private float controllerDegreesSensibility;
    
    public DroneController() {
        final float CONTROLLER_SENSIBILITY_DEFAULT_VALUE = 2;
        final float CONTROLLER_HEIGHT_DELTA_POINTS = 20;
        final float CONTROLLER_DEGREES_SENSIBILITY_DEFAULT_VALUE = 5;
        
        controller.addListener(this);
        
        this.controllerSensibility = getFloatValueFromSetting("sensibility",CONTROLLER_SENSIBILITY_DEFAULT_VALUE);
        this.controllerDeltaPoints = getFloatValueFromSetting("height_points_number",CONTROLLER_HEIGHT_DELTA_POINTS);
        this.controllerDegreesSensibility = getFloatValueFromSetting("degrees_sensibility", CONTROLLER_DEGREES_SENSIBILITY_DEFAULT_VALUE);
    }

    public static void main(String[] args) {
        System.out.println("Started Controller :)");
        DroneController controller = new DroneController();
        controller.run();
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected leap");
    }

    public void onFrame(Controller controller) {

    }

    public float getAverageDeltas() {
        float tot = 0;
        for (float delta : deltas) {
            tot += Math.abs(delta);
        }
        return tot / (float) deltas.size();
    }

    public float translateAltitude(float altitude, float step) {
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = ((altitude / 10) - 30) / step;
        return translated;
    }

    public void shiftDeltas() {
        for (int i = deltas.size() - 1; i > 0; i--) {
            deltas.set(i, deltas.get(i - 1));
        }
    }

    public void checkHeightControl() {

        float lastY = helper.getDeltaY();

        float handSpeed = Math.abs(helper.getHandSpeedY(helper.getLeftHand()) / 10);

        if ((handSpeed > this.controllerSensibility) && lastY != 0.0) {
            if (deltas.size() < this.controllerDeltaPoints) {
                deltas.add(lastY);
            } else {
                shiftDeltas();
                deltas.set(0, lastY);
            }
            float average = getAverageDeltas();

            //System.out.println("average: " + average);
            int yPos = (int) lastY;

            //Costruisce la stringa
            if (yPos != 0 && (yPos < average || yPos > -average)) {
                String message = yPos > 0 ? Commands.up(yPos) : Commands.down(Math.abs(yPos));

                //Invia la stringa
                commandManager.sendCommand(message);
                System.out.println("average: " + average);
                System.out.println("Sending message: " + message);

            }
        }

    }

    public void checkMovementControl() {
        float pitchValue = -helper.getPitch(helper.getRightHand());
        float rollValueReal = -helper.getRoll(helper.getRightHand());
        float yawValue = -helper.getYaw(helper.getRightHand());
        float handSpeed = Math.abs(helper.getHandSpeedY(helper.getRightHand()) / 10);
        float rollValue = helper.getHandAngle(helper.getRightHand());

//        if (rollValue > 0) {
//            rollValue = 180 - rollValue;
//        } else {
//            rollValue = -(180 + rollValue);
//        }
        
//        System.out.println("real roll: " + rollValueReal);
        System.out.println("calc roll: " + rollValue);

//        System.out.println("hand speed: " + handSpeed);
        if (handSpeed > controllerSensibility) {
//            System.out.println("movement detected");
//            if (pitchValue != 180 && pitchValue != 0.0) {
//                System.out.println("pitch: " + pitchValue);
//
//            }
//
//            if (yawValue != 180 && yawValue != 0.0) {
//                System.out.println("yaw: " + yawValue);
//            }

            if (rollValue != 180 && rollValue != 0.0) {
                String message = rollValue > 90 ? Commands.right((int) rollValue - 90) : Commands.left(Math.abs((int) rollValue));
                System.out.println("message: " + message);
                System.out.println("roll: " + rollValue);
//                commandManager.sendCommand(message);
            }
        }
    }

    @Override
    public void run() {

        System.out.println("reading");
//        disabled for testing
//        commandManager.sendCommand(Commands.ENABLE_COMMANDS);
        System.out.println("sending command: " + Commands.ENABLE_COMMANDS);
        while (controller.isConnected()) {
            Frame frame = controller.frame();
            helper.setFrame(frame);
            if (helper.getFrame().id() != helper.getLastFrame().id()) {
                checkHeightControl();
                checkMovementControl();
            }
        }

        System.out.println("controller not connected");
    }
    
    
    private static float getFloatValueFromSetting(String settingName, float defaultValue){
        try{
            return Float.parseFloat(settingsManager.getSetting(settingName));
        }
        catch(NumberFormatException ex){
            System.err.println("[Parse error] Can't parse '"+ settingName +"' value from settings, set the default one.");
            return defaultValue;
        }
        catch(IllegalArgumentException ex){
            System.err.println("[Settings name error] Can't get setting value with name: '"+ settingName +"'");
            return defaultValue;
        }
    }
}
