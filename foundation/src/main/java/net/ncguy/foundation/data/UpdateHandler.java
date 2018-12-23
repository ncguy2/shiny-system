package net.ncguy.foundation.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

public class UpdateHandler {

    protected final Collection<Entity> entityList;
    protected final ForkJoinPool threadPool;


    public UpdateHandler(Collection<Entity> entityList) {
        this.entityList = entityList;
        threadPool = new ForkJoinPool();
    }

    protected Set<Entity> snapshot() {
        synchronized (entityList) {
            return new HashSet<>(entityList);
        }
    }

    protected void post(Runnable task) {
        threadPool.execute(task);
    }

    public void start(float delta) {
        updatePrePhysics(delta);
        updatePhysics(delta);
        updatePostPhysics(delta);
    }

    public void updatePrePhysics(float delta) {
        try {
            updateImpl(UpdatePhase.PRE_PHYSICS, delta);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updatePhysics(float delta) {
        try {
            // TODO integrate physics dispatch
            updateImpl(UpdatePhase.PHYSICS, delta);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updatePostPhysics(float delta) {
        try {
            updateImpl(UpdatePhase.POST_PHYSICS, delta);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void updateImpl(UpdatePhase phase, float delta) throws InterruptedException {
        Set<Entity> snapshot = snapshot();
        CountDownLatch latch = new CountDownLatch(snapshot.size());
        synchronized (this) {
            snapshot.forEach(e -> post(() -> {
                e.update(phase, delta);
                latch.countDown();
            }));
        }
        latch.await();
    }
}
