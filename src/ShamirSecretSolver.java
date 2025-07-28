import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import com.google.gson.*;

public class ShamirSecretSolver {

    public static void main(String[] args) throws Exception {
        JsonObject testCase1 = JsonParser.parseReader(new FileReader("testcase1.json")).getAsJsonObject();
        JsonObject testCase2 = JsonParser.parseReader(new FileReader("testcase2.json")).getAsJsonObject();

        BigInteger secret1 = solveSecret(testCase1);
        BigInteger secret2 = solveSecret(testCase2);

        System.out.println("Secret for Test Case 1: " + secret1);
        System.out.println("Secret for Test Case 2: " + secret2);
    }

    private static BigInteger solveSecret(JsonObject json) {
        JsonObject keys = json.getAsJsonObject("keys");
        int k = keys.get("k").getAsInt();

        List<BigInteger> xList = new ArrayList<>();
        List<BigInteger> yList = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().equals("keys")) continue;

            int x = Integer.parseInt(entry.getKey());
            JsonObject point = entry.getValue().getAsJsonObject();

            int base = Integer.parseInt(point.get("base").getAsString());
            String encoded = point.get("value").getAsString();

            BigInteger y = new BigInteger(encoded, base);
            xList.add(BigInteger.valueOf(x));
            yList.add(y);
        }

        return interpolateAtZero(xList.subList(0, k), yList.subList(0, k));
    }

    private static BigInteger interpolateAtZero(List<BigInteger> x, List<BigInteger> y) {
        BigInteger result = BigInteger.ZERO;
        int k = x.size();

        for (int i = 0; i < k; i++) {
            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                num = num.multiply(x.get(j));
                den = den.multiply(x.get(j).subtract(x.get(i)));
            }

            BigInteger term = y.get(i).multiply(num).divide(den);
            result = result.add(term);
        }

        return result;
    }
}
