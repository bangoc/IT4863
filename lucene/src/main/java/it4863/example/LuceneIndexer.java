package it4863.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class LuceneIndexer {

    private final Path indexPath;
    private final StandardAnalyzer analyzer;

    public LuceneIndexer() throws IOException {
        // Create a local folder on your computer to store the index files
        this.indexPath = Paths.get("lucene_index_dir");
        this.analyzer = new StandardAnalyzer();
    }

    public void indexFolder(String targetFolder) throws IOException {
        Path folderPath = Paths.get(targetFolder);
        if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            throw new IllegalArgumentException("The target folder does not exist or is not a directory: " + targetFolder);
        }

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try (FSDirectory directory = FSDirectory.open(indexPath);
             IndexWriter writer = new IndexWriter(directory, config);
             Stream<Path> paths = Files.walk(folderPath)) {

            paths.filter(Files::isRegularFile)
                 .forEach(path -> {
                     try {
                         String content = Files.readString(path);
                         Document doc = new Document();
                         doc.add(new TextField("content", content, Field.Store.YES));
                         writer.addDocument(doc);
                     } catch (IOException e) {
                         System.err.println("Failed to index file: " + path + " - " + e.getMessage());
                     }
                 });
            
            writer.commit();
            System.out.println("\nIndexing complete!");
        }
    }
}
