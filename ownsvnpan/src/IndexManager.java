/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 16-2-14
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */

/*
  试验搜索authz权限文件，删除指定用户成功。删除512个权限文件内的用户用时820ms（不使用apache lucene）
 */
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import jxl.Cell;       //读写excel文件
//import jxl.Sheet;
//import jxl.Workbook;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
//import org.apache.poi.hwpf.HWPFDocument;    //读写doc文件
//import org.apache.poi.hwpf.usermodel.Range;



public class IndexManager {
    public static void main(String[] args) {
         IndexManager ind = new IndexManager().getManager();
        boolean   bool = ind.createIndex("C:\\test");
        //ind.searchIndex("qq");

    }
    private static IndexManager indexManager;
    private static String content="";

    private static String INDEX_DIR="C:\\test";
    private static String DATA_DIR="C:\\test";
    private static Analyzer analyzer = null;
    private static Directory directory = null;
    private static IndexWriter indexWriter = null;

    public IndexManager getManager() {
        if (indexManager == null) {
            this.indexManager = new IndexManager();
        }
        return indexManager;
    }

    public static boolean createIndex(String path) {
        Date date1 = new Date();
        List<File> fileList = getFileList(path);
        for (File file:fileList) {
            content = "";
            String type = file.getName().substring(file.getName().lastIndexOf(".")+1);
            if ("conf".equalsIgnoreCase(type)){
                content += txt2String(file);
            }else if ("authz".equalsIgnoreCase(type)) {
                content += txt2String(file);
            }
            /*
            System.out.println("name :" +file.getName());
            System.out.println("path :"+file.getPath());
            System.out.println();
            try {
                analyzer = new StandardAnalyzer();
                //directory = new RAMDirectory();
                //下一行暂时不成功，需要对open的参数定义Path。
                directory = FSDirectory.open(Paths.get("c:/test", "test2"));

                //File indexFile = new File(INDEX_DIR);
                //if (!indexFile.exists()) {
                //    indexFile.mkdir();
                //}
                IndexWriterConfig config = new IndexWriterConfig(analyzer);
                indexWriter = new IndexWriter(directory,config);

                Document document = new Document();
                document.add(new TextField("filename",file.getName(),Store.YES));
                document.add(new TextField("content",content,Store.YES));
                document.add(new TextField("path",file.getPath(),Store.YES));
                indexWriter.addDocument(document);
                indexWriter.commit();
                closeWriter();

            }  catch (Exception e) {
                e.printStackTrace();
            }
             */


        }
        Date date2 = new Date();
        System.out.println("创建索引----耗时:"+(date2.getTime()-date1.getTime())+"ms\n");
        return true;

    }
    public static void closeWriter() throws Exception {
         if (indexWriter !=null) {
             indexWriter.close();

         }
    }

    public static void searchIndex(String text) {
        Date date1 = new Date();
        try {

            directory = FSDirectory.open(Paths.get("c:/test", "test2"));
            analyzer = new StandardAnalyzer();
            DirectoryReader ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);

            QueryParser parser = new QueryParser("content",analyzer);
            Query query = parser.parse(text);
            ScoreDoc[] hits = isearcher.search(query,null,1000).scoreDocs;
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                System.out.println("________________________");
                System.out.println(hitDoc.get("filename"));
                System.out.println(hitDoc.get("content"));
                System.out.println(hitDoc.get("path"));
                System.out.println("________________________");
            }
            ireader.close();
            directory.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date2 = new Date();
        System.out.println("查找索引----耗时:" + (date2.getTime()-date1.getTime())+"ms\n");
    }

    public static String txt2String(File file) {
        String result = "";
        try {
            BufferedReader br= new BufferedReader(new FileReader(file));
            String s= "";
            String s1="";
            while ((s= br.readLine()) !=null) {
                //result = result + "\n" +s;
                if (s.contains("kr.huang")) {
                    //StringBuilder sb = s.
                    //System.out.println(s.indexOf("kr"));
                    s = s.replace("kr.huang","");
                    //System.out.println(file.getName());

                    s1 = s1 + s + "\r\n";
                }else {
                    s1 = s1+ s +"\r\n";
                }
            }
            br.close();
            BufferedWriter br2 = new BufferedWriter(new FileWriter(file));
            br2.write(s1);
            br2.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static List<File> getFileList(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        List<File> fileList = new ArrayList<File>();
        for (File file: files) {
            if (isTxtFile(file.getName())) {
                fileList.add(file);
            }
        }
        return fileList;
    }
    public static boolean isTxtFile(String fileName) {
        if (fileName.lastIndexOf(".conf") >0) {
            return true;
        } else if (fileName.lastIndexOf(".authz") >0) {
            return true;
        }
        return false;
    }
}
