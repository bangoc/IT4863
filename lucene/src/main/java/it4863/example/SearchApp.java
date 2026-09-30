package it4863.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SearchApp {
    public static void main(String[] args) {
        // Expecting: [0] folderPath, [1] search text
        if (args.length < 1) {
            System.err.println("Error: Missing parameters.");
            System.err.println("Usage: mvn exec:java -Dexec.mainClass=\"it4863.example.SearchApp\" -Dexec.args=\"your search query\"");
            System.exit(1);
        }

        try {
            String rawQuery = args[0];
            LuceneSearcher searcher = new LuceneSearcher();
            searcher.executeSearch(rawQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
