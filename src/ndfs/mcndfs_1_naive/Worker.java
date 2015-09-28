package ndfs.mcndfs_1_naive;

import graph.Graph;
import graph.GraphFactory;
import graph.State;
import ndfs.CycleFoundException;
import ndfs.NoCycleFoundException;
import ndfs.ResultException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.File;
import java.io.FileNotFoundException;

public class Worker implements Runnable {

	
    private final Graph graph;
    private final Colors colors;
    private static AtomicInteger redStateCounter;
    private final Colors pink;
    private final Colors red;
    private int threadId;
    private ResultArray resultArray;
    public Thread thread;
    
    
    public Worker(File promelaFile, int threadId, Colors red, ResultArray resultArray) throws FileNotFoundException {
        this.graph = GraphFactory.createGraph(promelaFile);
        this.red = red;
        pink = new Colors();
        colors = new Colors();
        this.threadId = threadId;
        this.resultArray = resultArray;
        redStateCounter = new AtomicInteger();
    }

    private void dfsRed(State s) throws ResultException {
    	pink.color(s, Color.PINK);
        List graphList = graph.post(s);
        for (int i = 0; i < graphList.size(); i++) {
            State t = (State) graphList.get((i+threadId) % graphList.size());
            if (colors.hasColor(t, Color.CYAN)) {
                throw new CycleFoundException();
            } else if (!pink.hasColor(t, Color.PINK) && !red.hasColor(t, Color.RED)) {
                dfsRed(t);
            }
        }
        if(s.isAccepting()){
        	redStateCounter.getAndDecrement();
        	while (redStateCounter.get() != 0){}
        }

        red.color(s, Color.RED);

        pink.color(s, Color.WHITE);
    }
    
    private void dfsBlue(State s) throws ResultException {
        colors.color(s, Color.CYAN);
        for (State t : graph.post(s)) {
            if (colors.hasColor(t, Color.WHITE) && !red.hasColor(t,Color.RED)) {
                dfsBlue(t);
            }
        }
        if (s.isAccepting()) {
        	redStateCounter.getAndIncrement();
        	dfsRed(s);
        }
        colors.color(s, Color.BLUE);
    }

    private void nndfs(State s) throws ResultException {
        dfsBlue(s);
        throw new NoCycleFoundException();
    }

    @Override
    public void run() {
        State s = graph.getInitialState();
        try {
            nndfs(s);
        } catch (ResultException result) {
            if( result instanceof NoCycleFoundException) {
                resultArray.set(Result.NOCYCLE, threadId);
            }
            else if (result instanceof  CycleFoundException) {
                resultArray.set(Result.CYCLE, threadId);
            }
            else{
                resultArray.set(Result.ERROR, threadId);
            }
        }

    }

    public void start() throws ResultException {
        if (thread == null)
        {
            thread = new Thread (this, Integer.toString(threadId));
            thread.start();
        }

    }
}
