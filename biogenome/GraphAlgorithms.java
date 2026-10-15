package biogenome;
import java.util.*;
public class GraphAlgorithms {
    public static class DeBruijnGraph {
        private Map<String, List<String>> adj = new HashMap<>();
        private Map<String, Integer> inDegree = new HashMap<>();
        private Map<String, Integer> outDegree = new HashMap<>();

        public void addEdge(String from, String to) {
            adj.putIfAbsent(from, new ArrayList<>());
            adj.get(from).add(to);
            outDegree.put(from, outDegree.getOrDefault(from, 0) + 1);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            inDegree.putIfAbsent(from, 0);
            outDegree.putIfAbsent(to, 0);
        }
        public void buildFromKmers(List<String> kmers) {
            for (String kmer : kmers) {
                String prefix = kmer.substring(0, kmer.length() - 1);
                String suffix = kmer.substring(1);
                addEdge(prefix, suffix);
            }
        }
        public String findEulerianPath() {
            String startNode = null;
            for (String node : adj.keySet()) {
                if (outDegree.get(node) - inDegree.get(node) == 1) {
                    startNode = node; break;
                }
            }
            if (startNode == null) startNode = adj.keySet().iterator().next(); 
            Stack<String> stack = new Stack<>();
            List<String> path = new ArrayList<>();
            stack.push(startNode);
            Map<String, List<String>> tempAdj = new HashMap<>(adj);

            while (!stack.isEmpty()) {
                String u = stack.peek();
                if (tempAdj.containsKey(u) && !tempAdj.get(u).isEmpty()) {
                    stack.push(tempAdj.get(u).remove(0));
                } else {
                    path.add(stack.pop());
                }
            }
            Collections.reverse(path);
            if (path.isEmpty()) return "";
            StringBuilder genome = new StringBuilder(path.get(0));
            for (int i = 1; i < path.size(); i++) {
                genome.append(path.get(i).charAt(path.get(i).length() - 1));
            }
            return genome.toString();
        }
    }
}
