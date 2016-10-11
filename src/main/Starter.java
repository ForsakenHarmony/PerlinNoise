package main;

import graphics.NoiseCanvas;
import terrainGeneration.MapGenerator;
import utils.Console;

import javax.swing.*;


public class Starter {

    public Starter() {
        byte testByte = Byte.MAX_VALUE;
        testByte++;

        Console.log(testByte);

        JFrame.setDefaultLookAndFeelDecorated(false);

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        NoiseCanvas canvas = new NoiseCanvas();

        frame.setLayout(null);
        panel.setLayout(null);

        frame.setBounds(20, 20, 900 + 41, 900 + 41);
        frame.setLocationRelativeTo(null);
        panel.setBounds(1, 1, frame.getWidth() - 41, frame.getHeight() - 41);
        canvas.setBounds(0, 0, panel.getWidth(), panel.getHeight());

        panel.add(canvas);
        frame.add(panel);
        panel.setVisible(true);

//        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        MapGenerator mapGenerator = new MapGenerator(canvas);
        mapGenerator.generateMap();
    }


    public static void main(String[] args) {
        new Starter();
    }

}
