package dip_eva.image_processing;

import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class ImageHistogram {

    private Image image;

    private long alpha[] = new long[256];
    private long red[] = new long[256];
    private long green[] = new long[256];
    private long blue[] = new long[256];

    XYChart.Series<String,Number> seriesAlpha;
    XYChart.Series<String,Number> seriesRed;
    XYChart.Series<String,Number> seriesGreen;
    XYChart.Series<String,Number> seriesBlue;

    private boolean success;

    public ImageHistogram(Image src) {
        image = src;
        success = false;

        for (int i = 0; i < 256; i++) {
            alpha[i] = red[i] = green[i] = blue[i] = 0;
        }

        PixelReader pixelReader = image.getPixelReader();
        if (pixelReader == null) {
            return;
        }

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int argb = pixelReader.getArgb(x, y);
                int a = (0xff & (argb >> 24));
                int r = (0xff & (argb >> 16));
                int g = (0xff & (argb >> 8));
                int b = (0xff & argb);

                alpha[a]++;
                red[r]++;
                green[g]++;
                blue[b]++;
            }
        }

        seriesAlpha = new XYChart.Series<String,Number>();
        seriesRed = new XYChart.Series<String,Number>();
        seriesGreen = new XYChart.Series<String,Number>();
        seriesBlue = new XYChart.Series<String,Number>();
        seriesAlpha.setName("alpha");
        seriesRed.setName("red");
        seriesGreen.setName("green");
        seriesBlue.setName("blue");

        for (int i = 0; i < 256; i++) {
            seriesAlpha.getData().add(new XYChart.Data<String,Number>(String.valueOf(i), alpha[i]));
            seriesRed.getData().add(new XYChart.Data<String,Number>(String.valueOf(i), red[i]));
            seriesGreen.getData().add(new XYChart.Data<String,Number>(String.valueOf(i), green[i]));
            seriesBlue.getData().add(new XYChart.Data<String,Number>(String.valueOf(i), blue[i]));
        }

        success = true;
    }

    public boolean isSuccess() {
        return  success;
    }

    public XYChart.Series getSeriesAlpha() {

        return seriesAlpha;
    }

    public XYChart.Series getSeriesRed() {
        return seriesRed;
    }

    public XYChart.Series getSeriesGreen() {

        return seriesGreen;
    }

    public XYChart.Series getSeriesBlue() {

        return seriesBlue;
    }
}
