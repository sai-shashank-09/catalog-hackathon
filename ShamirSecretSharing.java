import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class ShamirSecretSharing {

    public static void main(String[] args) throws Exception {
        // Parse the JSON input from the file
        String jsonContent1 = new String(Files.readAllBytes(Paths.get("testcase1.json")));
        String jsonContent2 = new String(Files.readAllBytes(Paths.get("testcase2.json")));

        // Process Test Case 1
        System.out.println("Processing Test Case 1...");
        processTestCase(jsonContent1);

        // Process Test Case 2
        System.out.println("Processing Test Case 2...");
        processTestCase(jsonContent2);
    }

    // Function to process each test case
    public static void processTestCase(String jsonContent) {
        JSONObject json = new JSONObject(jsonContent);

        // Extract the number of points (n) and minimum required (k)
        JSONObject keys = json.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Parse the points (x, y) pairs
        List<Point> points = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (json.has(String.valueOf(i))) {
                JSONObject point = json.getJSONObject(String.valueOf(i));
                int x = i;
                int base = Integer.parseInt(point.getString("base"));
                String value = point.getString("value");
                BigInteger y = new BigInteger(value, base); // Convert Y value to BigInteger based on base
                points.add(new Point(x, y));
            }
        }

        // Print parsed points
        System.out.println("Parsed points for Test Case: " + points);

        // Apply Lagrange interpolation to get the constant term (secret)
        BigInteger secret = calculateLagrangeInterpolation(points, k);
        System.out.println("Secret for Test Case: " + secret);

        // Check for imposter points (only in Test Case 2 where n > k)
        if (n > k) {
            detectImposterPoints(points, secret, k);
        }
    }

    // Function to apply Lagrange interpolation and find the constant term
    public static BigInteger calculateLagrangeInterpolation(List<Point> points, int k) {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < k; i++) {
            BigInteger term = points.get(i).y;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xi = BigInteger.valueOf(points.get(i).x);
                    BigInteger xj = BigInteger.valueOf(points.get(j).x);
                    term = term.multiply(xj).divide(xj.subtract(xi));
                }
            }
            result = result.add(term);
        }
        return result.mod(BigInteger.TEN.pow(12));  // Modulo for large number handling
    }

    // Function to detect imposter points (up to 3 wrong points)
    public static void detectImposterPoints(List<Point> points, BigInteger secret, int k) {
        System.out.println("Detecting imposter points...");
        int imposterCount = 0;
        for (Point p : points) {
            if (!isPointOnCurve(p, secret, k)) {
                System.out.println("Imposter Point Detected: x = " + p.x + ", y = " + p.y);
                imposterCount++;
            }
            if (imposterCount >= 3) {
                break;
            }
        }
        if (imposterCount == 0) {
            System.out.println("No Imposter Points Detected");
        }
    }

    // Function to check if a point lies on the curve
    public static boolean isPointOnCurve(Point p, BigInteger secret, int k) {
        // Here, you would check if the point (p.x, p.y) satisfies the polynomial equation.
        // For simplicity, we assume that Lagrange interpolation produced the right constant term (secret),
        // so you can use this to verify the curve.
        // This is a placeholder and would need further development.
        return true; // Placeholder logic
    }

    // Helper class for representing points (x, y)
    static class Point {
        int x;
        BigInteger y;

        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
