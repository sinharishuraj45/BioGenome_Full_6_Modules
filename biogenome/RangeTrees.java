package biogenome;
public class RangeTrees {
    public static class SegmentTree {
        private int[] tree;
        private int n;

        public SegmentTree(String sequence) {
            this.n = sequence.length();
            tree = new int[4 * n];
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                char c = sequence.charAt(i);
                arr[i] = (c == 'G' || c == 'C') ? 1 : 0;
            }
            if(n > 0) build(arr, 0, 0, n - 1);
        }

        private void build(int[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
                return;
            }
            int mid = (start + end) / 2;
            build(arr, 2 * node + 1, start, mid);
            build(arr, 2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }

        public int queryGC(int l, int r) {
            if(n == 0) return 0;
            return query(0, 0, n - 1, l, r);
        }

        private int query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0; 
            if (l <= start && end <= r) return tree[node]; 
            
            int mid = (start + end) / 2;
            int p1 = query(2 * node + 1, start, mid, l, r);
            int p2 = query(2 * node + 2, mid + 1, end, l, r);
            return p1 + p2;
        }
    }
}