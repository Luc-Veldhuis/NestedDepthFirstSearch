package ndfs.mcndfs_1_naive;

import graph.Graph;
import graph.GraphFactory;
import graph.State;
import ndfs.CycleFoundException;
import ndfs.NoCycleFoundException;
import ndfs.ResultException;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.File;
import java.io.FileNotFoundException;

public class Worker implements Runnable {

	
    private final Graph graph;
    private final Colors colors;
    private static AtomicInteger redStateCounter;
    private Colors pink; //ik gebruik maar Colors ipv nieuwe map met binaries
    private int threadId;
    private ResultArray resultArray;
    public Thread thread;
    
    
    public Worker(File promelaFile, Colors colors, int threadId, ResultArray resultArray) throws FileNotFoundException {
        this.graph = GraphFactory.createGraph(promelaFile);
        this.colors = colors;
        this.threadId = threadId;
        this.resultArray = resultArray;
        redStateCounter = new AtomicInteger();
    }

    private void dfsRed(State s) throws ResultException {
    	pink.color(s, Color.PINK);
        for (State t : graph.post(s)) {
            if (colors.hasColor(t, Color.CYAN)) {
                throw new CycleFoundException();
            } else if (!colors.hasColor(t, Color.RED) && !pink.hasColor(t,Color.PINK)) {
                dfsRed(t);
            }
        }
        if(s.isAccepting()){
        	redStateCounter.getAndDecrement();
        	while (redStateCounter.get() != 0){}
        }
        colors.color(s,Color.RED);
        pink.color(s, null); //mogelijk een probleem als compare methode niet met null om kan gaan, white / alternatieve kleur gebruiken?
    }
    
    private void dfsBlue(State s) throws ResultException {
        colors.color(s, Color.CYAN);
        for (State t : graph.post(s)) {
            if (colors.hasColor(t, Color.WHITE) && !colors.hasColor(t,Color.RED)) {
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
