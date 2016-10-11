package utils;

public class MathUtils {

    public static int fastFloor(float x) {
        int xi = (int) x;
        return x < xi ? xi - 1 : xi;
    }

    public static float lerp(float a, float b, float w) {
        return (1.0f - w) * a + w * b;
    }

    public static float inverseLerp(float a, float b, float value) {
        return (a - value) / (a - b);
    }

    public static float pow(float b, int e) {
        if (b < 0 || e < 0) {
            return Float.NaN;
        } else if (b == 0) {
            return 0;
        } else if (e == 0) {
            return 1;
        } else {
            float total = b;
            for (int i = 0; i < e; i++) {
                total *= b;
            }
            return total;
        }
    }
}
