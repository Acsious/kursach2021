package com.example.kursach88;

import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class CoolImage {
        private  int y,u,v;
        private int height;             // высота изображения
        private int width;              // ширина изображения
        private int[] pixels;             // собственно массив цветов точек составляющих изображение

        public int getPixel(int x, int y) {
            return pixels[y * width + x];
        }   // получить пиксель в формате RGB

        public int getRed(int color) {
            return color >> 16;
        }         // получить красную составляющую цвета

        public int getGreen(int color) {
            return (color >> 8) & 0xFF;
        } // получить зеленую составляющую цвета

        public int getBlue(int color) {
            return color & 0xFF;
        }        // получить синюю составляющую цвета

        // Конструктор - создание изображения из файла
        public CoolImage(String fileName) throws IOException {
            BufferedImage img = readFromFile(fileName);
            this.height = img.getHeight();
            this.width = img.getWidth();
            this.pixels = copyFromBufferedImage(img);
        }

        // Чтение изображения из файла в BufferedImage
        private BufferedImage readFromFile(String fileName) throws IOException {


            /*
            ImageReader r = new JPEGImageReader(new JPEGImageReaderSpi());
            r.setInput(new FileImageInputStream(new File(fileName)));
            BufferedImage bi = r.read(0, new ImageReadParam());
            ((FileImageInputStream) r.getInput()).close();
            */ //то как должно работать

            File input = new File(fileName); //то как работает
            BufferedImage bi = ImageIO.read(input);

            return bi;
        }

        // Формирование BufferedImage из массива pixels
        private BufferedImage copyToBufferedImage() {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    bi.setRGB(j, i, pixels[i * width + j]);
            return bi;
        }

        // Формирование массива пикселей из BufferedImage
        private int[] copyFromBufferedImage(BufferedImage bi) {
            int[] pict = new int[height * width];
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    pict[i * width + j] = bi.getRGB(j, i) & 0xFFFFFF; // 0xFFFFFF: записываем только 3 младших байта RGB
            return pict;
        }

        // Запись изображения в jpeg-формате
        public void saveAsJpeg(String fileName) throws IOException {
            /*
            ImageWriter writer = new JPEGImageWriter(new JPEGImageWriterSpi());
            saveToImageFile(writer, fileName);
             */ // то как должно работать


            File input = new File(fileName); //то как работает
            BufferedImage image = ImageIO.read(input);

            File compressedImageFile = new File("JP2000.jpg");
            OutputStream os = new FileOutputStream(compressedImageFile);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            writer.write(null, new IIOImage(image, null, null), param);

            os.close();
            ios.close();
            writer.dispose();

        }

        // Собственно запись файла (общая для всех форматов часть).
        private void saveToImageFile(ImageWriter iw, String fileName) throws IOException {
            iw.setOutput(new FileImageOutputStream(new File(fileName)));
            iw.write(copyToBufferedImage());
            ((FileImageOutputStream) iw.getOutput()).close();
        }

        public void yuvConvert(){
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int newRed = getRed(pixels[i * width + j]);
                    if (newRed > 255) newRed = 255;  // Отсекаем при превышении границ байта
                    if (newRed < 0) newRed = 0;

                    int newGreen = getGreen(pixels[i * width + j]);
                    if (newGreen > 255) newGreen = 255;  // Отсекаем при превышении границ байта
                    if (newGreen < 0) newGreen = 0;

                    int newBlue = getBlue(pixels[i * width + j]);
                    if (newBlue > 255) newBlue = 255;  // Отсекаем при превышении границ байта
                    if (newBlue < 0) newBlue = 0;

                    y = (int) (0.299 * newRed + 0.587 * newGreen + 0.114 * newBlue);
                    u = newBlue - y;                    // переводим yuv
                    v = newRed - y;

                    if (i % 2 == 0) {                   //сжимаем через DWT
                        y *= 1.115087052456994;
                        u *= 0.5912717631142470;
                        v *= -0.05754352622849957;
                    } else {                            //сжимаем через DWT
                        y *= 0.6029490182363579;
                        u *= -0.2668641184428723;
                        v *= -0.07822326652898785;
                    }
                    newRed = v + y;                     //возвращаемся в RGB
                    newGreen = (int) (y - ((2 * 0.299 * v * (1 - 0.299) + 2 * 0.114 * u * (1 - 0.114)) / (1 - 0.299 - 0.114)));
                    newBlue = (int) (y + 2 * u * (1 - 0.114));
                    pixels[i * width + j] = pixels[i * width + j] | (newRed << 16) | (newGreen << 8) | (newBlue);
                }
            }
        }
    }
