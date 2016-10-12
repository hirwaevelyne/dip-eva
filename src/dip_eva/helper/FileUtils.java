package dip_eva.helper;

import java.io.File;
import java.io.IOException;


public class FileUtils {
    public static File getTempFile()  {
        File dir = new File("temp/compress");

        if (!dir.isDirectory()) {
            dir.mkdirs();
        }

        File output = new File(dir, "output.jpg");
        try {
            if (!output.exists()) {

                output.createNewFile();
                return output;
            } else {
                return output;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
