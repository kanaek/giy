package test;

import java.io.File;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class CheckOut {

	//声明SVN客户端管理类
	private static SVNClientManager ourClientManager;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//初始化库。 必须先执行此操作。具体操作封装在setupLibrary方法中。
		setupLibrary();
		
		//相关变量赋值
		SVNURL repositoryURL = null;
		try {
			repositoryURL = SVNURL.parseURIEncoded("https://user-PC/svn/qq/");
		} catch (SVNException e) {
			//
		}
		String name = "kr";
		String password = "123";
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		
		//实例化客户端管理类
		ourClientManager = SVNClientManager.newInstance(
				(DefaultSVNOptions) options, name, password);
		
		//要把版本库的内容check out到的目录
		File wcDir = new File("c:/svntest/client");
		
		//通过客户端管理类获得updateClient类的实例。
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
		
		updateClient.setIgnoreExternals(false);
		
		//执行check out 操作，返回工作副本的版本号。
		long workingVersion = -1;
		try {
			workingVersion = updateClient
					.doCheckout(repositoryURL, wcDir, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY,false);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("把版本："+workingVersion+" check out 到目录："+wcDir+"中。");

	}
	/*
     * 初始化库
     */
    private static void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();
        
        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }


}
