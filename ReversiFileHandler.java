import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * ReversiFileHandler is a small utility class with 
 * static methods to load and save games.
 * 
 * The files on disk can be in txt format. For files written
 * by this class, the format is the Java FILE format.
 *
 * @author Emmanuel Molefi
 * @version 10.05.2018
 */
public class ReversiFileHandler
{    
    /**
     * Write a text file to disk. The file format is txt. In case of any
     * problem the method just silently returns.
     * 
     * @param selectedFile The file to save to.
     * @param              The text data to be saved.
     */
    public static void saveToFile(File selectedFile, String data)
    {
        if(selectedFile.exists()) {
            selectedFile.delete();
        }        
        
        try (FileWriter writer = new FileWriter(selectedFile)) {
            writer.write(data);
        }
        catch(IOException e) {
            return;
        }
    }
    
    /**
     * Read a text file from disk and return the data as a string.
     * This method can read txt file formats. In case of any problem
     * (e.g the file does not exist, is malformed/corrupted, or any other
     * read error) this method reports with a message and returns null.
     * 
     * @param selectedFile The file to be loaded.
     * @return             The String object or null if it could not be read.
     */
    public static String loadFromFile(File selectedFile)
    {
        try{            
            Scanner line = new Scanner(selectedFile);
            String data = "";
                        
            while(line.hasNextLine()) {
                String response = line.nextLine();
                data += response;
            }
            
            return data;
        }
        catch(FileNotFoundException e) {
            System.err.println("Unable to open " + selectedFile.toPath());
        }
        catch(IOException e) {
            System.err.println("A problem was encountered reading " +
                               selectedFile.toPath());
        }
        
        return null;
    }
}