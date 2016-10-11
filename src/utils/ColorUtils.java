package utils;

import java.awt.*;

public class ColorUtils {

    public static Color lerp(Color a, Color a1, float w) {
        int r = (int) MathUtils.lerp(a.getRed(), a1.getRed(), w);
        int g = (int) MathUtils.lerp(a.getGreen(), a1.getGreen(), w);
        int b = (int) MathUtils.lerp(a.getBlue(), a1.getBlue(), w);
        return new Color(r, g, b);
    }
}
