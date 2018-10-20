package net.ncguy.foundation.tools.profile;

import com.badlogic.gdx.Gdx;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProfilerHost {

    protected static ThreadLocal<Boolean> supportsGpu = ThreadLocal.withInitial(() -> false);

    public static void SupportsGPUProfiling() {
        supportsGpu.set(true);
    }

    public static boolean ThreadSupportsGPU() {
        return supportsGpu.get();
    }

    private static ProfilerHost instance;
    public static boolean PROFILER_ENABLED = true;
    private static boolean profilerEnabled;

    public List<BiConsumer<List<TaskStats>, List<TaskStats>>> onNotify;

    public static ProfilerHost instance() {
        if (instance == null)
            instance = new ProfilerHost();
        return instance;
    }

    public ConcurrentLinkedQueue<TaskStats> taskStats;

    private ProfilerHost() {
        taskStats = new ConcurrentLinkedQueue<>();
        onNotify = new ArrayList<>();
    }

    public static void Notify(BiConsumer<List<TaskStats>, List<TaskStats>> task) {
        instance().onNotify.add(task);
    }

    public static synchronized void Post(TaskStats payload) {
        instance().Add(payload);
    }

    private void Add(TaskStats payload) {
        taskStats.add(payload);
    }

    public void Iterate(Consumer<TaskStats> task) {
        TaskStats stat;
        List<TaskStats> stats = new ArrayList<>();

        while (!taskStats.isEmpty()) {
            stat = taskStats.poll();
            stats.add(stat);
        }

        stats.stream()
                .sorted(Comparator.comparingLong(a -> a.startTime))
                .forEach(task);
    }

    public void NotifyListeners() {
        List<TaskStats> cpu = this.GetCurrentStats("CPU");
        List<TaskStats> gpu = this.GetCurrentStats("GPU");
        onNotify.forEach(c -> c.accept(cpu, gpu));
    }

    public static void Clear() {
        instance().taskStats.clear();
    }

    public String GetFullDump() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Iterate(s -> s.Dump(stream));
        return stream.toString();
    }

    public Optional<TaskStats> GetLatestOfType(String type) {
        ArrayList<TaskStats> stats = new ArrayList<>(taskStats);
        return stats.stream()
                .filter(s -> s.type.equalsIgnoreCase(type))
                .findFirst();
    }

    public List<TaskStats> FlattenLatestOfTypes(String... types) {
        List<TaskStats> roots = new ArrayList<>();

        for (String type : types)
            GetLatestOfType(type).ifPresent(roots::add);

        final List<TaskStats> stats = new ArrayList<>();
        roots.forEach(r -> FlattenLatestOfTypes(r, stats));
        return stats;
    }

    public void FlattenLatestOfTypes(TaskStats stat, List<TaskStats> list) {
        list.add(stat);
        stat.children.forEach(c -> FlattenLatestOfTypes(c, list));
    }

    public List<TaskStats> GetCurrentStats(String type) {
        return FlattenLatestOfTypes(type).stream()
                .sorted(Comparator.comparingLong(a -> a.startTime))
                .collect(Collectors.toList());
    }

    public String GetCurrentDump(String type) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream print = new PrintStream(stream);

        List<TaskStats> flatList = FlattenLatestOfTypes(type);
        flatList.stream()
                .sorted(Comparator.comparingLong(a -> a.startTime))
                .map(TaskStats::ToDumpString)
                .forEach(print::println);

        return stream.toString();
    }

    public static void StartFrame() {

        profilerEnabled = PROFILER_ENABLED;

        CPUProfiler.Get().profilingEnabled = profilerEnabled;
        GPUProfiler.Get().profilingEnabled = profilerEnabled && ThreadSupportsGPU();

        if (!profilerEnabled)
            return;

        CPUProfiler.Get().setFrameCounter(Math.toIntExact(Gdx.graphics.getFrameId()));
        GPUProfiler.Get().setFrameCounter(Math.toIntExact(Gdx.graphics.getFrameId()));

        CPUProfiler.Get().StartFrame();
        GPUProfiler.Get().StartFrame();
    }

    public static void Start(String name) {
        if (!profilerEnabled)
            return;
        CPUProfiler.Get().Start(name);
        GPUProfiler.Get().Start(name);
    }

    public static void End(String name) {
        End();
    }

    public static void End() {
        if (!profilerEnabled)
            return;
        CPUProfiler.Get().End();
        GPUProfiler.Get().End();
    }

    public static void EndFrame() {
        if (!profilerEnabled)
            return;
        CPUProfiler.Get().EndFrame();
        GPUProfiler.Get().EndFrame();

        profilerEnabled = false;
    }

}
