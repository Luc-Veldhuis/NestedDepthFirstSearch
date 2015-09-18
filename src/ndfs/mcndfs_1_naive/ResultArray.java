package ndfs.mcndfs_1_naive;

/**
 * Created by Luc on 18-9-2015.
 */
public class ResultArray {
    private volatile Result[] resultArray;
    private int filled;

    public ResultArray(int length){
        resultArray = new Result[length];
        this.filled = 0;
    }

    public Result get(int index) {
        return resultArray[index];
    }

    public void set(Result result, int index) {
        resultArray[index] = result;
        filled++;
    }

    public boolean allFilled() {
        return filled == resultArray.length;
    }

    public boolean hasCycle() {
        for(Result result : resultArray) {
            if (result == Result.CYCLE) {
                return true;
            }
        }
        return false;
    }
}
