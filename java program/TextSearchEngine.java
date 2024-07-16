import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TextSearchEngine {
    // Inverted index data structure using a Trie
    private Trie index = new Trie();

    // Document class to store document metadata
    private static class Document {
        String id;
        String title;
        String content;

        public Document(String id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }
    }

    // Trie node class to store word prefixes
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<Document> documents = new ArrayList<>();
    }

    // Trie class to store the inverted index
    private class Trie {
        TrieNode root = new TrieNode();

        void addWord(String word, Document document) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
            }
            node.documents.add(document);
        }

        List<Document> search(String query) {
            TrieNode node = root;
            for (char c : query.toCharArray()) {
                node = node.children.get(c);
                if (node == null) return Collections.emptyList();
            }
            return node.documents;
        }
    }

    // Add a document to the index
    public void addDocument(String id, String title, String content) {
        Document doc = new Document(id, title, content);
        String[] words = content.split("\\s+");

        for (String word : words) {
            word = word.toLowerCase(); // case-insensitive search
            index.addWord(word, doc);
        }
    }

    // Search for documents containing a query
    public List<Document> search(String query) {
        query = query.toLowerCase(); // case-insensitive search
        List<Document> results = index.search(query);

        // Use a HashSet to remove duplicates and preserve order
        Set<Document> uniqueResults = new LinkedHashSet<>(results);
        return new ArrayList<>(uniqueResults);
    }

    public static void main(String[] args) {
        TextSearchEngine searchEngine = new TextSearchEngine();

        // Add documents from files
        addDocumentFromFile(searchEngine, "doc1", "document1.txt");
        addDocumentFromFile(searchEngine, "doc2", "document2.txt");
        addDocumentFromFile(searchEngine, "doc3", "document3.txt");
        addDocumentFromFile(searchEngine, "doc4", "document4.txt");
        addDocumentFromFile(searchEngine, "doc5", "document5.txt");

        // Search for documents containing the query "Java"
        List<TextSearchEngine.Document> results = searchEngine.search("blooming"); // Use TextSearchEngine.Document
        System.out.println("Search results for 'blooming':");
        for (TextSearchEngine.Document doc : results) {
            System.out.println(doc.title);
        }

        // Search for documents containing the query "programming"
        results = searchEngine.search("sun-kissed");
        System.out.println("Search results for 'sun-kissed':");
        for (TextSearchEngine.Document doc : results) {
            System.out.println(doc.title);
        }
    }
    private static void addDocumentFromFile(TextSearchEngine searchEngine, String id, String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            searchEngine.addDocument(id, filename, content.toString());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }
}
