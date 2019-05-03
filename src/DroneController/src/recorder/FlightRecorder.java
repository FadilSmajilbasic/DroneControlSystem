package recorder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.StandardOpenOption;

/**
 * This class is used for record a full flight, from the take off to the landing.
 * 
 * @author Luca Di Bello
 */
public class FlightRecorder {
    
    
    private void createBase() throws IOException{
        final Path recordDir = Paths.get("records");
        
        if(!Files.exists(recordDir) || !Files.isDirectory(recordDir)){
            Files.createDirectory(recordDir);
        } 
    }
    
    /**
     * This method generates a record files.
     * @return Path of the generated file.
     * @throws IOException Thrown when the file cannot be created.
     */
    private Path generateRecordFile() throws IOException{
        final String fileHead = "dcs-flight-";
        final String fileTail = ".dcsfp"; // Drone Control System Flight Pattern
        
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
	Date date = new Date();
        
        String dateTime = dateFormat.format(date);
        Path recordFile = Paths.get("records",(fileHead + dateTime + fileTail));
        
        try{
            if(!Files.exists(recordFile) || Files.isDirectory(recordFile) || !Files.isReadable(recordFile)){
                Files.createFile(recordFile);
            }
        }
        catch(IOException ex){
            throw new IOException("Can't create record file: " + recordFile);
        }
        
        return recordFile;
    }
    
    /**
     * This method saves on a file all the flight movements saved in a FlightBuffer.
     * @param buffer FlightBuffer that contains all the movements done during a flight.
     * @param generatedPath Path of the file.
     * @throws IOException Thrown when the file is unaccessible.
     */
    private void saveFlightPattern(FlightBuffer buffer, Path generatedPath)throws IOException{
        try{
            while(buffer.existsNextCommand()){
                String command = buffer.getNextCommand();
                Files.write(generatedPath, (command+"\n").getBytes(), StandardOpenOption.APPEND);
            }
        }
        catch(IOException ex){
            throw new IOException("Error while writing the recording pattern on file " + generatedPath);
        }
    }
    
    /**
     * Main method. Used only for testing purposes.
     * @param args Non used.
     */
    public static void main(String[] args) {
        try{
            FlightRecorder recorder = new FlightRecorder();
            FlightBuffer buffer = new FlightBuffer();
            
            for(int i = 0; i < 100; i++) buffer.addMovement("pota");
            
            recorder.createBase();
            Path recordFile = recorder.generateRecordFile();
            
            recorder.saveFlightPattern(buffer, recordFile);
        }
        catch(IOException ex){
            System.err.println("ERROR:" + ex.getMessage());
        }
    }
}
