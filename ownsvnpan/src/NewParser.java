import java.util.Map;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-2-16
 * Time: 上午10:24
 * To change this template use File | Settings | File Templates.
 * 正则表达式:http://blog.csdn.net/kdnuggets/article/details/2526588
 *操作ini文件:http://blog.csdn.net/Mr__fang/article/details/42030071
 */
public class NewParser {
    protected Map<String, Map<String,String>> sections;
    private transient String currentSection;
    private transient Map<String,String> current;
    private String[] notes = new String[] {";", "#", "//"};

    public NewParser(String filename) throws Exception {
        BufferedReader reader = null;
        try {
            File file = new File(filename);
            if (file.exists() && file.isFile()) {
                reader = new BufferedReader(new FileReader(file));
                reader(reader);
                reader.close();
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    protected void reader(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
    }

    protected void parseLine(String line) {
        line = line.trim();
        if (line.matches("\\[.*\\]")) {
            currentSection = line.replaceFirst("\\[.*\\]", "$1");
            current = new LinkedHashMap<String, String>();
            sections.put(currentSection.trim(), current);
        }  else if (line.matches(".*=.*") && !line.startsWith("#")) {
            if (current != null) {
                for (String str : notes) {
                    int num = line.indexOf(str);
                    if (num !=-1) {

                    }
                }
            }
        }
    }
}
