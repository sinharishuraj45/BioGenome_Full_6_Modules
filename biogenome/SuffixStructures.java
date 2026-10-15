package biogenome;
import java.util.*;
public class SuffixStructures {
    public static int[] buildSuffixArray(String text) {
        int n = text.length();
        Integer[] sa = new Integer[n];
        int[] rank = new int[n], temp = new int[n];
        for (int i = 0; i < n; i++) { sa[i] = i; rank[i] = text.charAt(i); }
        for (int k = 1; k < n; k <<= 1) {
            final int offset = k;
            final int[] currentRank = rank.clone();
            Arrays.sort(sa, (a, b) -> {
                if (currentRank[a] != currentRank[b]) return Integer.compare(currentRank[a], currentRank[b]);
                int rankA = (a + offset < n) ? currentRank[a + offset] : -1;
                int rankB = (b + offset < n) ? currentRank[b + offset] : -1;
                return Integer.compare(rankA, rankB);
            });
            temp[sa[0]] = 0;
            for (int i = 1; i < n; i++) {
                int prev = sa[i - 1], curr = sa[i];
                boolean sameFirst = currentRank[prev] == currentRank[curr];
                int r1 = (prev + offset < n) ? currentRank[prev + offset] : -1;
                int r2 = (curr + offset < n) ? currentRank[curr + offset] : -1;
                temp[curr] = (sameFirst && r1 == r2) ? temp[prev] : temp[prev] + 1;
            }
            System.arraycopy(temp, 0, rank, 0, n);
            if (rank[sa[n - 1]] == n - 1) break;
        }
        int[] result = new int[n];
        for (int i = 0; i < n; i++) result[i] = sa[i];
        return result;
    }
    public static int[] buildLCPArray(String text, int[] sa) {
        int n = text.length();
        int[] rank = new int[n], lcp = new int[n];
        for (int i = 0; i < n; i++) rank[sa[i]] = i;
        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];
                while (i + h < n && j + h < n && text.charAt(i + h) == text.charAt(j + h)) h++;
                lcp[rank[i]] = h;
                if (h > 0) h--;
            }
        }
        return lcp;
    }
    public static List<Integer> searchWithSuffixArray(String text, int[] sa, String pattern) {
        List<Integer> matches = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0 || n < m) return matches;
        int low = 0, high = n - 1, startIdx = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            String suffix = text.substring(sa[mid], Math.min(sa[mid] + m, n));
            if (suffix.compareTo(pattern) >= 0) { startIdx = mid; high = mid - 1; }
            else { low = mid + 1; }
        }
        if (startIdx == -1 || !text.startsWith(pattern, sa[startIdx])) return matches;
        low = 0; high = n - 1;
        int endIdx = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            String suffix = text.substring(sa[mid], Math.min(sa[mid] + m, n));
            if (suffix.compareTo(pattern) <= 0) { endIdx = mid; low = mid + 1; }
            else { high = mid - 1; }
        }
        for (int i = startIdx; i <= endIdx; i++) {
            if (text.startsWith(pattern, sa[i])) matches.add(sa[i]);
        }
        Collections.sort(matches);
        return matches;
    }
}
