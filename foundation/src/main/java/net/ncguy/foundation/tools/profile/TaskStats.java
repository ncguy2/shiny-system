package net.ncguy.foundation.tools.profile;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TaskStats {

    public final String type;
    public final String name;
    public final long startTime;
    public final long endTime;
    public final ArrayList<TaskStats> children;
    public final int depth;
    public final int frame;

    public final StackTraceElement startLocation;
    public final StackTraceElement endLocation;

    public final long approximateOverhead;
    public final long totalLocationDiscoveryCost;

    public TaskStats(final GPUTaskProfile task) {
        this("GPU", task);
    }

    public TaskStats(final CPUTaskProfile task) {
        this("CPU", task);
    }

    private TaskStats(String type, TaskProfile<?> task) {
        this.type = type;
        this.name = task.name;
        this.frame = task.frame;
        this.startTime = task.getStartTime();
        this.endTime = task.getEndTime();
        this.children = new ArrayList<>();
        this.depth = task.depth;
        this.startLocation = task.startLocation;
        this.endLocation = task.endLocation;
        this.approximateOverhead = task.approximateOverhead;
        this.totalLocationDiscoveryCost = task.totalLocationDiscoveryCost;

        task.getChildren()
                .stream()
                .map(t -> new TaskStats(type, t))
                .forEach(this.children::add);
    }

    public float GetOverheadMs() {
        return approximateOverhead * 0.000001f;
    }
    public float GetLocationCostMs() {
        return totalLocationDiscoveryCost * 0.000001f;
    }

    public float CalculateOverheadRatio() {
        float msTaken = getMsTaken();
        return GetOverheadMs() / msTaken;
    }

    public String GetOverheadRatioString() {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(CalculateOverheadRatio() * 100) + "%";
    }

    public String GetOverheadString() {
        DecimalFormat formatter = new DecimalFormat("#0.0000");
        return formatter.format(GetOverheadMs());
    }
    public String GetLocationCostString() {
        DecimalFormat formatter = new DecimalFormat("#0.0000");
        return formatter.format(GetLocationCostMs());
    }

    public float getMsTaken() {
        return getTimeTaken() * 0.000001f;
    }

    public long getTimeTaken() {
        return this.endTime - this.startTime;
    }

    @Override
    public String toString() {

        DecimalFormat df = new DecimalFormat("#.####");
        return "[" +
                type +
                "] " +
                name +
                " : " +
                df.format(getTimeTaken() / 1000f / 1000f) +
                "ms";
    }

    public void Dump() {
        Dump(System.out);
    }

    public void Dump(OutputStream out) {
        Dump(new PrintStream(out));
    }

    public void Dump(PrintStream out) {
        out.println(toString());

        for (TaskStats child : children)
            child.Dump(out);
    }

    public String ToDumpString() {
        String singleIndent = "    ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++)
            sb.append(singleIndent);
        return sb.append(toString()).toString();
    }
}
