package com.example.kursach88;

import com.sun.imageio.plugins.jpeg.*;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.imageio.*;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;


public class HelloController {

    private CollectionsResultBook resultBook1 = new CollectionsResultBook();

    int cntPng = 0, cntJpg = 0, cntJpg2000 = 0, sumPng = 0, sumJpg = 0, sumJpg2000 = 0;

    @FXML
    private Label welcomeText;

    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn<Result, String> colAlg;

    @FXML
    private TableColumn<Result, String> colTime;

    @FXML
    private BarChart<?, ?> ResultChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    public HelloController() throws IOException {
    }

    @FXML
    public void initialize() {
        colAlg.setCellValueFactory(new PropertyValueFactory<Result, String>("sName"));
        colTime.setCellValueFactory(new PropertyValueFactory<Result, String>("sTime"));
        choiceBox.getItems().addAll("JPEG", "JPEG2000", "PNG");
        choiceBox.setValue("JPEG");
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Успешное сжатие");

        XYChart.Series set1 = new XYChart.Series<>();

        tableView.setItems(resultBook1.getResultList());

        switch (choiceBox.getValue().toString()) {

            case "JPEG2000":
                Instant start1 = Instant.now();
                try {
                    compressImageJPG2000();
                } catch (IOException e) {
                    welcomeText.setText("Произошла ошибка");
                    e.printStackTrace();
                }
                Instant finish1 = Instant.now();
                long elapsed1 = Duration.between(start1, finish1).toMillis();
                System.out.println("JPEG2000 сжался за: " + elapsed1);
                cntJpg2000++;
                sumJpg2000 += elapsed1;
                resultBook1.add(new Result("JPEG2000", (int) elapsed1));
                set1.getData().add(new XYChart.Data("JPEG2000", (int) elapsed1));

                ResultChart.getData().addAll(set1);
                break;

            case "JPEG":
                Instant start0 = Instant.now();
                try {
                    compressImageJPG();
                } catch (IOException e) {
                    welcomeText.setText("Произошла ошибка");
                    e.printStackTrace();
                }
                Instant finish0 = Instant.now();
                long elapsed0 = Duration.between(start0, finish0).toMillis();
                System.out.println("JPEG сжался за: " + elapsed0);
                cntJpg++;
                sumJpg += elapsed0;
                resultBook1.add(new Result("JPEG", (int) elapsed0));
                set1.getData().add(new XYChart.Data("JPEG", (int) elapsed0));
                ResultChart.getData().addAll(set1);
                break;

            case "PNG":
                Instant start = Instant.now();
                try {
                    compressImagePNG();
                } catch (IOException e) {
                    welcomeText.setText("Произошла ошибка");
                    e.printStackTrace();
                }
                Instant finish = Instant.now();
                long elapsed = Duration.between(start, finish).toMillis();
                System.out.println("PNG сжался за: " + elapsed);
                cntPng++;
                sumPng += elapsed;
                resultBook1.add(new Result("PNG", (int) elapsed));
                set1.getData().add(new XYChart.Data("PNG", (int) elapsed));
                ResultChart.getData().addAll(set1);
                break;
        }
    }

    void compressImageJPG() throws IOException {
        File input = new File("TEST.jpg");
        BufferedImage image = ImageIO.read(input);

        File compressedImageFile = new File("compressed_image.jpg");
        OutputStream os = new FileOutputStream(compressedImageFile);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = (ImageWriter) writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.05f);  // Степень сжатия
        writer.write(null, new IIOImage(image, null, null), param);

        os.close();
        ios.close();
        writer.dispose();
    }

    void compressImagePNG() throws IOException {

        File input = new File("TEST.jpg");
        BufferedImage image = ImageIO.read(input);

        File compressedImageFile = new File("compressed_image.png");
        OutputStream os = new FileOutputStream(compressedImageFile);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
        ImageWriter writer = (ImageWriter) writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.5f);  // Степень сжатия
        writer.write(null, new IIOImage(image, null, null), param);

        os.close();
        ios.close();
        writer.dispose();
    }

    //ориг
    /*
    void compressImageJPG2000() throws IOException {

        File file = new File("TEST.jpg");
        BufferedImage source = ImageIO.read(file);

        BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {

                Color color = new Color(source.getRGB(x, y));

                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();

                int newRed = (int) (red * 0.942);
                int newGreen = (int) (green * 0.939);
                int newBlue = (int) (blue * 0.923);

                Color newColor = new Color(newRed, newGreen, newBlue);

                result.setRGB(x, y, newColor.getRGB());
            }
        }

        File output = new File("jpg2000Test.jpg");
        ImageIO.write(result, "jpg", output);
    }
     */

    void compressImageJPG2000() throws IOException {
        Jpg2Compress();
/*
        File file = new File("TEST.jpg");
        BufferedImage source = ImageIO.read(file);

        BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {

                Color color = new Color(source.getRGB(x, y));

                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();

                int newRed = (int) (red * 0.989);
                int newGreen = (int) (green * 0.995);
                int newBlue = (int) (blue * 0.983);

                Color newColor = new Color(newRed, newGreen, newBlue);
                result.setRGB(x, y, newColor.getRGB());
            }
        }
        File output = new File("jpg2000Test.jpg");
        ImageIO.write(result, "jpg", output);
        */ //запаска
    }


    public static void Jpg2Compress(){
        try {
            CoolImage coolImage=new CoolImage("TEST.jpg");
            coolImage.yuvConvert();
            coolImage.saveAsJpeg("TEST.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
