package biogenome;
import java.util.*;
public class AlignmentDP {
    public static class AlignmentResult {
        public final int score; public final String alignedSeq1; public final String alignedSeq2;
        public final String matchVisual; public final int matches; public final int mismatches; public final int gaps;
        public AlignmentResult(int score, String alignedSeq1, String alignedSeq2) {
            this.score = score; this.alignedSeq1 = alignedSeq1; this.alignedSeq2 = alignedSeq2;
            StringBuilder mv = new StringBuilder();
            int mCount = 0, mmCount = 0, gCount = 0;
            for (int k = 0; k < alignedSeq1.length(); k++) {
                char c1 = alignedSeq1.charAt(k), c2 = alignedSeq2.charAt(k);
                if (c1 == '-' || c2 == '-') { mv.append(' '); gCount++; }
                else if (c1 == c2) { mv.append('|'); mCount++; }
                else { mv.append('.'); mmCount++; }
            }
            this.matchVisual = mv.toString(); this.matches = mCount; this.mismatches = mmCount; this.gaps = gCount;
        }
        public double getIdentityPercentage() {
            int total = matches + mismatches + gaps;
            return total == 0 ? 0.0 : (matches * 100.0) / total;
        }
    }
    public static int levenshteinDistance(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[] prev = new int[m + 1], curr = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(prev[j] + 1, Math.min(curr[j - 1] + 1, prev[j - 1] + cost));
            }
            System.arraycopy(curr, 0, prev, 0, m + 1);
        }
        return prev[m];
    }
    public static AlignmentResult needlemanWunsch(String seq1, String seq2, int match, int mismatch, int gap) {
        int n = seq1.length(), m = seq2.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i * gap;
        for (int j = 0; j <= m; j++) dp[0][j] = j * gap;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int diag = dp[i - 1][j - 1] + (seq1.charAt(i - 1) == seq2.charAt(j - 1) ? match : mismatch);
                int up = dp[i - 1][j] + gap, left = dp[i][j - 1] + gap;
                dp[i][j] = Math.max(diag, Math.max(up, left));
            }
        }
        StringBuilder a1 = new StringBuilder(), a2 = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + (seq1.charAt(i - 1) == seq2.charAt(j - 1) ? match : mismatch)) {
                a1.append(seq1.charAt(i - 1)); a2.append(seq2.charAt(j - 1)); i--; j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + gap) {
                a1.append(seq1.charAt(i - 1)); a2.append('-'); i--;
            } else {
                a1.append('-'); a2.append(seq2.charAt(j - 1)); j--;
            }
        }
        return new AlignmentResult(dp[n][m], a1.reverse().toString(), a2.reverse().toString());
    }
    public static AlignmentResult smithWaterman(String seq1, String seq2, int match, int mismatch, int gap) {
        int n = seq1.length(), m = seq2.length();
        int[][] dp = new int[n + 1][m + 1];
        int maxScore = 0, maxI = 0, maxJ = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int diag = dp[i - 1][j - 1] + (seq1.charAt(i - 1) == seq2.charAt(j - 1) ? match : mismatch);
                int up = dp[i - 1][j] + gap, left = dp[i][j - 1] + gap;
                dp[i][j] = Math.max(0, Math.max(diag, Math.max(up, left)));
                if (dp[i][j] > maxScore) { maxScore = dp[i][j]; maxI = i; maxJ = j; }
            }
        }
        StringBuilder a1 = new StringBuilder(), a2 = new StringBuilder();
        int i = maxI, j = maxJ;
        while (i > 0 && j > 0 && dp[i][j] > 0) {
            if (dp[i][j] == dp[i - 1][j - 1] + (seq1.charAt(i - 1) == seq2.charAt(j - 1) ? match : mismatch)) {
                a1.append(seq1.charAt(i - 1)); a2.append(seq2.charAt(j - 1)); i--; j--;
            } else if (dp[i][j] == dp[i - 1][j] + gap) {
                a1.append(seq1.charAt(i - 1)); a2.append('-'); i--;
            } else {
                a1.append('-'); a2.append(seq2.charAt(j - 1)); j--;
            }
        }
        return new AlignmentResult(maxScore, a1.reverse().toString(), a2.reverse().toString());
    }
    public static int bitmaskPrimerSelection(int numTargets, int[] primerCoverMasks, int[] primerCosts) {
        int maxState = 1 << numTargets;
        int[] dp = new int[maxState];
        Arrays.fill(dp, 1_000_000_000);
        dp[0] = 0;
        for (int mask = 0; mask < maxState; mask++) {
            if (dp[mask] == 1_000_000_000) continue;
            for (int i = 0; i < primerCoverMasks.length; i++) {
                int nextMask = mask | primerCoverMasks[i];
                dp[nextMask] = Math.min(dp[nextMask], dp[mask] + primerCosts[i]);
            }
        }
        return dp[maxState - 1];
    }
}
