package biogenome;
import java.util.*;
public class StringAlgorithms {
    public static List<Integer> searchKMP(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0 || n < m) return matches;
        int[] pi = computePrefixFunction(pattern);
        int q = 0;
        for (int i = 0; i < n; i++) {
            while (q > 0 && pattern.charAt(q) != text.charAt(i)) q = pi[q - 1];
            if (pattern.charAt(q) == text.charAt(i)) q++;
            if (q == m) { matches.add(i - m + 1); q = pi[q - 1]; }
        }
        return matches;
    }
    private static int[] computePrefixFunction(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];
        int k = 0;
        for (int i = 1; i < m; i++) {
            while (k > 0 && pattern.charAt(k) != pattern.charAt(i)) k = pi[k - 1];
            if (pattern.charAt(k) == pattern.charAt(i)) k++;
            pi[i] = k;
        }
        return pi;
    }
    public static int[] computeZArray(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i <= r) z[i] = Math.min(r - i + 1, z[i - l]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] - 1 > r) { l = i; r = i + z[i] - 1; }
        }
        return z;
    }
    public static List<Integer> searchRabinKarp(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0 || n < m) return matches;
        final long BASE = 256, MOD = 1_000_000_007L;
        long pHash = 0, tHash = 0, power = 1;
        for (int i = 0; i < m - 1; i++) power = (power * BASE) % MOD;
        for (int i = 0; i < m; i++) {
            pHash = (pHash * BASE + pattern.charAt(i)) % MOD;
            tHash = (tHash * BASE + text.charAt(i)) % MOD;
        }
        for (int i = 0; i <= n - m; i++) {
            if (pHash == tHash && text.startsWith(pattern, i)) matches.add(i);
            if (i < n - m) {
                tHash = (tHash - text.charAt(i) * power) % MOD;
                tHash = (tHash * BASE + text.charAt(i + m)) % MOD;
                if (tHash < 0) tHash += MOD;
            }
        }
        return matches;
    }
    public static class AhoCorasick {
        static class Node {
            Map<Character, Node> children = new HashMap<>();
            Node fail = null;
            List<String> output = new ArrayList<>();
        }
        private final Node root = new Node();
        public void addPattern(String pattern) {
            Node curr = root;
            for (char c : pattern.toCharArray()) {
                curr = curr.children.computeIfAbsent(c, k -> new Node());
            }
            curr.output.add(pattern);
        }
        public void buildAutomaton() {
            Queue<Node> queue = new LinkedList<>();
            for (Node child : root.children.values()) {
                child.fail = root;
                queue.add(child);
            }
            while (!queue.isEmpty()) {
                Node curr = queue.poll();
                for (Map.Entry<Character, Node> entry : curr.children.entrySet()) {
                    char c = entry.getKey();
                    Node child = entry.getValue();
                    Node f = curr.fail;
                    while (f != null && !f.children.containsKey(c)) f = f.fail;
                    child.fail = (f == null) ? root : f.children.get(c);
                    child.output.addAll(child.fail.output);
                    queue.add(child);
                }
            }
        }
        public Map<String, List<Integer>> search(String text) {
            Map<String, List<Integer>> results = new HashMap<>();
            Node curr = root;
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                while (curr != null && !curr.children.containsKey(c)) curr = curr.fail;
                curr = (curr == null) ? root : curr.children.get(c);
                for (String pattern : curr.output) {
                    results.computeIfAbsent(pattern, k -> new ArrayList<>()).add(i - pattern.length() + 1);
                }
            }
            return results;
        }
    }
}
