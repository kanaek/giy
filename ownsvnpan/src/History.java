/*
 * ====================================================================
 * Copyright (c) 2004-2011 TMate Software Ltd.  All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.  The terms
 * are also available at http://svnkit.com/license.html
 * If newer versions of this license are posted there, you may use a
 * newer version instead, at your option.
 * ====================================================================
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.*;

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
/*
 * The following example program demonstrates how you can use SVNRepository to
 * obtain a history for a range of revisions including (for each revision): all
 * changed paths, log message, the author of the commit, the timestamp when the 
 * commit was made. It is similar to the "svn log" command supported by the 
 * Subversion client library.
 * 
 * As an example here's a part of one of the program layouts (for the default
 * values):
 * 
 * ---------------------------------------------
 * revision: 1240
 * author: alex
 * date: Tue Aug 02 19:52:49 NOVST 2005
 * log message: 0.9.0 is now trunk
 *
 * changed paths:
 *  A  /trunk (from /branches/0.9.0 revision 1239)
 * ---------------------------------------------
 * revision: 1263
 * author: sa
 * date: Wed Aug 03 21:19:55 NOVST 2005
 * log message: updated examples, javadoc files
 *
 * changed paths:
 *  M  /trunk/doc/javadoc-files/javadoc.css
 *  M  /trunk/doc/javadoc-files/overview.html
 *  M  /trunk/doc/examples/src/org/tmatesoft/svn/examples/wc/StatusHandler.java
 * ...
 * 
 */
public class History {
    /*
     * args parameter is used to obtain a repository location URL, a start
     * revision number, an end revision number, user's account name & password
     * to authenticate him to the server.
     */
    public static void main(String[] args) {
        /*
         * Default values:
         */
        String url = "http://svn.svnkit.com/repos/svnkit/trunk/doc";
        String name = "anonymous";
        String password = "anonymous";
        long startRevision = 0;
        long endRevision = -1;//HEAD (the latest) revision
        /*
         * Initializes the library (it must be done before ever using the
         * library itself)
         */
        setupLibrary();

        if (args != null) {
            /*
             * Obtains a repository location URL
             */
            url = (args.length >= 1) ? args[0] : url;
            /*
             * Obtains the start point of the revisions range
             */
            startRevision = (args.length >= 2) ? Long.parseLong(args[1])
                    : startRevision;
            /*
             * Obtains the end point of the revisions range
             */
            endRevision = (args.length >= 3) ? Long.parseLong(args[2])
                    : endRevision;
            /*
             * Obtains an account name (will be used to authenticate the user to
             * the server)
             */
            name = (args.length >= 4) ? args[3] : name;
            /*
             * Obtains a password
             */
            password = (args.length >= 5) ? args[4] : password;
        }

        SVNRepository repository = null;

        try {
            /*
             * Creates an instance of SVNRepository to work with the repository.
             * All user's requests to the repository are relative to the
             * repository location used to create this SVNRepository.
             * SVNURL is a wrapper for URL strings that refer to repository locations.
             */
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException svne) {
            /*
             * Perhaps a malformed URL is the cause of this exception.
             */
            System.err
                    .println("error while creating an SVNRepository for the location '"
                            + url + "': " + svne.getMessage());
            System.exit(1);
        }

        /*
         * User's authentication information (name/password) is provided via  an 
         * ISVNAuthenticationManager  instance.  SVNWCUtil  creates  a   default 
         * authentication manager given user's name and password.
         * 
         * Default authentication manager first attempts to use provided user name 
         * and password and then falls back to the credentials stored in the 
         * default Subversion credentials storage that is located in Subversion 
         * configuration area. If you'd like to use provided user name and password 
         * only you may use BasicAuthenticationManager class instead of default 
         * authentication manager:
         * 
         *  authManager = new BasicAuthenticationsManager(userName, userPassword);
         *  
         * You may also skip this point - anonymous access will be used. 
         */
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);

        /*
         * Gets the latest revision number of the repository
         */
        try {
            endRevision = repository.getLatestRevision();
        } catch (SVNException svne) {
            System.err.println("error while fetching the latest repository revision: " + svne.getMessage());
            System.exit(1);
        }
        List<SVNLogEntry> logEntries = new ArrayList<SVNLogEntry>();
        //Collection logEntries = null;
        long latestRevision = -1;
        try {
            latestRevision = repository.getLatestRevision();

            //List<SVNLogEntry> logEntries = new ArrayList<SVNLogEntry>();
            try
            {
                repository.log(new String[]{""},//为过滤的文件路径前缀，为空表示不进行过滤
                        logEntries,
                        -1,//-1代表最新的版本号，初始版本号为0
                        -1,
                        true,
                        true);
            }
            catch (SVNException e)
            {
                e.printStackTrace();

            }
            System.out.println("当前log信息数量:"+logEntries.size());
            String message=logEntries.get(0).getMessage().toString();
            System.out.println("提交的message信息："+message);


        } catch (SVNException svne) {
            System.err.println("获取最新版本号时出错: " + svne.getMessage());
            System.exit(1);
        }

        for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
            /*
             * gets a next SVNLogEntry
             */
            SVNLogEntry logEntry = (SVNLogEntry) entries.next();
            System.out.println("---------------------------------------------");
            /*
             * gets the revision number
             */
            System.out.println("revision: " + logEntry.getRevision());
            /*
             * gets the author of the changes made in that revision
             */
            System.out.println("author: " + logEntry.getAuthor());
            /*
             * gets the time moment when the changes were committed
             */
            System.out.println("date: " + logEntry.getDate());
            /*
             * gets the commit log message
             */
            System.out.println("log message: " + logEntry.getMessage());
            /*
             * displaying all paths that were changed in that revision; cahnged
             * path information is represented by SVNLogEntryPath.
             */
            if (logEntry.getChangedPaths().size() > 0) {
                System.out.println();
                System.out.println("changed paths:");
                /*
                 * keys are changed paths
                 */
                Set changedPathsSet = logEntry.getChangedPaths().keySet();

                for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths
                        .hasNext();) {
                    /*
                     * obtains a next SVNLogEntryPath
                     */
                 //   SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry
                  //          .getChangedPaths().get(changedPaths.next());
                    /*
                     * SVNLogEntryPath.getPath returns the changed path itself;
                     * 
                     * SVNLogEntryPath.getType returns a charecter describing
                     * how the path was changed ('A' - added, 'D' - deleted or
                     * 'M' - modified);
                     * 
                     * If the path was copied from another one (branched) then
                     * SVNLogEntryPath.getCopyPath &
                     * SVNLogEntryPath.getCopyRevision tells where it was copied
                     * from and what revision the origin path was at.
                     */
              //      System.out.println(" "
              //              + entryPath.getType()
              //              + "	"
              //              + entryPath.getPath()
               //             + ((entryPath.getCopyPath() != null) ? " (from "
                //            + entryPath.getCopyPath() + " revision "
                //            + entryPath.getCopyRevision() + ")" : ""));
                }
            }
        }

    }

    /*
     * Initializes the library to work with a repository via 
     * different protocols.
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