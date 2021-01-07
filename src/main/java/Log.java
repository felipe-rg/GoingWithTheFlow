
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    static Logger logger;
    public FileHandler fileHandler;                  // fileHandler will maintain the logging file

    public Log () throws IOException {

        String fileName = "fileName";
        // checks whether a logging file already exists, if not a log file will be created
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        fileHandler = new FileHandler(fileName, true);  // this allows the file to be appended
        logger = Logger.getLogger(Log.class.getName());                     // retrieves logged errors
        logger.addHandler(fileHandler);                        // adds logged errors to the file handler
        SimpleFormatter formatter = new SimpleFormatter();     // sets format of log handler
        fileHandler.setFormatter(formatter);
    }
}
