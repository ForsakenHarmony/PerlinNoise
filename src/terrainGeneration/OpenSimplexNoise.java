package terrainGeneration;

/*
 * OpenSimplex Noise in Java.
 * by Kurt Spencer
 * 
 * v1.1 (October 5, 2014)
 * - Added 2D and 4D implementations.
 * - Proper gradient sets for all dimensions, from a
 *   dimensionally-generalizable scheme with an actual
 *   rhyme and reason behind it.
 * - Removed default permutation array in favor of
 *   default seed.
 * - Changed seed-based constructor to be independent
 *   of any particular randomization library, so results
 *   will be the same when ported to other languages.
 */

import utils.MathUtils;

public class OpenSimplexNoise {

    private static final float STRETCH_2D = -0.211324865405187f;    //(1/Math.sqrt(2+1)-1)/2;
    private static final float SQUISH_2D = 0.366025403784439f;      //(Math.sqrt(2+1)-1)/2;

    private static final float NORM_2D = 1.0f / 47.0f;

    private byte[] perm;
    private byte[] perm2D;

    private static float[] gradients2D = new float[]{
            5, 2, 2, 5,
            -5, 2, -2, 5,
            5, -2, 2, -5,
            -5, -2, -2, -5,
    };

    private static Contribution2[] lookup2D;

    static {
        int[][] base2D = new int[][]{
                new int[]{1, 1, 0, 1, 0, 1, 0, 0, 0},
                new int[]{1, 1, 0, 1, 0, 1, 2, 1, 1}
        };
        int[] p2D = new int[]{0, 0, 1, -1, 0, 0, -1, 1, 0, 2, 1, 1, 1, 2, 2, 0, 1, 2, 0, 2, 1, 0, 0, 0};
        int[] lookupPairs2D = new int[]{0, 1, 1, 0, 4, 1, 17, 0, 20, 2, 21, 2, 22, 5, 23, 5, 26, 4, 39, 3, 42, 4, 43, 3};

        Contribution2[] contributions2D = new Contribution2[p2D.length / 4];
        for (int i = 0; i < p2D.length; i += 4) {
            int[] baseSet = base2D[p2D[i]];
            Contribution2 previous = null, current = null;
            for (int k = 0; k < baseSet.length; k += 3) {
                current = new Contribution2(baseSet[k], baseSet[k + 1], baseSet[k + 2]);
                if (previous == null) {
                    contributions2D[i / 4] = current;
                } else {
                    previous.Next = current;
                }
                previous = current;
            }
            current.Next = new Contribution2(p2D[i + 1], p2D[i + 2], p2D[i + 3]);
        }

        lookup2D = new Contribution2[64];
        for (int i = 0; i < lookupPairs2D.length; i += 2) {
            lookup2D[lookupPairs2D[i]] = contributions2D[lookupPairs2D[i + 1]];
        }
    }

    public OpenSimplexNoise() {
        this(System.currentTimeMillis());
    }

    //Initializes the class using a permutation array generated from a 64-bit seed.
    //Generates a proper permutation (i.e. doesn't merely perform N successive pair swaps on a base array)
    //Uses a simple 64-bit LCG.
    public OpenSimplexNoise(long seed) {
        perm = new byte[256];
        perm2D = new byte[256];

        byte[] source = new byte[256];

        for (int i = 0; i < 256; i++) {
            source[i] = (byte) i;
        }
        seed = seed * 6364136223846793005L + 1442695040888963407L;
        seed = seed * 6364136223846793005L + 1442695040888963407L;
        seed = seed * 6364136223846793005L + 1442695040888963407L;
        for (int i = 255; i >= 0; i--) {
            seed = seed * 6364136223846793005L + 1442695040888963407L;

            int r = (int) ((seed + 31) % (i + 1));
            if (r < 0) {
                r += (i + 1);
            }
            perm[i] = source[r];
            perm2D[i] = perm2D[i] = (byte) (perm[i] & 0x0E);
            source[r] = source[i];
        }
    }

    //2D OpenSimplex Noise.
    public float eval(float x, float y) {

        float stretchOffset = (x + y) * STRETCH_2D;
        float xs = x + stretchOffset;
        float ys = y + stretchOffset;

        float xsb = MathUtils.fastFloor(xs);
        float ysb = MathUtils.fastFloor(ys);

        float squishOffset = (xsb + ysb) * SQUISH_2D;
        float dx0 = x - (xsb + squishOffset);
        float dy0 = y - (ysb + squishOffset);

        float xins = xs - xsb;
        float yins = ys - ysb;

        float inSum = xins + yins;

        int hash =
                (int) (xins - yins + 1) |
                        (int) (inSum) << 1 |
                        (int) (inSum + yins) << 2 |
                        (int) (inSum + xins) << 4;

        Contribution2 c = lookup2D[hash];

        float value = 0.0f;
        while (c != null) {
            float dx = dx0 + c.dx;
            float dy = dy0 + c.dy;
            float attn = 2 - dx * dx - dy * dy;
            if (attn > 0) {
                int px = (int) xsb + c.xsb;
                int py = (int) ysb + c.ysb;

                int i = perm2D[(perm[px & 0xFF] + py) & 0xFF];
                float valuePart = gradients2D[i] * dx + gradients2D[i + 1] * dy;

                attn *= attn;
                value += attn * attn * valuePart;
            }
            c = c.Next;
        }
        return value * NORM_2D / 1.732f * 2;
    }

    private static class Contribution2 {
        public float dx, dy;
        public int xsb, ysb;
        public Contribution2 Next;

        public Contribution2(float multiplier, int xsb, int ysb) {
            dx = -xsb - multiplier * SQUISH_2D;
            dy = -ysb - multiplier * SQUISH_2D;
            this.xsb = xsb;
            this.ysb = ysb;
        }
    }
}