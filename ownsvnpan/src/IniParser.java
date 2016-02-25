/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-2-15
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
/**
 *通过解析可在权限文件中删除添加用户。创建[]版本库内权限，角色权限等。
 *利用自动机解析ini文件，参考csdn blog
 *1 定义状态
 * 1.1SectionState: 进入到某个Section Label的状态
 * 1.1.1如果进入到groups section，则可添加，删除用户
 * 1.1.2如果进入到其他的section, 则添加或删除repository的名字
 * 1.2 KeyState：进入处理key的状态
 * 1.3 ValueState：进入处理value的状态
 * 1.4 CommentState：进入处理注释状态
 *
 * 2 开始状态
 *   当存在'['与']'属性时，进入SectionState状态
 *   当存在';'时，进入CommentState
 *   当存在'
 *   2.2 SectionState状态：
 *   读入'['与']'当中的内容，返回开始状态 ：
 */
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;

public class IniParser {
     private File Filename;
     //private List<IdentityHashMap<String,String>> ll;
     //private Properties current;
     private IdentityHashMap<String,String> map;
     private List<GroupValue> gvL;
     private GroupValue gv;

    public static void main(String[] args) {
        /*
        String str1 = new String("xx");
        String str2 = new String(str1);
        System.out.println(str1 == str2);

        Map<String,String> map = new IdentityHashMap<String, String>();
        map.put(str1,"hello");
        map.put(str2,"world");
        for (Entry<String,String> entry:map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("containsKey---> " + map.containsKey("xx"));
        System.out.println("value----> " + map.get("xx"));
        */

     try {
         IniParser Ip = new IniParser();
         File file = new File("C:\\test\\svn.authz");
         Ip.IniReader(file);
     }  catch (Exception e) {
         e.printStackTrace();
     }

        String str = "zf,kr,qq";
        //System.out.println(str.indexOf(","));


    }

    public void IniReader(File filename) throws Exception{
        String GroupName;
        String KeyValue;
        gvL = new ArrayList<GroupValue>();
        /*
        ll = new ArrayList<IdentityHashMap<String, String>>();
        IdentityHashMap<String,String> map = new IdentityHashMap<String, String>();
        String str1 = new String("xx");
        String str2 = new String(str1);
        map.put(str1,"hello");
        map.put(str2,"world");
        ll.add(map);
        */
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String s = "";
        Date dt1 = new Date();
        while ((s=br.readLine()) !=null) {
           if (s.contains("[") && s.contains("]")) {
               gv = new GroupValue();
               //gv = new ArrayList<GroupValue>();
               GroupName = s.substring(s.indexOf("[")+1, s.indexOf("]"));
               gv.GroupName = GroupName;
           }
           else if (s.contains("=")) {
                 s = s.replace(" ","");
                 KeyValue = s.substring(0,s.indexOf("="));
                 System.out.println(KeyValue);
                 s = s.substring(s.indexOf("=")+1,s.length());
                 System.out.println(s);
                 while (s != null) {
                   //  = s.substring(0,s.indexOf(","));
                 }
           }

        }
        Date dt2 = new Date();
        System.out.println("查找索引----耗时:" + (dt2.getTime()-dt1.getTime())+"ms\n");
    }
}
