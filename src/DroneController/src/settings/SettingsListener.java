package settings;

/**
 * Listener used by the gui to notify the settingsManager in order to update the config file.
 * @author Luca Di Bello
 */
public interface SettingsListener {
    /**
     * Method that notifies the listener that the settings have been modified.
     */
    void settingsChanged();
}
