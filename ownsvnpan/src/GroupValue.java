import java.util.IdentityHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-2-15
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.Entry;

public class GroupValue {
    public String GroupName;
    public List<IdentityHashMap<String,String>> KeyValue;
    public void GroupValue(String groupName) {
        this.GroupName = groupName;
        KeyValue = new ArrayList<IdentityHashMap<String, String>>();
    }

}
