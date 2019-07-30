package nationalcipher.parallel;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import nationalcipher.util.CollectionUtil;

// Creates Jobs and assigns them to worker threads
public class MasterThread extends Thread {

    public final LinkedBlockingQueue<Runnable> jobs = new LinkedBlockingQueue<>(100000);
    private final ThreadGroup threadGroup = new ThreadGroup("job-queue");
    private final Set<WorkerThread> workers = new LinkedHashSet<WorkerThread>();
    private final Consumer<MasterThread> jobProvider;
    private volatile boolean finish = false;
    
    public MasterThread(Consumer<MasterThread> jobProvider) {
        this(Runtime.getRuntime().availableProcessors(), jobProvider);
    }
    
    public MasterThread(int numThreads, Consumer<MasterThread> jobProvider) {
        if (numThreads < 1 || jobProvider == null) {
            throw new IllegalArgumentException();
        }

        this.jobProvider = jobProvider;
        for (int i = 0; i < numThreads; i++) {
            this.workers.add(new WorkerThread(this, this.threadGroup));
        }
    }
    
    @Override
    public void run() {
        // Start all the workers
        this.workers.forEach(Thread::start);

//        this.workers.forEach(System.out::println);
        
        // Generate jobs, this may sleep while it waits for there to be space in the job queue
        this.jobProvider.accept(this);
        
        // Mark as finished to stop more jobs being added
        this.finish();
        System.out.println("Master Thread Ended");
    }
    
    public boolean addJob(Runnable job) {
        if(!this.finish && this.jobs.remainingCapacity() > 0) {
            return this.jobs.add(job);
        }
        
        return false;
    }
    
    @Nullable
    public Runnable getJob() {
        return this.jobs.poll();
    }
    
    public void finish() {
        this.finish = true;
    }
    
    public boolean isFinishing() {
        return this.finish;
    }
    
    public boolean completed() {
        return this.finish && this.jobs.isEmpty() && CollectionUtil.noneMatch(this.workers, WorkerThread::hasJobRunning);
    }

    /**
     * Stops any more jobs being added and removes all the current jobs
     * There may still be jobs being executed by the threads
     */
    public void shutdown() {
        this.finish();
        this.jobs.clear();
    }

    public void waitTillCompleted(Supplier<Boolean> shutdownCondition) {
        while (!this.completed()) {
            if (shutdownCondition.get()) {
                this.shutdown();
            }
            
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }

}
