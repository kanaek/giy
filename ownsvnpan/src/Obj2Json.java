/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-1-15
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
import net.sf.json.JSONObject;
public class Obj2Json {
    public String Object2Json(Object obj){
        JSONObject json = JSONObject.fromObject(obj);
        String str=json.toString();
        return str;
    }
}
