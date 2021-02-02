package com.lark.util;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * ��һ����ת����ͼƬ������
 */
public class FontImage {
    public static void main(String[] args) throws Exception {
        createImage("����������������", new Font("����", Font.PLAIN, 32), 500, 64);
    }

    // ����str,font����ʽ�Լ�����ļ�Ŀ¼
    public static InputStream createImage(String str, Font font, Integer width, Integer height) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // ����ͼƬ
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);// ���ú�ɫ�������ͼƬ,Ҳ���Ǳ���
        g.setColor(Color.black);// �ڻ��ɺ�ɫ
        g.setFont(font);// ���û�������
        /** ���ڻ�ô�ֱ����y */
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        for (int i = 0; i < 6; i++) {// 256 340 0 680
            g.drawString(str, i * 680, y);// �����ַ���
        }
        g.dispose();
        ImageIO.write(image, "png", outputStream);// ���pngͼƬ
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return byteArrayInputStream;
    }
}
