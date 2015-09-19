package ndfs.mcndfs_1_naive;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Luc on 18-9-2015.
 */
public class ResultArray {
    private Result[] resultArray;
    private AtomicInteger filled;

    public ResultArray(int length){
        resultArray = new Result[length];
        this.filled = new AtomicInteger(0);
    }

    public Result get(int index) {
        return resultArray[index];
    }

    public void set(Result result, int index) {
        resultArray[index] = result;
        filled.getAndIncrement();
    }

    public boolean allFilled() {
        return filled.get() == resultArray.length;
    }

    public boolean hasCycle() {
        for(Result result : resultArray) {
            if (result == Result.CYCLE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String resultText = "";
        for(Result result : resultArray) {
            resultText += result;
        }
        return resultText;
    }
}
