package ndfs.mcndfs_1_improved;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Luc on 18-9-2015.
 */
public class ResultTracker {
    private AtomicBoolean cycleFound;
    private AtomicInteger finished;
    private int length;
    private boolean crached;

    public ResultTracker(int length){
        finished = new AtomicInteger(0);
        cycleFound = new AtomicBoolean(false);
        this.length = length;
        crached = false;
    }

    public void noCycle() {
        finished.getAndIncrement();
    }

    public void cycleFound() {
        cycleFound.compareAndSet(false, true);
        finished.getAndIncrement();
    }

    public boolean hasCycle() {
        return cycleFound.get();
    }

    public boolean allFilled() {
        return finished.get() == length;
    }

    public void setCrached() {
        crached = true;
    }

    public boolean crashed() {
        return crached;
    }

    @Override
    public String toString() {
        String resultText = "";
        resultText += finished.get();
        return resultText;
    }
}
