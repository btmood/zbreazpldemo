package com.lark.util;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 汉字转zpl图片
 */
public class Font2ZplGF {

    private static BufferedImage source = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    private static Graphics2D gs = source.createGraphics();

    //	public static String getFontZpl(String content, int x, int y, int size, String fontName) {
    //		return String.format("^FO%d,%d^A1N,%d,%d^FD%s^FS", x, y, size, size, content);
    //	}

    public static String getFontHexWithWidth(String content, int x, int y, int width,
                                             int maxHeight, String fontName) {
        if (content == null || "".equals(content))
            return "";
        Font f = null;
        width = (width + 7) / 8 * 8;
        int size = width / content.length();
        int retryFlag = 1;
        if (size > maxHeight) {
            size = maxHeight;
            if ("宋体".equals(fontName)) {
                f = new Font("simsun", Font.PLAIN, size);
            } else if ("黑体".equals(fontName)) {
                f = new Font("simhei", Font.BOLD, size);
            } else {
                f = new Font("simsun", Font.PLAIN, size);
            }
        } else {
            while (true) {
                if ("宋体".equals(fontName)) {
                    f = new Font("simsun", Font.PLAIN, size);
                } else if ("黑体".equals(fontName)) {
                    f = new Font("simhei", Font.BOLD, size);
                } else {
                    f = new Font("simsun", Font.PLAIN, size);
                }
                gs.setFont(f);
                FontMetrics fontMetrics = gs.getFontMetrics();
                Rectangle2D stringBounds = fontMetrics.getStringBounds(content, gs);
                int nw = (int) stringBounds.getWidth();

                if (nw > width) {
                    size--;
                    if (retryFlag == 1) {
                        break;
                    }
                    retryFlag = 0;

                } else {
                    if (size >= maxHeight)
                        break;
                    size++;
                    retryFlag = 1;
                }
            }
        }
        int height = size;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setFont(f);
        g2.setColor(Color.BLACK);
        g2.drawString(content, 1, (int) (height * 0.88));

        g2.dispose();

        return "^FO" + x + "," + y +
                getImage(image) + "^FS";
    }

    public static String getFontHex(String content, int x, int y, int size, int style) {
        if (content == null || "".equals(content))
            return "";
        Font f = getSelfDefinedFont("font/SIMHEI.TTF", style, size);
//        f = new Font("simhei", Font.BOLD, size);

        gs.setFont(f);
        FontMetrics fontMetrics = gs.getFontMetrics();
        Rectangle2D stringBounds = fontMetrics.getStringBounds(content, gs);
        int width = (int) stringBounds.getWidth();
        width = (width + 7) / 8 * 8;
        int height = width;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        image = modifyImageRatio(image,270);

        Graphics2D g2 = image.createGraphics();

        g2.setFont(f);
        g2.setColor(Color.BLACK);
        // 图片旋转
        g2.rotate(Math.toRadians(270), width / 2, height / 2);
        g2.drawString(content, 1, (int) (size * 0.88));
        g2.dispose();

        StringBuilder zpl = new StringBuilder("^FO").append(x).append(",").append(y)
                .append(getImage(image)).append("^FS");

        return zpl.toString();

    }

    /**
     * 自定义字体
     * @param filepath 字体文件路径
     * @param style 样式
     * @param size 大小
     * @return
     */
    private static Font getSelfDefinedFont(String filepath, int style, float size){

        Font font = null;
        try{
            File file = new File(filepath);

//            Resource resource = new ClassPathResource(filepath);
            font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(file));
            font = font.deriveFont(style, size);
        } catch (FontFormatException | IOException e){
            throw new RuntimeException(e);
        }
        return font;
    }

    /**
     * 旋转图片为指定角度
     *
     * @param bufferedimage 目标图像
     * @param degree        旋转角度
     * @return
     */
    public static BufferedImage rotateImage(final BufferedImage bufferedimage,
                                            final int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }

    /**
     * @param mini  贴图
     * @param ratio 旋转角度
     * @return
     */
    public static BufferedImage modifyImageRatio(BufferedImage mini, int ratio) {
        int src_width = mini.getWidth();
        int src_height = mini.getHeight();
        //针对图片旋转重新计算图的宽*高
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), ratio);
        //设置生成图片的宽*高，色彩度
        BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
        //创建画布
        Graphics2D g2 = res.createGraphics();
        res = g2.getDeviceConfiguration().createCompatibleImage(rect_des.width, rect_des.height, Transparency.TRANSLUCENT);
        g2 = res.createGraphics();
        //重新设定原点坐标
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        //执行图片旋转，rotate里包含了translate，并还原了原点坐标
        g2.rotate(Math.toRadians(ratio), src_width / 2, src_height / 2);
        g2.drawImage(mini, null, null);
        g2.dispose();
        return res;
    }

    private static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    public static String printImage(BufferedImage image, int x, int y) {
        if (image.getWidth() % 8 != 0) {
            BufferedImage i = new BufferedImage(((image.getWidth() + 7) / 8) * 8,
                    image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = i.createGraphics();
            g2.drawImage(image, null, 0, 0);
            g2.dispose();
            image = i;
        }
        StringBuilder zpl = new StringBuilder("^FO").append(x).append(",").append(y)
                .append(getImage(image)).append("^FS");
        return zpl.toString();
    }

    private static String getImage(BufferedImage i) {
        int w = i.getWidth();
        int h = i.getHeight();
        boolean black[] = getBlackPixels(i, w, h);
        int hex[] = getHexValues(black);

        String data = ints2Hex(hex);

        int bytes = data.length() / 2;
        int perRow = bytes / h;

        return "^GFA," + bytes + "," + bytes + "," + perRow + "," + data;

    }


    private static String flipRows(String hex, int height) {
        String flipped = "";
        int width = hex.length() / height;

        for (int i = 0; i < height; i++) {
            flipped += new StringBuilder(hex.substring(i * width, (i + 1) * width)).reverse()
                    .toString();
        }
        return flipped;
    }

    /**
     * Returns an array of ones or zeros. boolean is used instead of int for memory considerations.
     *
     * @param bi
     * @param w
     * @param h
     * @return
     */
    private static boolean[] getBlackPixels(BufferedImage bi, int w, int h) {
        int[] rgbPixels = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        int i = 0;
        boolean[] pixels = new boolean[rgbPixels.length];
        for (int rgbpixel : rgbPixels) {
            pixels[i++] = isBlack(rgbpixel);
        }

        return pixels;
    }

    private static boolean isBlack(int rgbPixel) {
        int a = (rgbPixel & 0xFF000000) >>> 24;
        if (a < 127) {
            return false; // assume pixels that are less opaque than the luma threshold should be considered to be white
        }
        int r = (rgbPixel & 0xFF0000) >>> 16;
        int g = (rgbPixel & 0xFF00) >>> 8;
        int b = rgbPixel & 0xFF;
        int luma = ((r * 299) + (g * 587) + (b * 114)) / 1000; //luma formula
        return luma < 127;
    }

    private static int[] getHexValues(boolean[] black) {
        int[] hex = new int[(int) (black.length / 8)];
        // Convert every eight zero's to a full byte, in decimal
        for (int i = 0; i < hex.length; i++) {
            for (int k = 0; k < 8; k++) {
                hex[i] += (black[8 * i + k] ? 1 : 0) << 7 - k;
            }
        }
        return hex;
    }

    public static String getHexString(byte[] b) throws Exception {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    private static String ints2Hex(int[] ints) {
        char[] hexChars = new char[ints.length * 2];
        for (int i = 0; i < ints.length; ++i) {
            hexChars[i * 2] = HEX[(ints[i] & 0xF0) >> 4];
            hexChars[i * 2 + 1] = HEX[ints[i] & 0x0F];
        }
        return new String(hexChars);
    }
}
