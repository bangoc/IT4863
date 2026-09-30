package it4863.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IndexApp {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: Missing target folder argument.");
            System.err.println("Usage: mvn exec:java -Dexec.mainClass=\"it4863.example.IndexApp\" -Dexec.args=\"/path/to/your/documents\"");
            System.exit(1);
        }

        String folderToScan = args[0];
        Path targetPath = Paths.get(folderToScan);

        if (!Files.exists(targetPath) || !Files.isDirectory(targetPath)) {
            System.err.println("Error: The path '" + folderToScan + "' does not exist or is not a directory.");
            System.exit(1);
        }

        try {
            LuceneIndexer indexer = new LuceneIndexer();
            
            System.out.println("Scanning and indexing target folder: " + targetPath.toAbsolutePath());
            indexer.indexFolder(folderToScan);
            
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during indexing/searching:");
            e.printStackTrace();
        }
    }
}
