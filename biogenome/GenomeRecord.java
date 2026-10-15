package biogenome;
import java.io.*;
import java.util.*;
public class GenomeRecord {
    private final String id, description, sequence;
    public GenomeRecord(String id, String description, String sequence) {
        this.id = id; this.description = description; this.sequence = sequence.toUpperCase().replaceAll("[^ACGTN]", "");
    }
    public String getId() { return id; }
    public String getDescription() { return description; }
    public String getSequence() { return sequence; }
    public int length() { return sequence.length(); }
}
