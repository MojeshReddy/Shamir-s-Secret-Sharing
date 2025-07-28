import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;

public class ShamirSecretSharingAssignment {

    public static void main(String[] args) throws Exception {
        JSONObject testCase1 = new JSONObject(new String(Files.readAllBytes(Paths.get("testcase1.json"))));
        JSONObject testCase2 = new JSONObject(new String(Files.readAllBytes(Paths.get("testcase2.json"))));
        System.out.println(findConstantTerm(testCase1));
        System.out.println(findConstantTerm(testCase2));
    }
    private static BigInteger findConstantTerm(JSONObject json) {
        JSONObject keys = json.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");
        List<BigInteger> xVals = new ArrayList<>();
        List<BigInteger> yVals = new ArrayList<>();
        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            
            int x = Integer.parseInt(key);
            JSONObject root = json.getJSONObject(key);
            int base = Integer.parseInt(root.getString("base"));
            String valStr = root.getString("value");

            BigInteger y = new BigInteger(valStr, base);

            xVals.add(BigInteger.valueOf(x));
            yVals.add(y);
        }
        List<BigInteger> xSubset = xVals.subList(0, k);
        List<BigInteger> ySubset = yVals.subList(0, k);

        return lagrangeInterpolationAtZero(xSubset, ySubset);
    }
    private static BigInteger lagrangeInterpolationAtZero(List<BigInteger> x, List<BigInteger> y) {
        int k = x.size();
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    numerator = numerator.multiply(x.get(j));
                    denominator = denominator.multiply(x.get(j).subtract(x.get(i)));
                }
            }
            
            BigInteger term = y.get(i).multiply(numerator);
            term = term.divide(denominator);
            result = result.add(term);
        }
        return result;
    }
}
