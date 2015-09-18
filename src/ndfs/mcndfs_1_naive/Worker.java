package ndfs.mcndfs_1_naive;

import graph.Graph;
import graph.GraphFactory;
import graph.State;
import ndfs.CycleFoundException;
import ndfs.NoCycleFoundException;
import ndfs.ResultException;
import spinja.Run;

import java.io.File;
import java.io.FileNotFoundException;

public class Worker implements Runnable {

    private final Graph graph;
    private final Colors colors;
    private Thread thread;
    private int threadId;

    public Worker(File promelaFile, Colors colors, int threadId) throws FileNotFoundException {
        this.graph = GraphFactory.createGraph(promelaFile);
        this.colors = colors;
        this.threadId = threadId;
    }

    private void dfsRed(State s) throws ResultException {

        for (State t : graph.post(s)) {
            if (colors.hasColor(t, Color.CYAN)) {
                throw new CycleFoundException();
            } else if (colors.hasColor(t, Color.BLUE)) {
                colors.color(t, Color.RED);
                dfsRed(t);
            }
        }
    }

    private void dfsBlue(State s) throws ResultException {

        colors.color(s, Color.CYAN);
        for (State t : graph.post(s)) {
            if (colors.hasColor(t, Color.WHITE)) {
                dfsBlue(t);
            }
        }
        if (s.isAccepting()) {
            dfsRed(s);
            colors.color(s, Color.RED);
        } else {
            colors.color(s, Color.BLUE);
        }
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
            System.out.println(result);
            System.exit(0);
        }

    }

    public void start() throws ResultException {
        if (thread == null)
        {
            thread = new Thread (this, Integer.toString(threadId));
            thread.start ();
        }

    }
}