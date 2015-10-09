package ndfs.mcndfs_1_improved;

import graph.Graph;
import graph.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luc on 9-10-2015.
 */
public class Splitter {
    public int start;
    public int end;
    public final Graph graph;
    public int threadId;
    public State state;
    List<State> postStates;

    Splitter(int threadId, int start, int end, Graph graph, State s) {
        this.start = start;
        this.end = end;
        this.graph = graph;
        state = s;
        this.threadId = threadId;
        postStates = graph.post(state);
    }

    public List<State> getStates() {

        List<State> resultStates = new ArrayList<State>();
        int blockSize = (int) Math.ceil((end-start)/(double)postStates.size());
        if (blockSize <= 0 || postStates.size() <= 0) return resultStates;
        int startingIndex = (threadId/blockSize) % postStates.size();
        for(int i = 0; i < postStates.size(); i++) {
            resultStates.add(postStates.get((startingIndex+i)% postStates.size()));
        }
        return resultStates;
    }

    public int getStart() {
        int blockSize = (int) Math.ceil((end-start)/(double)postStates.size());
        if (blockSize <= 0) return start;
        int startingIndex = (threadId/blockSize) % postStates.size();
        return start + startingIndex*blockSize;
    }

    public int getEnd() {
        int blockSize = (int) Math.ceil((end-start)/(double)postStates.size());
        if (blockSize <= 0) return end;
        int startingIndex = (threadId/blockSize) % postStates.size();
        return start + (startingIndex + 1)*blockSize;
    }
}
