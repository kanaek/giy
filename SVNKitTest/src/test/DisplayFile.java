package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class DisplayFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//初始化库。 必须先执行此操作。具体操作封装在setupLibrary方法中。
        setupLibrary();
       
        /*
         * 相关变量赋值
         */
        String url = "https://user-PC/svn/qq/";
        String name = "kr";
        String password = "123";
        String filePath = "doImport.txt";
        //定义svn版本库的URL。
        SVNURL repositoryURL = null;
        //定义版本库。
        SVNRepository repository = null;
        try {
        	//获取SVN的URL。
        	repositoryURL=SVNURL.parseURIEncoded(url);
        	//根据URL实例化SVN版本库。
            repository = SVNRepositoryFactory.create(repositoryURL);
        } catch (SVNException svne) {
            /*
             * 打印版本库实例创建失败的异常。
             */
            System.err
                    .println("创建版本库实例时失败，版本库的URL是 '"
                            + url + "': " + svne.getMessage());
            System.exit(1);
        }

        /*
         * 对版本库设置认证信息。
         */
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);

        //此变量用来存放要查看的文件的属性名/属性值列表。
        SVNProperties fileProperties = new SVNProperties();
        //此输出流用来存放要查看的文件的内容。
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            //获得版本库中文件的类型状态（是否存在、是目录还是文件），参数-1表示是版本库中的最新版本。
            SVNNodeKind nodeKind = repository.checkPath(filePath, -1);
            
            if (nodeKind == SVNNodeKind.NONE) {
                System.err.println("要查看的文件在 '" + url + "'中不存在.");
                System.exit(1);
            } else if (nodeKind == SVNNodeKind.DIR) {
                System.err.println("要查看对应版本的条目在 '" + url
                        + "'中是一个目录.");
                System.exit(1);
            }
            //获取要查看文件的内容和属性，结果保存在baos和fileProperties变量中。
            repository.getFile(filePath, -1, fileProperties, baos);
            
        } catch (SVNException svne) {
            System.err.println("在获取文件内容和属性时发生错误: " + svne.getMessage());
            System.exit(1);
        }

        //获取文件的mime-type
        String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);
        //判断此文件是否是文本文件        
        boolean isTextType = SVNProperty.isTextMimeType(mimeType);
        /*
         * 显示文件的所有属性
         */
        Iterator iterator = fileProperties.nameSet().iterator();
        while (iterator.hasNext()) {
            String propertyName = (String) iterator.next();
            String propertyValue = fileProperties.getStringValue(propertyName);
            System.out.println("文件的属性: " + propertyName + "="
                    + propertyValue);
        }
        /*
         * 如果文件是文本类型，则把文件的内容显示到控制台。
         */
        if (isTextType) {
            System.out.println("File contents:");
            System.out.println();
            try {
                baos.writeTo(System.out);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            System.out
                    .println("因为文件不是文本文件，无法显示！");
        }
        System.out.println("");
        /*
         * 获得版本库的最新版本号。
         */
        long latestRevision = -1;
        try {
            latestRevision = repository.getLatestRevision();
            
            List<SVNLogEntry> entries = new ArrayList<SVNLogEntry>();
	        try
	        {
	            repository.log(new String[]{""},//为过滤的文件路径前缀，为空表示不进行过滤
	                    entries,
	                    -1,//-1代表最新的版本号，初始版本号为0
	                    -1,
	                    true,
	                    true);
	        }
	        catch (SVNException e)
	        {
	        	e.printStackTrace();
	        }
	        System.out.println("当前log信息数量:"+entries.size());
	        String message=entries.get(0).getMessage().toString();
	        System.out.println("提交的message信息："+message);
            
          
        } catch (SVNException svne) {
            System.err.println("获取最新版本号时出错: " + svne.getMessage());
            System.exit(1);
        }
        System.out.println("");
        System.out.println("---------------------------------------------");
        System.out.println("版本库的最新版本号: " + latestRevision);
        System.exit(0);

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
