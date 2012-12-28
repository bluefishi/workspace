package com.apple.video.offlineprocess;
import java.io.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/*
 * author apple
 * Date 11 21*/
public class LuceneIndex {
	String sourcePath = null;
	String indexPath = null;
	
	public LuceneIndex(String sourcePath,String indexPath){
		this.sourcePath = sourcePath;
		this.indexPath = indexPath;
	}
	
	@SuppressWarnings("deprecation")
	private void buildindex() throws IOException
	{
		File f = new File(sourcePath);
		File[] list = f.listFiles();
		File file2 = new File(indexPath);
		Directory dir = FSDirectory.open(file2);
		Analyzer TextAnalyzer = new SimpleAnalyzer();
		IndexWriter TextIndex = new IndexWriter(dir,TextAnalyzer,IndexWriter.MaxFieldLength.LIMITED);
		for(int i=0;i<list.length;i++)
		{
			Document document = new Document();
			Field field_name = new Field("name",list[i].getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
			document.add(field_name);
			FileInputStream inputfile = new FileInputStream(list[i]);
			int len = inputfile.available();
			byte[] buffer = new byte[len];
			inputfile.read(buffer);
			inputfile.close();
			
			String contenttext = new String(buffer);
			Field field_content = new Field("content",contenttext,Field.Store.YES,Field.Index.ANALYZED);
			document.add(field_content);
			
			TextIndex.addDocument(document);
			TextIndex.optimize();
			
		}
		TextIndex.close();
		
		
	}
	public static void main(String args[])throws IOException
	{
		LuceneIndex bi = new LuceneIndex("lucene/sourcefile","lucene/index");
		bi.buildindex();
		System.out.println("ok");
	}

}
