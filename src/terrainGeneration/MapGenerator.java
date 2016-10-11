package terrainGeneration;

import graphics.NoiseCanvas;
import utils.ColorUtils;
import utils.Console;
import utils.ConsoleUtils;
import utils.MathUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapGenerator {

    private static final int SEED = 42;
    public final Vector offset = new Vector(0, 0);

    public final String fileName = "out.1.0.png";

    public final int mapWidth = 1024;
    public final int mapHeight = 1024;

    public final int octaveCount = 5;
    public final float persistance = 0.5f;
    public final float lacunarity = 2f;

    public final float noiseScale = 1024f;

    private NoiseCanvas canvas;

    public MapGenerator(NoiseCanvas canvas) {
        this.canvas = canvas;
    }

    public void generateMap() {
        float[][] noiseMap = TerrainNoise.generateNoiseMap(mapWidth, mapHeight, SEED, noiseScale, octaveCount, persistance, lacunarity, offset);

        Console.log("Creating Image");

        BufferedImage im = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < mapWidth; x++) {
            Color color;
            for (int y = 0; y < mapHeight; y++) {
                int n = (int) MathUtils.lerp(0.0f, 100f, noiseMap[x][y]);
                color = ColorUtils.lerp(Color.black, Color.green, noiseMap[x][y]);

//                if (n < 20) {
//                    color = Color.blue.darker();
//                } else if (n < 30) {
//                    color = Color.blue;
//                } else if (n < 45) {
//                    color = Color.blue.brighter();
//                } else if (n < 51) {
//                    color = Color.yellow;
//                } else if (n < 62) {
//                    color = Color.green.darker();
//                } else if (n < 79) {
//                    color = Color.green.darker().darker();
//                } else if (n < 92) {
//                    color = Color.gray;
//                } else {
//                    color = Color.white;
//                }

                im.setRGB(x, y, color.getRGB());
            }
            ConsoleUtils.printProgress((int) ((1.0f * x / mapWidth) * 100.0f));
        }

        System.out.println();

        Console.log("    Done");

        new Thread(() -> {
            Console.log("Writing to File: " + fileName);
            try {
                ImageIO.write(im, "PNG", new File(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Console.log("Done");
        }).start();

        canvas.paintMap(im);
    }

}
