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
 * Date: 16-1-26
 * Time: 下午5:16
 * To change this template use File | Settings | File Templates.
 */
/*
本程序用于测试文件上传至SVN的功能。当文件名重名时，可选择覆盖或者新增。选择新增时，文件名+上传的时间，再上传
 */
public class UploadFile {
    public static void main(String[] args) {



        setupLibrary();

        try {
            commitExample();
        }   catch (SVNException e){
               e.printStackTrace();
        }   catch (IOException e) {
            e.printStackTrace();
        }


        /*
        try {
            readLocalFile();
        } catch (IOException e){
            e.printStackTrace();
        }  */

        System.exit(0);
    }

    private static void readLocalFile() throws IOException{
        FileInputStream fis = new FileInputStream("C:\\git\\giy\\svn.docx");      //file not found exception
        String FileName = "svn.docx";  //file name
        byte[] buff = new byte[fis.available()];
        int hasRead = 0;


        while ((hasRead = fis.read(buff)) > 0) {
            System.out.println(hasRead);
        }
        fis.close();
    }

    private static void commitExample() throws SVNException,IOException {
        SVNURL url = SVNURL.parseURIEncoded("https://user-PC/svn/qq/test1");
        String userName = "kr";
        String userPassword = "123";

        FileInputStream fis = new FileInputStream("C:\\git\\giy\\svn.docx");
        String UpFileName = "svn.docx";  //file name

        byte[] contents = new byte[fis.available()];

        fis.read(contents);
        fis.close();

        //byte [] contents = "this is a new file".getBytes();
        //byte [] modifiedContents = "this is the same file but modified a little".getBytes();

        SVNRepository repository = SVNRepositoryFactory.create(url);

        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName,userPassword);
        repository.setAuthenticationManager(authManager);

        SVNNodeKind nodeKind = repository.checkPath("/test1",-1);
        if (nodeKind == SVNNodeKind.NONE) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "No entry at URL '{0}'",url);
            throw new SVNException(err);
        }  else if (nodeKind == SVNNodeKind.FILE) {
            SVNErrorMessage err = SVNErrorMessage.create(SVNErrorCode.UNKNOWN, "Entry at URL ''{0}'' is a file while directory was expected", url);
            throw new SVNException(err);
        }

        long latestRevision = repository.getLatestRevision();
        System.out.println("repository latest revision before committing " + latestRevision);

        ISVNEditor editor = repository.getCommitEditor("directory and file added", null);
        try {
            SVNCommitInfo commitInfo = addDir(editor, "test1", UpFileName, contents);
        }  catch (SVNException e) {
            SVNErrorMessage err = e.getErrorMessage();
            if(err.getMessage().contains("already exists")) {
                //选择覆盖
                SVNCommitInfo deleInfo = deleteDir(editor,UpFileName);
                System.out.println("already delete");
                /*
                ISVNEditor editor2 = repository.getCommitEditor("directory and file added", null);

                try {
                    addDir(editor2, "test1", UpFileName,contents);
                    System.out.println("new svn.docx");
                }  catch (SVNException e1) {
                    e1.printStackTrace();
                }   */


                //选择新增
                ISVNEditor editor3 = repository.getCommitEditor("file name revised",null);
                SimpleDateFormat df = new SimpleDateFormat("yyMMddHH:mm:ss");
                String temp = df.format(new Date());
                StringBuilder test = new StringBuilder("svn.docx");
                test.insert(test.indexOf("."), temp);
                try {
                    addDir(editor3,"test1", test.toString(), contents);
                    System.out.println("upload" + test.toString() +"successfully");
                }  catch (SVNException e2) {
                    e2.printStackTrace();
                }



            } else {
                //暂时没有
            }
        }


        //SVNCommitInfo commitInfo = addDir(editor, "test1", UpFileName, contents);
        //System.out.println("The directory was added: " + commitInfo);
        //System.out.println("The directory was added: " + commitInfo);
    }
    /*
      deletion of a directory
     */
    private static SVNCommitInfo deleteDir(ISVNEditor editor, String dirPath) throws SVNException {
        editor.openRoot(-1);
        editor.deleteEntry(dirPath,-1);
        editor.closeDir();
        return editor.closeEdit();
    }

    private static SVNCommitInfo addDir(ISVNEditor editor, String dirPath,
                                        String filePath, byte[] data) throws SVNException{

        editor.openRoot(-1);

        //editor.addDir(dirPath,null,-1);

        editor.addFile(filePath,null,-1);

        //editor.addFile(filePath,null,-1);
        editor.applyTextDelta(filePath,null);

        //说明是delta还不知道
        SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
        String checksum = deltaGenerator.sendDelta(filePath,new ByteArrayInputStream(data),editor,
                true);

        editor.closeFile(filePath,checksum);

        //editor.closeDir();
        return editor.closeEdit();


    }

   // private static

    private static void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

}
