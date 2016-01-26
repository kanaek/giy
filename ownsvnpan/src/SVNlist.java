import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-1-15
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
public class SVNlist {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    private String name;
    private String path;
    private long size;
    private String URL;
    private String author;
    private long revision;

    public SVNlist(){
        name=null;
        path=null;
        size=0;
        URL=null;
        author=null;
    }

}
