package biogenome;
import java.util.*;
public class BioGenomeEngine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n==========================================================");
            System.out.println("  BioGenome™ - Full 6-Module DSA Bioinformatics Platform");
            System.out.println("==========================================================");
            System.out.println("[1] Module 1: String Algorithms (KMP, Rabin-Karp, Aho-Corasick)");
            System.out.println("[2] Module 2: Suffix Structures (Suffix Array, LCP)");
            System.out.println("[3] Module 3: Dynamic Programming (Needleman-Wunsch, Bitmask)");
            System.out.println("[4] Module 4: Graph Algorithms (De Bruijn Assembly)");
            System.out.println("[5] Module 5: Range Trees (Segment Tree GC Content)");
            System.out.println("[6] Module 6: Greedy & Backtracking (Scheduling & Probes)");
            System.out.println("[7] Exit");
            System.out.print("Select Module (1-7): ");
            
            String input = scanner.nextLine().trim();
            switch(input) {
                case "1": runModule1(scanner); break;
                case "2": runModule2(scanner); break;
                case "3": runModule3(scanner); break;
                case "4": runModule4(); break;
                case "5": runModule5(scanner); break;
                case "6": runModule6(); break;
                case "7": System.out.println("Exiting BioGenome... Goodbye!"); return;
                default: System.out.println("Invalid selection.");
            }
        }
    }
    
    private static void runModule1(Scanner scanner) {
        System.out.print("\nEnter DNA Sequence (e.g. ACGTACGTGACGTACTGACGT): ");
        String text = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Target Motif (e.g. CGT): ");
        String motif = scanner.nextLine().trim().toUpperCase();
        System.out.println("1. KMP Matches: " + StringAlgorithms.searchKMP(text, motif));
        System.out.println("2. Rabin-Karp Matches: " + StringAlgorithms.searchRabinKarp(text, motif));
        System.out.println("3. Z-Array Length: " + StringAlgorithms.computeZArray(text).length);
    }
    
    private static void runModule2(Scanner scanner) {
        System.out.print("\nEnter DNA Sequence for Suffix Indexing: ");
        String text = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Motif to Query: ");
        String motif = scanner.nextLine().trim().toUpperCase();
        int[] sa = SuffixStructures.buildSuffixArray(text);
        int[] lcp = SuffixStructures.buildLCPArray(text, sa);
        System.out.println("Suffix Array Binary Search Matches: " + SuffixStructures.searchWithSuffixArray(text, sa, motif));
        int maxLcp = 0; for (int v : lcp) if (v > maxLcp) maxLcp = v;
        System.out.println("Max repeated sequence length (Kasai LCP): " + maxLcp);
    }
    
    private static void runModule3(Scanner scanner) {
        System.out.print("\nEnter Sequence 1 (e.g. GATTACA): ");
        String s1 = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Sequence 2 (e.g. GCATGCU): ");
        String s2 = scanner.nextLine().trim().toUpperCase();
        AlignmentDP.AlignmentResult nw = AlignmentDP.needlemanWunsch(s1, s2, 1, -1, -2);
        System.out.println("\n[Needleman-Wunsch Global Alignment] Score: " + nw.score);
        System.out.println("Seq 1: " + nw.alignedSeq1);
        System.out.println("       " + nw.matchVisual);
        System.out.println("Seq 2: " + nw.alignedSeq2);
        
        System.out.println("\n[Bitmask DP Optimization]");
        int targets = 3; int[] cover = {0b001, 0b010, 0b110, 0b011}; int[] costs = {10, 15, 25, 20};
        System.out.println("Min Cost to cover 3 biological loci: $" + AlignmentDP.bitmaskPrimerSelection(targets, cover, costs));
    }

    private static void runModule4() {
        System.out.println("\n--- [Module 4: Graph Algorithms - De Bruijn Assembly] ---");
        List<String> kmers = Arrays.asList("ATG", "TGC", "GCC", "CCA", "CAG");
        System.out.println("Input fragmented DNA Reads (k-mers): " + kmers);
        GraphAlgorithms.DeBruijnGraph graph = new GraphAlgorithms.DeBruijnGraph();
        graph.buildFromKmers(kmers);
        System.out.println("Assembled Genome (Eulerian Path): " + graph.findEulerianPath());
    }

    private static void runModule5(Scanner scanner) {
        System.out.println("\n--- [Module 5: Range Trees - Segment Tree] ---");
        String seq = "GATTACAGCGGCTAACGCGT";
        System.out.println("DNA Sequence: " + seq + " (Length: " + seq.length() + ")");
        RangeTrees.SegmentTree st = new RangeTrees.SegmentTree(seq);
        System.out.print("Enter range [L, R] to query GC content (0 to 19): ");
        try {
            String[] parts = scanner.nextLine().split(" ");
            int l = Integer.parseInt(parts[0]), r = Integer.parseInt(parts[1]);
            int gcCount = st.queryGC(l, r);
            System.out.printf("GC Content from index %d to %d is: %d bases (%.2f%%)\n", l, r, gcCount, (gcCount * 100.0) / (r - l + 1));
        } catch(Exception e) { System.out.println("Invalid input."); }
    }

    private static void runModule6() {
        System.out.println("\n--- [Module 6: Greedy & Backtracking] ---");
        System.out.println("1. Greedy (Activity Selection) - Lab Machine Scheduling");
        List<GreedyBacktracking.LabTask> tasks = Arrays.asList(
            new GreedyBacktracking.LabTask("Sample A", 1, 4), new GreedyBacktracking.LabTask("Sample C", 0, 6),
            new GreedyBacktracking.LabTask("Sample B", 3, 5), new GreedyBacktracking.LabTask("Sample D", 5, 7)
        );
        for (GreedyBacktracking.LabTask t : GreedyBacktracking.scheduleSequencingTasks(tasks)) {
            System.out.println(" - " + t.name + " (" + t.startTime + " to " + t.endTime + ")");
        }
        System.out.println("\n2. Backtracking - Generate Exhaustive DNA Probes (Length 3)");
        List<String> motifs = GreedyBacktracking.generateAllMotifs(3);
        System.out.println("Total combinations generated: " + motifs.size());
        System.out.println("Preview: " + motifs.subList(0, 10) + " ... ");
    }
}
