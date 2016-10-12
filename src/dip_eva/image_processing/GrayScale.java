package dip_eva.image_processing;

import java.awt.image.BufferedImage;

public class GrayScale {
    private BufferedImage image;
    int width;
    int height;

    public GrayScale(BufferedImage input) {
        try {
            image = input;
            width = image.getWidth();
            height = image.getHeight();

            for (int w=0; w<width; w++) {
                for (int h=0; h<height; h++) {
                    int rgb = image.getRGB(w, h);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb & 0xFF);

                    int grayLevel = (r + g + b) / 3;
                    int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                    image.setRGB(w, h, gray);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
