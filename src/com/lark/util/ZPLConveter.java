package com.lark.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 将图片流转换为ZPL指令
 */
public class ZPLConveter {
    private int blackLimit = 380;
    private int total;
    private int widthBytes;
    private boolean compressHex = false;
    private static Map<Integer, String> mapCode = new HashMap<Integer, String>();
    {
        mapCode.put(1, "G");
        mapCode.put(2, "H");
        mapCode.put(3, "I");
        mapCode.put(4, "J");
        mapCode.put(5, "K");
        mapCode.put(6, "L");
        mapCode.put(7, "M");
        mapCode.put(8, "N");
        mapCode.put(9, "O");
        mapCode.put(10, "P");
        mapCode.put(11, "Q");
        mapCode.put(12, "R");
        mapCode.put(13, "S");
        mapCode.put(14, "T");
        mapCode.put(15, "U");
        mapCode.put(16, "V");
        mapCode.put(17, "W");
        mapCode.put(18, "X");
        mapCode.put(19, "Y");
        mapCode.put(20, "g");
        mapCode.put(40, "h");
        mapCode.put(60, "i");
        mapCode.put(80, "j");
        mapCode.put(100, "k");
        mapCode.put(120, "l");
        mapCode.put(140, "m");
        mapCode.put(160, "n");
        mapCode.put(180, "o");
        mapCode.put(200, "p");
        mapCode.put(220, "q");
        mapCode.put(240, "r");
        mapCode.put(260, "s");
        mapCode.put(280, "t");
        mapCode.put(300, "u");
        mapCode.put(320, "v");
        mapCode.put(340, "w");
        mapCode.put(360, "x");
        mapCode.put(380, "y");
        mapCode.put(400, "z");
    }
    private static class SingletonHolder {
        private static final ZPLConveter INSTANCE = new ZPLConveter();
    }
    private ZPLConveter(){}

    public static final ZPLConveter getInstance() {
        return SingletonHolder.INSTANCE;
    }
    public String convertfromImg(BufferedImage image) throws IOException {
        String cuerpo = createBody(image);
        if(compressHex)
            cuerpo = encodeHexAscii(cuerpo);
        return headDoc() + cuerpo + footDoc();
    }
    private String createBody(BufferedImage orginalImage) throws IOException {
        StringBuffer sb = new StringBuffer();
        Graphics2D graphics = orginalImage.createGraphics();
        graphics.drawImage(orginalImage, 0, 0, null);
        int height = orginalImage.getHeight();
        int width = orginalImage.getWidth();
        int rgb, red, green, blue, index=0;
        char auxBinaryChar[] =  {'0', '0', '0', '0', '0', '0', '0', '0'};
        widthBytes = width/8;
        if(width%8>0){
            widthBytes= (((int)(width/8))+1);
        } else {
            widthBytes= width/8;
        }
        this.total = widthBytes*height;
        for (int h = 0; h<height; h++)
        {
            for (int w = 0; w<width; w++)
            {
                rgb = orginalImage.getRGB(w, h);
                red = (rgb >> 16 ) & 0x000000FF;
                green = (rgb >> 8 ) & 0x000000FF;
                blue = (rgb) & 0x000000FF;
                char auxChar = '1';
                int totalColor = red + green + blue;
                if(totalColor>blackLimit){
                    auxChar = '0';
                }
                auxBinaryChar[index] = auxChar;
                index++;
                if(index==8 || w==(width-1)){
                    sb.append(fourByteBinary(new String(auxBinaryChar)));
                    auxBinaryChar =  new char[]{'0', '0', '0', '0', '0', '0', '0', '0'};
                    index=0;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    private String fourByteBinary(String binaryStr){
        int decimal = Integer.parseInt(binaryStr,2);
        if (decimal>15){
            return Integer.toString(decimal,16).toUpperCase();
        } else {
            return "0" + Integer.toString(decimal,16).toUpperCase();
        }
    }
    private String encodeHexAscii(String code){
        int maxlinea =  widthBytes * 2;
        StringBuffer sbCode = new StringBuffer();
        StringBuffer sbLinea = new StringBuffer();
        String previousLine = null;
        int counter = 1;
        char aux = code.charAt(0);
        boolean firstChar = false;
        for(int i = 1; i< code.length(); i++ ){
            if(firstChar){
                aux = code.charAt(i);
                firstChar = false;
                continue;
            }
            if(code.charAt(i)=='\n'){
                if(counter>=maxlinea && aux=='0'){
                    sbLinea.append(",");
                } else     if(counter>=maxlinea && aux=='F'){
                    sbLinea.append("!");
                } else if (counter>20){
                    int multi20 = (counter/20)*20;
                    int resto20 = (counter%20);
                    sbLinea.append(mapCode.get(multi20));
                    if(resto20!=0){
                        sbLinea.append(mapCode.get(resto20) + aux);
                    } else {
                        sbLinea.append(aux);
                    }
                } else {
                    sbLinea.append(mapCode.get(counter) + aux);
                    if(mapCode.get(counter)==null){
                    }
                }
                counter = 1;
                firstChar = true;
                if(sbLinea.toString().equals(previousLine)){
                    sbCode.append(":");
                } else {
                    sbCode.append(sbLinea.toString());
                }
                previousLine = sbLinea.toString();
                sbLinea.setLength(0);
                continue;
            }
            if(aux == code.charAt(i)){
                counter++;
            } else {
                if(counter>20){
                    int multi20 = (counter/20)*20;
                    int resto20 = (counter%20);
                    sbLinea.append(mapCode.get(multi20));
                    if(resto20!=0){
                        sbLinea.append(mapCode.get(resto20) + aux);
                    } else {
                        sbLinea.append(aux);
                    }
                } else {
                    sbLinea.append(mapCode.get(counter) + aux);
                }
                counter = 1;
                aux = code.charAt(i);
            }
        }
        return sbCode.toString();
    }
    private String headDoc(){
//        String str = "^XA " +
//                "^FO0,0^GFA,"+ total + ","+ total + "," + widthBytes +", ";
        String str = "^GFA,"+ total + ","+ total + "," + widthBytes +", ";
        return str;
    }
    private String footDoc(){
//        String str = "^FS"+
//                "^XZ";
        String str = "^FS";
        return str;
    }
    public void setCompressHex(boolean compressHex) {
        this.compressHex = compressHex;
    }
    public void setBlacknessLimitPercentage(int percentage){
        blackLimit = (percentage * 768 / 100);
    }

    /**
     *
     * @param inputStream 图片流
     * @param compressHex 直接填true即可
     * @param blacknessLimitPercentage 直接填50即可
     * @return 返回ZPL指令
     */
    public static String getFont2ImgZPL(InputStream inputStream, Boolean compressHex, int blacknessLimitPercentage) throws IOException {
        SingletonHolder.INSTANCE.setBlacknessLimitPercentage(blacknessLimitPercentage);
        SingletonHolder.INSTANCE.setCompressHex(compressHex);
        BufferedImage orginalImage = ImageIO.read(inputStream);
//        System.out.println(SingletonHolder.INSTANCE.convertfromImg(orginalImage));
        return SingletonHolder.INSTANCE.convertfromImg(orginalImage);
    }

    public static void main(String[] args) throws Exception {
//        BufferedImage orginalImage = ImageIO.read(new File("d:/a.png"));
//        ZPLConveter zp = new ZPLConveter();
//        zp.setCompressHex(true);
//        zp.setBlacknessLimitPercentage(50);
//        System.out.println(zp.convertfromImg(orginalImage));
        FileInputStream fileInputStream = new FileInputStream(new File("d:\\a.png"));
//        String font2ImgZPL = ZPLConveter.getFont2ImgZPL(FontImage.createImage("请在这里输入文字", new Font("宋体", Font.PLAIN, 32), 500, 64), true, 50);
        String font2ImgZPL = ZPLConveter.getFont2ImgZPL(fileInputStream, true, 50);
        System.out.println(font2ImgZPL);
    }
}
