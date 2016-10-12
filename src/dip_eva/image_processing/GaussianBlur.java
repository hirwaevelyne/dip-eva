package dip_eva.image_processing;

import dip_eva.helper.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GaussianBlur {
    BufferedImage image;
    public GaussianBlur(String path) {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            Mat source = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_COLOR);

            Mat destination = new Mat(source.rows(), source.cols(), source.type());
            Imgproc.GaussianBlur(source, destination, new Size(45,45), 0);

            Highgui.imwrite(FileUtils.getTempFile().getAbsolutePath(), destination);

            image = ImageIO.read(FileUtils.getTempFile());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public BufferedImage getImage() {
        return image;
    }
}
