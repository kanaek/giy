package test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class DoDiff {

	//声明SVN客户端管理类
	private static SVNClientManager ourClientManager;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//初始化支持svn://协议的库。 必须先执行此操作。
		SVNRepositoryFactoryImpl.setup();
		String name = "hanyi";
		String password = "hanyi";
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		//实例化客户端管理类
		ourClientManager = SVNClientManager.newInstance(
				(DefaultSVNOptions) options, name, password);
		//要比较的文件
		File compFile = new File("D:/svntest/svnkittest/branches/doImport.txt");
		//获得SVNDiffClient类的实例。
		SVNDiffClient diff=ourClientManager.getDiffClient();
		//保存比较结果的输出流
		BufferedOutputStream result;
		try {
			result = new BufferedOutputStream(new FileOutputStream("D:/result.txt"));
			//比较compFile文件的SVNRevision.WORKING版本和	SVNRevision.HEAD版本的差异，结果保存在D:/result.txt文件中。
			//SVNRevision.WORKING版本指工作副本中当前内容的版本，SVNRevision.HEAD版本指的是版本库中最新的版本。
			diff.doDiff(compFile, SVNRevision.HEAD, SVNRevision.WORKING, SVNRevision.HEAD, SVNDepth.INFINITY, true, result,null);
			result.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(SVNRevision.WORKING+"+++"+SVNRevision.HEAD);
		System.out.println("比较的结果保存在D:/result.txt文件中！");

	}

}
