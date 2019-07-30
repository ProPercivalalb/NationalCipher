package nationalcipher.parallel;

public class WorkerThread extends Thread {
    
    private MasterThread master;
    private Runnable currentJob;
    
    public WorkerThread(MasterThread master, ThreadGroup threadGroup) {
        super(threadGroup, "worker-thread");
        this.master = master;
    }
    
    @Override
    public void run() {
        while(true) {
            this.currentJob = this.master.getJob();
            
            if (this.currentJob != null) {
                this.currentJob.run();
                this.currentJob = null;
            } else {
                if (this.master.isFinishing()) {
                    break;
                }
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public boolean hasJobRunning() {
        return this.currentJob != null;
    }
}
