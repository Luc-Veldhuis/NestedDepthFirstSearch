package ndfs.mcndfs_1_naive;

import java.io.File;
import java.io.FileNotFoundException;

import graph.Graph;
import graph.GraphFactory;
import graph.State;
import ndfs.CycleFoundException;
import ndfs.NDFS;
import ndfs.NoCycleFoundException;
import ndfs.ResultException;

/**
 * This is a straightforward implementation of Figure 1 of
 * <a href="http://www.cs.vu.nl/~tcs/cm/ndfs/laarman.pdf">
 * "the Laarman paper"</a>.
 *
 * This class should be modified/extended to implement Figure 2 of this paper.
 */
public class NNDFS implements NDFS {

    private final Colors colors = new Colors();
    private final File promelaFile;
    private final int numberOfWorkers;
    private final ResultArray resultArray;

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
        this.resultArray = new ResultArray(this.numberOfWorkers);
    }

    @Override
    public void ndfs() throws ResultException {
        Worker[] workers = new Worker[numberOfWorkers];
        for(int i = 0; i < numberOfWorkers; i++) {
            try {
                workers[i] = new Worker(promelaFile, colors, i, resultArray);
                workers[i].start();
            } catch (FileNotFoundException file) {
                System.out.println(file);
                file.printStackTrace();
                System.exit(1);
            }
        }
        //wait for threads to die
        while (!resultArray.hasCycle() && !resultArray.allFilled());
        //all threads finished or cycle is found
        if( resultArray.hasCycle()) {
            throw new CycleFoundException();
        } else {
            throw new NoCycleFoundException();
        }
    }
}
