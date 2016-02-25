import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

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
 * Date: 16-2-1
 * Time: 下午5:55
 * To change this template use File | Settings | File Templates.
 */
/*
 本文件用于在助理创建repository后自动建立各个下属目录，示例如下：
 trunk
    doc
      00.项目计划
      01.用户需求
      02.需求分析
      03.模版
      04.设计
      05.质量
      06.发布
      07.部署
      08.会议机要
      09.项目周报
      10.验收
      11.规范
    lib
    src
 branches

 tags
 */
public class AddTopDir {

    public static void main(String[] args) {
        setupLibrary();

        try {
            addDirect();
        }  catch (SVNException e) {
            e.printStackTrace();
        }
    }

    private static void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    private static void addDirect() throws SVNException {
        SVNURL url = SVNURL.parseURIEncoded("https://user-PC/svn/qq/");

        String[] twldirc = {"00.项目计划","01.用户需求","02.需求分析","03.模版","04.设计","05.质量","06.发布","07.部署","08.会议机要","09.项目周报","10.验收",
        "11.规范"};
        String userName = "kr";
        String userPassword = "123";

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
        System.out.println("repository latest revision before committing " + latestRevision);

        ISVNEditor editor = repository.getCommitEditor("add directory trunk,branches,tags",null);
        editor.openRoot(-1);
        editor.addDir("trunk",null,-1);
        editor.addDir("doc",null,-1);
       for (int i=0; i<= 11; i++) {
            editor.addDir(twldirc[i],null,-1);
            editor.closeDir();
            System.out.println(twldirc[i]);

        }

        editor.closeDir();
        editor.closeDir();

        editor.addDir("branches",null,-1);
        editor.closeDir();
        editor.addDir("tags",null,-1);
        editor.closeDir();
        SVNCommitInfo commitInfo = editor.closeEdit();


    }

}
