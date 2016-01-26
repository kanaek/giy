import java.io.ByteArrayInputStream;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-1-26
 * Time: 下午5:16
 * To change this template use File | Settings | File Templates.
 */
public class Commit {
    public static void main(String[] args) {
        setupLibrary();

        try {
            commitExample();
        }  catch (SVNException e) {
            SVNErrorMessage err = e.getErrorMessage();

            while (err != null) {
                System.err.println(err.getMessage());
                err = err.getChildErrorMessage();
            }
            System.exit(1);
        }
        System.exit(0);
    }

    private static void commitExample() throws SVNException {
        SVNURL url = SVNURL.parseURIEncoded("https://user-PC/svn/qq");
        String userName = "kr";
        String userPassword = "123";

        byte [] contents = "this is a new file".getBytes();
        byte [] modifiedContents = "this is the same file but modified a little".getBytes();

        SVNRepository repository = SVNRepositoryFactory.create(url);

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName,userPassword);
        repository.setAuthenticationManager(authManager);

        SVNNodeKind nodeKind = repository.checkPath("",-1);
        if (nodeKind == SVNNodeKind.NONE) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "No entry at URL '{0}'",url);
            throw new SVNException(err);
        }  else if (nodeKind == SVNNodeKind.FILE) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "Entry at URL ''{0}'' is a file while directory was expected", url);
            throw new SVNException(err);
        }

        long latestRevision = repository.getLatestRevision();
        System.out.println("repository latest revision before committing" + latestRevision);

        ISVNEditor editor = repository.getCommitEditor("directory and file added", null);
        editor.addDir("test", null,0);
        //System.out.println("The directory was added: " + commitInfo);
    }
}