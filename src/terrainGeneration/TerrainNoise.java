package terrainGeneration;

import utils.Console;
import utils.ConsoleUtils;
import utils.MathUtils;

import java.util.Random;

public class TerrainNoise {

    public static float[][] generateNoiseMap(int mapWidth, int mapHeight, int seed, float scale, int octaves, float persistance, float lacunarity, Vector offset) {
        Console.log("Generating NoiseMap [ " + mapWidth + " | " + mapHeight + " ] Scale: " + scale);
        Console.log("Octaves: " + octaves + " ");

        float[][] noiseMap = new float[mapWidth][mapHeight];

        OpenSimplexNoise noise = new OpenSimplexNoise(seed);

        Random prng = new Random(seed);
        Vector[] octaveOffsets = new Vector[octaves];

        for (int i = 0; i < octaves; i++) {
            float offsetX = prng.nextInt(200001) - 100000 + offset.get(0);
            float offsetY = prng.nextInt(200001) - 100000 + offset.get(1);
            octaveOffsets[i] = new Vector(offsetX, offsetY);
        }

//        scale = scale * (mapWidth / 100 - 0f);


        if (scale <= 0) {
            scale = 0.0001f;
        }

        float maxNoiseHeight = Float.MIN_VALUE;
        float minNoiseHeight = Float.MAX_VALUE;

        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;

        float halfWidth = mapWidth / 2f;
        float halfHeight = mapHeight / 2f;

        Console.log("Generating");

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {

                float amplitude = 1;
                float frequency = 1;

                float noiseHeight = 0;

                for (int i = 0; i < octaves; i++) {
                    float sampleX = ((x - halfWidth) / scale * frequency) + octaveOffsets[i].get(0);
                    float sampleY = ((y - halfHeight) / scale * frequency) + octaveOffsets[i].get(1);

                    float perlinValue = noise.eval(sampleX, sampleY);
//                    perlinValue = perlinValue / 1.732f * 2;

                    if (perlinValue > max) {
                        max = perlinValue;
                    } else if (perlinValue < min) {
                        min = perlinValue;
                    }

                    noiseHeight += perlinValue * amplitude;

                    amplitude *= persistance;
                    frequency *= lacunarity;
                }

                if (noiseHeight > maxNoiseHeight) {
                    maxNoiseHeight = noiseHeight;
                } else if (noiseHeight < minNoiseHeight) {
                    minNoiseHeight = noiseHeight;
                }

                noiseMap[x][y] = noiseHeight;
            }

            ConsoleUtils.printProgress(Math.round((1.0f * y / mapHeight) * 100));
        }
        System.out.println();

        Console.log("Min: " + minNoiseHeight);
        Console.log("Max: " + maxNoiseHeight);

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                noiseMap[x][y] = MathUtils.inverseLerp(minNoiseHeight, maxNoiseHeight, noiseMap[x][y]);
            }
        }

        Console.log("    Done");

        return noiseMap;
    }
}
