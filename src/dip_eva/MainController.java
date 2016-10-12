package dip_eva;

import dip_eva.helper.FileUtils;
import dip_eva.image_processing.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    Slider hue;
    @FXML
    Slider saturation;
    @FXML
    Slider brightness;
    @FXML
    Slider contrast;
    @FXML
    ImageView imageView;
    @FXML
    LineChart<String, Number> chartHistogram;

    BufferedImage image;
    Image fxImage;
    FileChooser fc;
    ColorAdjust colorAdjust;
    File input;

    public static String DEFAULT_TITLE = "DIP - Evelyne / Project 1.0";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setContrast();
        setHue();
        setBrightness();
        setSaturation();

        colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(0);
        colorAdjust.setBrightness(0);
        colorAdjust.setHue(0);
        colorAdjust.setSaturation(0);

        chartHistogram.setCreateSymbols(false);

        fc = new FileChooser();
        fc.setTitle(DEFAULT_TITLE);
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png")
        );
    }

    // Menu File Action
    public void newAction() throws IOException {
        fc.setTitle("Open");
        input = fc.showOpenDialog(null);

        if (input != null) {
            image = ImageIO.read(input);
            updateView();
        }
    }

    public void saveAction() {
        fc.setTitle("Save");
        File output = fc.showSaveDialog(null);
        image = SwingFXUtils.fromFXImage(imageView.snapshot(null, null), null);

        if (output != null) {

            try {
                ImageIO.write(image, "png", output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void exitAction() {
        System.exit(0);
    }

    // Edit Menu Action
    public void backToOriginalAction() throws IOException {
        image = ImageIO.read(input);
        updateView();
    }

    // Tool Menu Action
    public void grayscaleConversionAction() {
        GrayScale grayscale = new GrayScale(image);
        image = grayscale.getImage();
        updateView();
    }

    public void hsvConversionAction() {
        HSV hsv = new HSV(image);
        image = hsv.getImage();
        updateView();
    }

    public void compressAction() {
        //final double imgQuality;
        Dialog dialog = new Dialog();
        dialog.setTitle("Compress Image");
        dialog.setHeaderText("Set image quality");

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

        ButtonType btnCompress = new ButtonType("Compress", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnCompress, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(1);
        slider.setMajorTickUnit(0.2);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setValue(1);
        slider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                compress(slider.getValue());
                updateView();
            }
        });

        grid.add(new Label("Image quality"), 0, 0);
        grid.add(slider, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnCompress) {
                return slider.getValue();
            }
            return null;
        });

        Optional result = dialog.showAndWait();



        result.ifPresent(compress -> { compress(slider.getValue()); updateView(); });
    }

    public void segmentImageAction() throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat source = Highgui.imread(input.getAbsolutePath(),
                Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        destination = source;

        Imgproc.threshold(source,destination,127,255,Imgproc.THRESH_TOZERO);

        Highgui.imwrite(FileUtils.getTempFile().getAbsolutePath(), destination);

        image = ImageIO.read(FileUtils.getTempFile());
        updateView();
    }

    // Enhancing Image Action
    public void setContrast() {
        contrast.prefWidth(300);
        contrast.setMin(-1);
        contrast.setMax(1);
        contrast.setMajorTickUnit(0.2);
        contrast.setShowTickMarks(true);
        contrast.setShowTickLabels(true);
        contrast.setValue(0);
        contrast.setTooltip(new Tooltip("Contrast"));
        contrast.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                colorAdjust.setContrast(contrast.getValue());
                updateView();
            }
        });
    }

    public void setHue() {
        hue.prefWidth(300);
        hue.setMin(-1);
        hue.setMax(1);
        hue.setMajorTickUnit(0.2);
        hue.setShowTickMarks(true);
        hue.setShowTickLabels(true);
        hue.setValue(0);
        hue.setTooltip(new Tooltip("Hue"));
        hue.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                colorAdjust.setHue(hue.getValue());
                updateView();
            }
        });
    }

    public void setBrightness() {
        brightness.prefWidth(300);
        brightness.setMin(-1);
        brightness.setMax(1);
        brightness.setMajorTickUnit(0.2);
        brightness.setShowTickMarks(true);
        brightness.setShowTickLabels(true);
        brightness.setValue(0);
        brightness.setTooltip(new Tooltip("Brightness"));
        brightness.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                colorAdjust.setBrightness(brightness.getValue());
                updateView();
            }
        });
    }

    public void setSaturation() {
        saturation.prefWidth(300);
        saturation.setMin(-1);
        saturation.setMax(1);
        saturation.setMajorTickUnit(0.2);
        saturation.setShowTickMarks(true);
        saturation.setShowTickLabels(true);
        saturation.setValue(0);
        saturation.setTooltip(new Tooltip("Saturation"));
        saturation.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                colorAdjust.setSaturation(saturation.getValue());
                updateView();
            }
        });
    }

    // Menu Filter Action
    public void gaussianAction() {
        GaussianBlur gaussianBlur = new GaussianBlur(input.getAbsolutePath());
        image = gaussianBlur.getImage();
        updateView();
    }

    public void weightedAverageAction() {
        WeightedAverageFilter filter = new WeightedAverageFilter(input.getAbsolutePath());
        image = filter.getImage();
        updateView();
    }

    public void morphErodeAction() throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat source = Highgui.imread(input.getAbsolutePath(),
                Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        destination = source;

        int erosion_size = 5;

        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * erosion_size +1, 2 * erosion_size +1));
        Imgproc.erode(source, destination, element);

        Highgui.imwrite(FileUtils.getTempFile().getAbsolutePath(), destination);

        image = ImageIO.read(FileUtils.getTempFile());
        updateView();
    }

    public void  morphDilateAction() throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat source = Highgui.imread(input.getAbsolutePath(),
                Highgui.CV_LOAD_IMAGE_COLOR);
        Mat destination = new Mat(source.rows(), source.cols(), source.type());
        destination = source;

        int dilatation_size = 5;

        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * dilatation_size +1, 2 * dilatation_size +1));
        Imgproc.dilate(source, destination, element);

        Highgui.imwrite(FileUtils.getTempFile().getAbsolutePath(), destination);

        image = ImageIO.read(FileUtils.getTempFile());
        updateView();
    }

    public void updateView() {
        fxImage = SwingFXUtils.toFXImage(image, null);
        imageView.setImage(fxImage);
        imageView.setEffect(colorAdjust);
        chartHistogram.getData().clear();

        ImageHistogram imageHistogram = new ImageHistogram(fxImage);
        if (imageHistogram.isSuccess()) {
            chartHistogram.getData().addAll(
                    imageHistogram.getSeriesAlpha(),
                    imageHistogram.getSeriesRed(),
                    imageHistogram.getSeriesGreen(),
                    imageHistogram.getSeriesBlue());

        }
    }

    public void compress(double quality) {

        try {
            Compress compress = new Compress(input.getAbsolutePath(), quality);
            image = compress.getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
