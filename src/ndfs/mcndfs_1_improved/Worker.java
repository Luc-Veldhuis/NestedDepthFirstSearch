package ndfs.mcndfs_1_improved;

import graph.Graph;
import graph.GraphFactory;
import graph.State;
import ndfs.CycleFoundException;
import ndfs.NoCycleFoundException;
import ndfs.ResultException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable {

	
    private final Graph graph;
    private final Colors colors;
    private static AtomicInteger redStateCounter;
    private final Colors pink;
    private final Colors red;
    private int threadId;
    private int numberOfWorkers;
    private ResultTracker tracker;
    private Object main;
    public Thread thread;


    public Worker(File promelaFile, int threadId, Colors red, ResultTracker tracker, Object main, int numberOfWorkers) throws FileNotFoundException {
        this.graph = GraphFactory.createGraph(promelaFile);
        this.red = red;
        pink = new Colors();
        colors = new Colors();
        this.threadId = threadId;
        this.tracker = tracker;
        this.main = main;
        this.numberOfWorkers = numberOfWorkers;
        redStateCounter = new AtomicInteger();
    }

    private void dfsRed(State s) throws Exception {
    	pink.color(s, Color.PINK);
        for (State t: graph.post(s)) {
            if (colors.hasColor(t, Color.CYAN)) {
                throw new CycleFoundException();
            } else if (!pink.hasColor(t, Color.PINK) && !red.hasColor(t, Color.RED)) {
                dfsRed(t);
            }
        }
        if(s.isAccepting()){
        	redStateCounter.getAndDecrement();
            int counter = redStateCounter.get();
        	while (counter != 0){
                if(counter < 0) {
                    throw new Exception("We crached");
                }
                if(Thread.currentThread().isInterrupted()) {
                    throw new Exception("Other threads are already done");
                }
                counter = redStateCounter.get();
            }
        }
        red.color(s, Color.RED);
        pink.color(s, Color.WHITE);
    }
    
    private void dfsBlue(State s, Splitter splitter) throws Exception {
        if(Thread.currentThread().isInterrupted()) {
            throw new Exception("Other threads are already done");
        }
        colors.color(s, Color.CYAN);
        for (State t : splitter.getStates()) {
            if (colors.hasColor(t, Color.WHITE) && !red.hasColor(t,Color.RED)) {
                dfsBlue(t, new Splitter(threadId, splitter.getStart(), splitter.getEnd(), graph, t));
            }
        }
        if (s.isAccepting()) {
        	redStateCounter.getAndIncrement();
            dfsRed(s);
        }
        colors.color(s, Color.BLUE);
    }

    private void nndfs(State s) throws Exception {
        dfsBlue(s, new Splitter(threadId, 0, numberOfWorkers, graph, s));
        throw new NoCycleFoundException();
    }

    @Override
    public void run() {
        State s = graph.getInitialState();
        try {
            nndfs(s);
        } catch (Exception result) {
            if( result instanceof NoCycleFoundException) {
                tracker.noCycle();
            }
            else if (result instanceof  CycleFoundException) {
                tracker.cycleFound();
            }
            else {
                System.out.println(result.getMessage());
                tracker.setCrached();
            }
            synchronized (main) {
                main.notifyAll();
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
