package dip_eva.image_processing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class HSV {
    BufferedImage image;
    public HSV(BufferedImage input) {
        try {

            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            BufferedImage image = input;
            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            mat.put(0, 0, data);

            Mat mat1 =  new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2HSV);

            byte[] data1 = new byte[mat1.rows()*mat1.cols()*(int)(mat1.elemSize())];
            mat1.get(0, 0, data1);

            this.image = new BufferedImage(mat1.cols(), mat1.rows(), 5);
            this.image.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public BufferedImage getImage() {
        return image;
    }
}
