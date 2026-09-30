package it4863.example;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneSearcher {

    private final String indexPath;
    private final Analyzer analyzer;

    public LuceneSearcher() {
        this.indexPath = "lucene_index_dir";
        this.analyzer = new StandardAnalyzer();
    }

    private List<String> tokenizeQueryText(String fieldName, String textQuery) throws IOException {
        List<String> terms = new ArrayList<>();
        
        try (TokenStream tokenStream = analyzer.tokenStream(fieldName, textQuery)) {
            CharTermAttribute termAttr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            
            while (tokenStream.incrementToken()) {
                terms.add(termAttr.toString());
            }
            
            tokenStream.end();
        }
        return terms;
    }

    public void executeSearch(String rawQueryText) throws IOException {
        try (FSDirectory directory = FSDirectory.open(Paths.get(indexPath));
             DirectoryReader reader = DirectoryReader.open(directory)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            
            searcher.setSimilarity(new BM25Similarity(1.2f, 0.75f));

            List<String> contentTokens = tokenizeQueryText("content", rawQueryText);

            BooleanQuery.Builder combinedQueryBuilder = new BooleanQuery.Builder();

            for (String token : contentTokens) {
                TermQuery termQuery = new TermQuery(new Term("content", token));
                combinedQueryBuilder.add(termQuery, BooleanClause.Occur.SHOULD);
            }

            BooleanQuery finalQuery = combinedQueryBuilder.build();
            TopDocs hits = searcher.search(finalQuery, 10);
            System.out.printf("%nFound %d matches for evaluated query syntax: [%s]%n", 
                    hits.totalHits.value(), finalQuery);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.storedFields().document(scoreDoc.doc);
                System.out.printf("Doc content: %s | Score (BM25): %.4f%n", 
                        doc.get("content"), scoreDoc.score);
            }
        }
    }
}
