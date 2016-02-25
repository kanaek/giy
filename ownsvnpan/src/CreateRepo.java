import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-2-3
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */

/*
 本程序用于新建代码版本库（可能之后需要先验证用户权限再允许创建版本库）
 */
public class CreateRepo {
     public static void main(String[] args) {
         try {
             String tgtPath = "C:\\Program Files (x86)\\WANdisco\\uberSVN\\repositories\\newtest";
             SVNURL tgtURL = SVNRepositoryFactory.createLocalRepository(new File(tgtPath),true,false);

         }   catch (SVNException e) {
             e.printStackTrace();

         }
     }
}
