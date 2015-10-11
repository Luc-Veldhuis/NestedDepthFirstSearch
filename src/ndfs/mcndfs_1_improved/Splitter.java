package ndfs.mcndfs_1_improved;

import graph.Graph;
import graph.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luc on 9-10-2015.
 *
 * This Class is responsible for dividing child nodes evenly over the threads.
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

    private List<Integer> generateList(int blockSize) {
        List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < postStates.size(); i++) {
            list.add(blockSize);
        }
        return list;
    }

    private List<Integer> generateBlockList(int blockSize) {
        List<Integer> blockList;
        if(blockSize*postStates.size() > (end-start) && blockSize > 1 && postStates.size() > 1) {
            blockList = generateList(blockSize);
            int numberOfSmallerValues = blockSize*postStates.size() -(end-start);
            for(int i = blockList.size()-1; i >= blockList.size()-numberOfSmallerValues; i--) {
                blockList.set(i, blockList.get(i) -1);
            }
        } else{
            blockList = generateList(blockSize);
        }
        return blockList;
    }

    private int getStartingIndex(List<Integer> blockList) {
        int range = 0;
        int startingIndex = 0;
        for(int blockIndex = 0; blockIndex < blockList.size(); blockIndex++) {
            range += blockList.get(blockIndex);
            if(threadId-start < range) {
                startingIndex = blockIndex;
                break;
            }
        }
        return startingIndex;
    }

    public List<State> getStates() {

        List<State> resultStates = new ArrayList<State>();
        int blockSize = (int) Math.ceil((end-start)/(double)postStates.size());
        if (blockSize <= 0 || postStates.size() <= 0) return resultStates;
        List<Integer> blockList = generateBlockList(blockSize);
        int startingIndex = getStartingIndex(blockList);
        for(int i = 0; i < postStates.size(); i++) {
            resultStates.add(postStates.get((startingIndex+i)% postStates.size()));
        }
        return resultStates;
    }

    public int getStart() {
        int blockSize = (int) Math.ceil((end-start)/(double)postStates.size());
        if (blockSize <= 0) return start;
        List<Integer> blockList = generateBlockList(blockSize);
        int startingIndex = getStartingIndex(blockList);
        int offset = 0;
        for(int i = 0; i < startingIndex; i++) {
            offset += blockList.get(i);
        }
        return start + offset;
    }

    public int getEnd() {
        int blockSize = (int) Math.ceil((end-start)/(double)postStates.size());
        if (blockSize <= 0) return end;
        List<Integer> blockList = generateBlockList(blockSize);
        int startingIndex = getStartingIndex(blockList);
        int offset = 0;
        for(int i = 0; i < startingIndex+1; i++) {
            offset += blockList.get(i);
        }
        return start + offset;
    }
}
