import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;


/**
 * 本文件用于解析svn.passwd文件
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-2-18
 * Time: 上午9:35
 * To change this template use File | Settings | File Templates.
 */
public class PasswdParser {
    private List<String> Username = new ArrayList<String>();

    public PasswdParser(String filename) throws Exception {
        BufferedReader reader = null;
        try {
            File file = new File(filename);
            if (file.exists() && isPassFile(filename)) {
                reader = new BufferedReader(new FileReader(file));
                reader(reader);
                reader.close();
            }
            else {
                System.err.println("文件不存在或不是passwd文件");

            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public boolean isPassFile(String fileName) {
        if (fileName.lastIndexOf(".passwd") >0) {
            return true;
        }
        return false;
    }

    public void reader(BufferedReader reader) throws Exception {
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            //System.out.println(line);
            parseLine(line);
            //System.out.println(Username.get(i));
            i++;

        }
    }

    public boolean search(String username) {
        if (username == null) {
            return false;
        } else {
            Iterator<String> itr = Username.iterator();
            while (itr.hasNext()) {
                if (username.equals(itr.next())) return true;
            }

        }
        return false;
    }
    public boolean delete(String username) {
        String temp;
        if (username == null) {
            return false;
        }  else {
            Iterator<String> iter  = Username.iterator();
            while (iter.hasNext()) {
               temp = iter.next();
               if (temp.equals(username)) {
                   iter.remove();
                   return true;
                   //break;
               }
            }


        }
        return false;
    }

    public void parseLine(String line) {
        String username;
        //int i = 0;
        line = line.trim();
        if (line.matches(".*:.*")) {
            username = line.substring(0,line.indexOf(":"));
            //System.out.println(username);
            Username.add(username);
            //System.out.println(Username.get(0));
            //i++;
            username = null;
        }
    }


    public static void main(String[] args) {
        try {
            PasswdParser pp = new PasswdParser("C:\\test\\svn.passwd");
            System.out.println(pp.search("kr"));
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }

}
