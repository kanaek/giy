/**
 * Created by kanrun on 2016/2/17.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class NewWriter {
    public StringBuffer sb = new StringBuffer();
    public static String LINE_SEPARATOR = System.lineSeparator();

    public NewWriter(String filename, Map<String,Map<String,String>> sections) throws Exception{
        BufferedWriter writer = null;
        try {
            writer = initWriter(filename);
            InitIni(sections);
            writer.write(sb.toString());
        } finally {
            if (writer !=null) {
                writer.close();
            }
        }
    }

    private BufferedWriter initWriter(String filename) throws IOException {
        File file = new File(filename);

    }

}
