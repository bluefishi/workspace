package com.apple.video.offlineprocess;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/*
 * Author:apple
 * Date: 28th Dec 2012
 * input: index path and search word
 * output: result files number and file names*/
public class LuceneSearch {
	String indexPath = null;
	String searchWord = null;
	
	public LuceneSearch(String indexPath,String searchWord){
		this.indexPath = indexPath;
		this.searchWord = searchWord;
	}
	
	@SuppressWarnings("deprecation")
	private void search() throws IOException
	{
		File fIndex_Path = new File(indexPath);
		Directory Index_Dir = FSDirectory.open(fIndex_Path);
		IndexSearcher searcher = new IndexSearcher(Index_Dir);
		Term t = new Term("content",searchWord);
		TermQuery query = new TermQuery(t);
		ScoreDoc[] hits = searcher.search(query, null,100).scoreDocs;
		System.out.println("Search results:"+hits.length);
		for(int i=0;i<hits.length;i++)
		{
			Document hitDoc = searcher.doc(hits[i].doc);
			System.out.println(hitDoc.get("name"));
		}
	}
	public static void main(String args[]) throws IOException
	{
		LuceneSearch ls = new LuceneSearch("lucene/index","a");
		ls.search();
	}

}
