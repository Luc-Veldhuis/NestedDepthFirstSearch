package ndfs.mcndfs_1_improved;

/**
 * Created by Luc on 18-9-2015.
 */
public class ResultArray {
    private Result[] resultArray;

    public ResultArray(int length){
        resultArray = new Result[length];
    }

    public Result get(int index) {
        return resultArray[index];
    }

    public void set(Result result, int index) {
        resultArray[index] = result;
    }

    public boolean allFilled() {
        for(Result r: resultArray) {
            if (r == null){
                return false;
            }
        }
        return true;
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
