package com.stadio.mediation.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageResizer {

    public BufferedImage cropImageSquare(byte[] image) throws IOException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // The image is already a square
        if (height == width) {
            return originalImage;
        }

        // Compute the size of the square
        int squareSize = (height > width ? width : height);

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc - (squareSize / 2), // x coordinate of the upper-left corner
                yc - (squareSize / 2), // y coordinate of the upper-left corner
                squareSize,            // widht
                squareSize             // height
        );

        return croppedImage;
    }

    public BufferedImage createResizedCopy(byte[] originalImage, int w, int h)
    {
        InputStream in = new ByteArrayInputStream(originalImage);
        try {
            BufferedImage oriImage = ImageIO.read(in);

            BufferedImage img =
                    new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            int x, y;
            int ww = oriImage.getWidth();
            int hh = oriImage.getHeight();
            int[] ys = new int[h];
            for (y = 0; y < h; y++)
                ys[y] = y * hh / h;
            for (x = 0; x < w; x++) {
                int newX = x * ww / w;
                for (y = 0; y < h; y++) {
                    int col = oriImage.getRGB(newX, ys[y]);
                    img.setRGB(x, y, col);
                }
            }
            return img;
        } catch (Exception e) {
            return null;
        }
    }


    public BufferedImage createThumbnailCopy(byte[] originalImage)
    {
        InputStream in = new ByteArrayInputStream(originalImage);
        try {
            BufferedImage oriImage = ImageIO.read(in);

            double aspectRatio = (double) oriImage.getHeight() / (double) oriImage.getWidth();

            int w = oriImage.getHeight();

            if (oriImage.getWidth() > 400) {
                w = 300;
            }
            int h = (int) ((double)w * aspectRatio);

            BufferedImage img =
                    new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            int x, y;
            int ww = oriImage.getWidth();
            int hh = oriImage.getHeight();
            int[] ys = new int[h];
            for (y = 0; y < h; y++)
                ys[y] = y * hh / h;
            for (x = 0; x < w; x++) {
                int newX = x * ww / w;
                for (y = 0; y < h; y++) {
                    int col = oriImage.getRGB(newX, ys[y]);
                    img.setRGB(x, y, col);
                }
            }
            return img;
        } catch (Exception e) {
            return null;
        }
    }

}
