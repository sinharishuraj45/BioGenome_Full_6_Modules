package biogenome;
import java.util.*;
public class GreedyBacktracking {
    public static class LabTask implements Comparable<LabTask> {
        String name; int startTime; int endTime;
        public LabTask(String name, int startTime, int endTime) {
            this.name = name; this.startTime = startTime; this.endTime = endTime;
        }
        @Override
        public int compareTo(LabTask other) { return Integer.compare(this.endTime, other.endTime); }
    }
    public static List<LabTask> scheduleSequencingTasks(List<LabTask> tasks) {
        Collections.sort(tasks); 
        List<LabTask> selectedTasks = new ArrayList<>();
        if (tasks.isEmpty()) return selectedTasks;
        
        LabTask lastSelected = tasks.get(0);
        selectedTasks.add(lastSelected);
        
        for (int i = 1; i < tasks.size(); i++) {
            if (tasks.get(i).startTime >= lastSelected.endTime) {
                selectedTasks.add(tasks.get(i));
                lastSelected = tasks.get(i);
            }
        }
        return selectedTasks;
    }
    public static List<String> generateAllMotifs(int length) {
        List<String> results = new ArrayList<>();
        char[] bases = {'A', 'C', 'G', 'T'};
        backtrackMotif(results, new StringBuilder(), bases, length);
        return results;
    }
    private static void backtrackMotif(List<String> results, StringBuilder current, char[] bases, int length) {
        if (current.length() == length) {
            results.add(current.toString());
            return;
        }
        for (char base : bases) {
            current.append(base);
            backtrackMotif(results, current, bases, length);
            current.deleteCharAt(current.length() - 1); 
        }
    }
}
