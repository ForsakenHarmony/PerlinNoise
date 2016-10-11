package graphics;

import utils.Console;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class NoiseCanvas extends Canvas {

    private static final long serialVersionUID = 1L;

    public NoiseCanvas() {
        setIgnoreRepaint(true);
    }

    private BufferStrategy prepareBuffer() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(2);
            bs = this.getBufferStrategy();
        }
        return bs;
    }

    public void paint1D(float[] values) {
        BufferStrategy bs = prepareBuffer();
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        draw1D(g2d, values);

        g2d.dispose();
        bs.show();
    }

    public void paintMap(BufferedImage image) {
        Console.log("Drawing...");

        JFrame frame = (JFrame) this.getParent().getParent().getParent().getParent().getParent();
        frame.setVisible(true);

        BufferStrategy bs = prepareBuffer();
        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        drawMap(g2d, image);

        g2d.dispose();
        bs.show();
        Console.log("Done.");
    }

    private void draw1D(Graphics2D g2d, float[] values) {
        float scale = (this.getWidth() - 2.0f) / values.length;

        g2d.setColor(Color.WHITE);

        for (int i = 1; i < values.length; i++) {
            g2d.drawLine(
                    (int) (1 + (i - 1) * scale), (int) (1 + values[i - 1] * this.getHeight()),
                    (int) (1 + i * scale), (int) (1 + values[i] * this.getHeight())
            );
        }
    }

    private void drawMap(Graphics2D g2d, BufferedImage image) {

        float scale = (this.getWidth() - 2.0f) / image.getWidth();
        if (image.getHeight() > image.getWidth()) {
            scale = (this.getHeight() - 2.0f) / image.getHeight();
        }

        Console.log("Canvas: " + this.getWidth() + "x" + this.getHeight() + " Scaling image to: " + scale);

        g2d.drawImage(image, 1, 1, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), null);
    }
}
