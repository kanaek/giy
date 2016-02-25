import java.util.Map;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * 本文件用户解析各个svn版本库下的svn.authz文件
 * 尚未完成(2月18日)
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-2-16
 * Time: 上午10:24
 * To change this template use File | Settings | File Templates.
 * 正则表达式:http://blog.csdn.net/kdnuggets/article/details/2526588
 *操作ini文件:http://blog.csdn.net/Mr__fang/article/details/42030071
 */
public class AuthzParser {
    protected Map<String, Map<String,String>> sections = new LinkedHashMap<String, Map<String, String>>();
    private transient String currentSection;
    private transient Map<String,String> current;
    private String[] notes = new String[] {";", "#", "//"};

    public AuthzParser(String filename) throws Exception {
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

    public boolean isAuthzFile(String fileName) {
        if (fileName.lastIndexOf(".passwd") >0) {
            return true;
        }
        return false;
    }

    protected void reader(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            //System.out.println(line);
            parseLine(line);
        }
    }

    protected void parseLine(String line) {
        line = line.trim();
        if (line.matches("\\[.*\\]"))  {
            //System.out.println(line);
            currentSection = line.replaceFirst("\\[(.*)\\]", "$1");
            //System.out.println(currentSection);
            current = new LinkedHashMap<String, String>();
            //current.put(null,null);
            //System.out.println(current);
            sections.put(currentSection, current);
        }  else if (line.matches(".*=.*") && !line.startsWith("#")) {
            if (current != null) {
                for (String str : notes) {
                    int num = line.indexOf(str);
                    if (num !=-1) {
                        line = line.substring(0,num);
                    }
                }
                if (line.length() > 0 && line.indexOf("=") != -1) {
                    String[] kv = line.split("=",2);
                    current.put(kv[0].trim(), kv[1].trim());
                }
            }
        }
    }

   public String getValue(String section, String name) {
       Map<String,String> sectionmap = sections.get(section);

       if (sectionmap == null) {
           return null;
       }
       String value = sectionmap.get(name);
       return value;
   }

   public Set<String> sectionKeys() {
       return sections.keySet();
   }

   public Map<String,String> getValues(String section) {
       return sections.get(section);
   }

   public boolean containsKey(String section, String key) {
       Map<String,String> m = sections.get(section);
       if (m != null) {
           return m.get(key) == null;
       }
       return false;
   }

   public static void main(String[] args) {
       try {
           AuthzParser p = new AuthzParser("C:\\test\\svn.authz");
           String val = p.getValue("groups", "RW__test");
           Set<String> keys = p.sectionKeys();
           for(String k : keys){
               System.out.println("[" + k + "]");
               Map<String, String> q = p.getValues(k);
               for(Map.Entry<String, String> entry : q.entrySet()){
                   System.out.println(entry.getKey() + "==" + entry.getValue());
                   break;
               }
           }

           }  catch (Exception e) {
           e.printStackTrace();
       }
   }
}
