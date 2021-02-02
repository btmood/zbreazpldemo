package com.lark.test;

import com.lark.util.ZPLConveter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 获取可供打印的ZPL代码
 * 由于Graph2D类没法拉伸字体，所以部分较长的字段用zebra designer3生成图片zpl代码复制到这里
 * 其他部分由Graph2D类画图片再转成ZPL的形式
 */
public class ZPLCodeConverter {

    public static String getTag01(String type, String color, String rolls, String thickness, String grossWeight, String width, String remark, String length, String rollNO, String date) throws IOException {
        // 创建图片
        BufferedImage image = new BufferedImage(1000, 800, BufferedImage.TYPE_INT_BGR);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 1000, 800);
        //设置画笔颜色
        g2d.setColor(Color.BLACK);

        //按顺序的横线
        myDrawLine(g2d, 3,10, 10, 760, 10);
        myDrawLine(g2d, 3,10, 90, 760, 90);
        myDrawLine(g2d, 3,10, 160, 760, 160);
        myDrawLine(g2d, 3,10, 230, 760, 230);
        myDrawLine(g2d, 3,10, 300, 760, 300);
        myDrawLine(g2d, 3,10, 370, 760, 370);
        myDrawLine(g2d, 3,10, 440, 760, 440);
        myDrawLine(g2d, 3,10, 510, 760, 510);
        myDrawLine(g2d, 3,10, 650, 760, 650);
        //按顺序的竖线
        myDrawLine(g2d, 3,10, 10, 10, 650);
        myDrawLine(g2d, 3,197, 90, 197, 440);
        myDrawLine(g2d, 3,384, 160, 384, 370);
        myDrawLine(g2d, 3,384, 440, 384, 510);
        myDrawLine(g2d, 3,571, 160, 571, 370);
        myDrawLine(g2d, 3,760, 10, 760, 650);
        //按顺序的字
        //行1
        myDrawString(g2d, 20, new Font("宋体", Font.BOLD, 60), "PRODUCT INFO SHEET", 80,75, 1);
        //行2
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), "TYPE", 40,145, 1);
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), type, 300,145, 1);
        //行3
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), "COLOR", 30,215, 1);
        myDrawString(g2d, 20, new Font("宋体", Font.BOLD, 53), color, 230,215, 1);
//        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 30), "NO.OF ROLLS", 350,215);
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), rolls + " ROLL", 575,215, 1);
        //行4
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), "12 mic", 210,285, 1);
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), grossWeight + " KG", 575,285, 1);
        //行5
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), "12 mic", 210,355, 1);
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), remark, 575,355, 1);
        //行6
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), length + "M", 260,425, 1);
        //行7
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 50), "ROLL NO.:" + rollNO, 20,495, 0.8);
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 50), "DATE.:" + date, 400,495, 0.8);

        g2d.dispose();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);// 输出png图片
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String code =
            "^XA\n" +
            "^MMT\n" +
            "^PW767\n" +
            "^LL0687\n" +
            "^LS0\n" +
            "^FO0,0" + ZPLConveter.getFont2ImgZPL(byteArrayInputStream, true , 50) + "\n" +
            "^FO0,224^GFA,02304,02304,00024,:Z64:\n" + //THICKNESS
            "eJzt00tu2zAQBuCfFitqQdgGstFCkH0ELaeoCwvIRXgELb3IgwFyrzDoRXgEdyegQdQh5YdkpYsiy3hWxAeIGP2cAa51ra9QqvvdblWHhXvfluJP1yZ7PETf70s0oHZXzmH8Li3xywFpURnN3hBphdrnei6eBm6Isui5SuABWSyju5zy4PrSM1oqW3tZJSkMoHsXwb8NPCuy6Dajde8ixXrgmt3VHgdHgeCJBdUyOtL4w0P3H7oNvr5wBDfsBDX271Y27AX425Ork5dI3Mh/WFnV3iwg/KUTu3Joe398i74N3vnE427kP53Ma+9Fi+3ono2TBd+DN6zs0OngG6ixe6m5TxwaPbuR8+CHRifOx7E3MuU8wZGYC1fRLT/3MM/qI+f8SSbsa/HMb3N2GdxNPaM8+GzqhRQTF0c/ztXR3ZL0Px0Th69oHlzTjMf67I3pvdikyvZ5xj26d7TQPP+7UievIf9OxL1b2ffVC2679qadPQ72Mj2damT/s9DXutan6y8HDLhJ:599A\n" +
            "^FO388,160^GFA,02304,02304,00024,:Z64:\n" + //NO.OF ROLLS
            "eJzt07FOwzAQBuCLXV2MsNKMEarabKwWU4QiyKNEPAFvQNJUgaHqzOMEZh4ij8DYoQLOvkJCqlKx5x8yfIMjn+8HGDPmdNJp1QJMy88W/B1sF8Ut+A25FtLQt2lbkBt4C2AG6p08EBgCRFmeg1d5KwQNoXGuIwCTZ4ZcYt8VeWy9rIR1EzpXGiA2WUi+lNZj6+ciIs/Z679d9R1Ou73v2TJC8ojdqztPyFv2Td8NecO+dm6cv+69+PHEujr0dOiVdj4f+iP71Pkd+wP5ht0vBr5jl03P7TnP7F478Cd2cH7V+Xrv90fcHHF7YXXdzefbI+sXna9+eTfnOuh79y41stt3VPMDz5wH3fvWKNHT4sAr9viYu/+iosWN2UXftUrs3lqXAqVGwz6JEghz5x6i0BO3zwoxTKkXbk/KFaLWiZubECYB1dDMZVmsg2A2S3PyGyHbhHpHnfJfmu3i43Z32XAlixDEvp1uBb3/9HnMmDEAXw/Vf0E=:0772\n" +
            "^FO32,288^GFA,01920,01920,00020,:Z64:\n" + //WIDTH
            "eJzt0jFqwzAUBuBfPIi3as0QHB+hYwgm+Cg+gtMuoRQiaMkU6JU0dsgBPCo3UDYPAVWWnDqxjNsxBP/T44OHxOMHxox57Gy/Tue5qbhRmBvzYZgRiEkWWAHmXJt4r+eYRIIUqGLMOMlNPV9MR2LBmbcnkrkzks8cHWOqNdYYVMKRXSzzpnOOvGOFtcSZfUt624ADzqI+IzVgi2sT3tJhmxxD24VGt7a1dwR9nvCH+d39usdeQzuExr777CW08g3dG7Byie6tWLkKTQ9Z8WvQaWP6f8ZUa2rWmAwturapNy5ay71V9ks3Vve5sibg+oxsiqb3MJpc78fcV34AqWunrA==:ECA8\n" +
            "^FO0,352^GFA,02688,02688,00028,:Z64:\n" + //LENGTH
            "eJzt1DFOwzAUBuDfcoQ7VHjtgByOkaFqjRgYOQI5QscMEXhA4loeOIgHDuCBIUOEeS4VFMcOHCD/ENn+ZCvS8zOwZMmSJX9FBGAfAvd4svShofjg4YRD9IAOF5AhGq30J/NkpiarsKtNo4TxNDkzYXqyTpiD4qnxox24aRVLjZlHMlewB1TMMaMVUoONZpmxCjY1fW8qbmHJdGqIRtuh4kpid7aK2zO2wu3RkDdXthtfCZRN5m0DPZSsKVsYoPtvCzT9n8UzdzP/UpdsNW/yWWI/lk24vIlXCZ6v0azxN7puBWNz9p6zWNRrjZFnbE3WRrNTW8VLpNGXzJKxjG2+bMvc1Brqh2jwU+tij2lc0SCxgXlhJNka28ukRiF46nfTktUjUOOn3yUZPQAvI/Wm8GDBnb0TS5b8yicfAODp:7BE9\n" +
            "^FO388,224^GFA,02304,02304,00024,:Z64:\n" + //GROSS WEIGHT
            "eJzt07FKw0AYB/DveqGJcNTrlqHUhL7AFZcIIoGKo4NPkFpxFl06FLwawaX4Dr7JtZaOPoKkuDh2s0OhfmeSNo0dOjhJ/ksuP0Iu4b4/QJEi/z/k817WYU4joO9fS3Nuvkw/lujUO1KH5LnsARl0nkCxqXrVXrJF4PbLho3eoKCsQE2a6GWLiyAsMUs7AWU7a/cfqMXQW7GPhQQwtI/WzhNnBhdqRCwD4LEFUgme8Qmx825pP0sdlMOVSl1egw1ZxxsbeDPrAWT8EixcDlpGzjvAcPm2u8vKFm/LtjK3e2TiJ7+FLOvVoQxmVGlfaF8mftCXQZdEq+dTr6IfwwzgPNx4v/Y6dAEuNvxEewW81OdZN6GWd8bRqf7h2MXKhyuHXZ0AntdtzpW8Ihz9jm66v80txgN1+uMdivPgJM5yLrIez9Uv92NvZN1g3InnFlxKpOKJl1k659pxnlOvCeGmjr2wb2Kn3BNNOt7n2nWP/NhJ1PPqpd4enpdr6t6F02ihC+nj3sD1Je7n31W9SJGd8w28B+AF:BC16\n" +
            "^FO388,288^GFA,02304,02304,00024,:Z64:\n" + //REMARK
            "eJzt07GO00AQBuDfWV+WItjtIZmkpD0qXFjySbwIEcW1KbeILmsh8VwbXUHJA1AwiBfY61yELLOz8eZOokSIIlNZn631r5lZ4FKXutS/rj5QEcLYQx2whApHrMbeAzoc+N2orfLs2AWnSDt2d48O0Sn6VpNyms/RdsXeaasJPWAKr9xcvLYdGvF37Bgn1+wL7SY3yi2Su8nfRN8qdy2u2Ofa1YQ7y9499Zl2LwkfT36THc/8w9mviP39yV322yt6TXhwf/BXhH3yvc35n7hRDzh76dmH5F+S19wfF70YiN1Pzn0r2N9SYUn69jWdf8/9Pzv3+bt4GMUNO4jnYtU3yRlIzmFX8NgdoR7FPzn5r1mzb2A6doo+RL/N3kD9SE7i7Zpm0XknnJc8ybs1xQimyu6T75PX7JvsiD5Hm9yIbyYvo2v2VtyIL/e04AdzmhN7K74akiv2Rpy3DcXZnXLL7Cr6CzTJK/GGfVbWA13z4poi+5J9XtbFyfM+V88c2eMcq+gtKpmj03byptQF3aBOrsQ1+7acx++jb2KiuJja9uHA7g1qu+WkxFcKvVePIRz6z/WvI3Y/A/rVWI9/67Zf6n+t3678/cs=:3A6F\n" +
            "^BY4,3,84^FT124,602^BCN,,Y,N\n" + //条码
            "^FD>;123456123456123456^FS\n" +
            "^PQ1,0,1,Y" +
            "^XZ\n";
        return code;
    }

    /**
     * 画一条线
     * @param g2d Graph2D对象
     * @param fontSize 线条宽度
     * @param sWidth 线条起点X坐标
     * @param sHeight 线条起点Y坐标
     * @param eWidth 线条终点X坐标
     * @param eHeight 线条终点Y坐标
     */
    public static void myDrawLine(Graphics2D g2d, Integer fontSize, Integer sWidth, Integer sHeight, Integer eWidth, Integer eHeight) {
        //设置线条的宽度
        BasicStroke bs1 = new BasicStroke(fontSize);
        g2d.setStroke(bs1);
        g2d.drawLine(sWidth, sHeight, eWidth, eHeight);
    }

    /**
     * 画一句字
     * 使用rate可以控制字体的间距，但是并不会拉伸字体，可能导致字体重叠
     * @param g2d Graph2D对象
     * @param fontSize 画笔宽度
     * @param font 字体格式
     * @param str *输入的字
     * @param x 字的X轴
     * @param y 字的Y轴
     * @param rate 字的间距（正常为1）
     */
    public static void myDrawString(Graphics2D g2d, Integer fontSize, Font font, String str, Integer x, Integer y, double rate) {
        g2d.setFont(font);
        String tempStr=new String();
        int orgStringWight=g2d.getFontMetrics().stringWidth(str);
        int orgStringLength=str.length();
        int tempx=x;
        int tempy=y;
        while(str.length()>0)
        {
            tempStr=str.substring(0, 1);
            str=str.substring(1, str.length());
            g2d.drawString(tempStr, tempx, tempy);
            tempx=(int)(tempx+(double)orgStringWight/(double)orgStringLength*rate);
        }
    }


}
