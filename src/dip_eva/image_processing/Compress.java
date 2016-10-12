package dip_eva.image_processing;

import dip_eva.helper.FileUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

public class Compress {
    private java.lang.Class context;
    private BufferedImage image;
    public static String TMP_FIFLE = System.getProperty("java.io.tmpdir");

    public Compress(String  input, double quality) throws IOException {

        this.image = ImageIO.read(new File(input));

        File compressImageFile = FileUtils.getTempFile();

        if (compressImageFile.canWrite()) {

            OutputStream os = new FileOutputStream(compressImageFile);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality((float) quality);
            writer.write(null, new IIOImage(image, null, null), param);

            os.close();
            ios.close();
            writer.dispose();

            image = ImageIO.read(FileUtils.getTempFile());
        } else {
            throw new IOException("Can write file: " + compressImageFile.getAbsolutePath());
        }

    }

    public BufferedImage getImage() throws IOException {
        return image;
    }
}
