package test;

import java.io.File;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class DoImport {

	//声明SVN客户端管理类
	private static SVNClientManager ourClientManager;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
		//相关变量赋值
		SVNURL repositoryURL = null;
		try {
			repositoryURL = SVNURL.parseURIEncoded("https://hy/svn/svnkittest/");
		} catch (SVNException e) {
			//
		}
		String name = "hanyi";
		String password = "hanyi";
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		//实例化客户端管理类
		ourClientManager = SVNClientManager.newInstance(
				(DefaultSVNOptions) options, name, password);
		//要把此目录中的内容导入到版本库
		File impDir = new File("D:/svntest");
		//执行导入操作
		SVNCommitInfo commitInfo = null;
		try {
			commitInfo = ourClientManager.getCommitClient().doImport(impDir, repositoryURL,
					"import operation!",null, false,false,SVNDepth.INFINITY);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println(commitInfo.toString());

	}

}
