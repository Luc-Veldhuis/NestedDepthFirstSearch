package ndfs.mcndfs_1_improved;

import ndfs.CycleFoundException;
import ndfs.NDFS;
import ndfs.NoCycleFoundException;
import ndfs.ResultException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a straightforward implementation of Figure 1 of
 * <a href="http://www.cs.vu.nl/~tcs/cm/ndfs/laarman.pdf">
 * "the Laarman paper"</a>.
 *
 * This class should be modified/extended to implement Figure 2 of this paper.
 */
public class NNDFS implements NDFS {

    private final Colors red = new Colors();
    private final File promelaFile;
    private final int numberOfWorkers;
    private final ResultTracker resultTracker;

    /**
     * Constructs an NDFS object using the specified Promela file.
     *
     * @param promelaFile
     *            the Promela file.
     * @param nrWorkers
     *            the number of worker threads to use.
     */
    public NNDFS(File promelaFile, int nrWorkers) {

        this.promelaFile = promelaFile;
        this.numberOfWorkers = nrWorkers;
        this.resultTracker = new ResultTracker(this.numberOfWorkers);
    }

    @Override
    public void ndfs() throws ResultException {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfWorkers);
        Worker[] workers = new Worker[numberOfWorkers];
        ConcurrentHashMap<Integer, AtomicInteger> stateCounters = new ConcurrentHashMap<Integer, AtomicInteger>();
        for(int i = 0; i < numberOfWorkers; i++) {
            try {
                workers[i] = new Worker(promelaFile, i, red, resultTracker, this, numberOfWorkers, stateCounters);
                executor.execute(workers[i]);
            } catch (FileNotFoundException file) {
                System.out.println(file);
                file.printStackTrace();
                System.exit(1);
            }
        }
        //wait for threads to die
        try{
            synchronized (this){
                while (!resultTracker.hasCycle() && !resultTracker.allFilled() && !resultTracker.crashed()) {
                    this.wait();
                }
            }
        } catch(InterruptedException error) {
            throw new Error("Main thread interrupted");
        }
        if(resultTracker.crashed()) {
            throw new Error("Something went wrong. This is not good");
        }
        executor.shutdownNow();
        //all threads finished or cycle is found
        if( resultTracker.hasCycle()) {
            throw new CycleFoundException();
        } else {
            throw new NoCycleFoundException();
        }
    }
}
