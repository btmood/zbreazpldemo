package com.lark.test;

/**
 * 博客里的几个案例
 */
public class MyTestOnDiffElementZPL {

    //矩形
    public static String getRectangle() {
        String code = "^XA\n" +
                "^CI28\n" +
                "^CW1,E:SIMSUN.FNT\n" +
                "^PW1000\n" +
                "^LL800\n" +
                "^LH0,0\n" +
                "^MD10\n" +
                "^FO50,50^GB700,200,9^FS\n" +
                "^FO650,570^A1,35,35^FD  掘金：梅长酥^FS\n" +
                "^FO650,620^A1,35,35^FD公众号：梅长酥^FS\n" +
                "^XZ";
        return code;
    }

    //线段
    public static String getLine() {
        String code = "^XA\n" +
                "^CI28\n" +
                "^CW1,E:SIMSUN.FNT\n" +
                "^PW1000\n" +
                "^LL800\n" +
                "^LH0,0\n" +
                "^MD10" +
                "^FO50,50^GB700,200,9^FS" +
                "^FO50,300^GB700,1,9^FS" +
                "^FO50,350^GB700,1,9^FS" +
                "^FO50,400^GB1,100,9^FS" +
                "^FO100,400^GB1,100,9^FS" +
                "^FO150,400^GB1,100,9^FS" +
                "^FO200,400^GB1,100,9^FS" +
                "^FO650,570^A1,35,35^FD  掘金：梅长酥^FS\n" +
                "^FO650,620^A1,35,35^FD公众号：梅长酥^FS\n" +
                "^XZ";
        return code;
    }

    //英文字符
    public static String getEnCharacter() {
        String code =
                "^XA\n" +
                        "^CI28\n" +
                        "^CW1,E:SIMSUN.FNT\n" +
                "^PW1000\n" +
                "^LL800\n" +
                "^LH0,0\n" +
                "^MD10\n" +
                "^FO50,50^A0,55,55^FDSERENITY to accept the things^FS\n" +
                "^FO50,120^A0,130,55^FD i cannot change^FS\n" +
                "^FO50,300^A0,55,55^FDCOURAGE to change the things^FS\n" +
                "^FO50,370^A0,55,130^FD i can^FS\n" +
                "^FO50,550^A0,55,55^FDWISDOM to know the difference^FS\n" +
                "^FO650,570^A1,35,35^FD  掘金：梅长酥^FS\n" +
                "^FO650,620^A1,35,35^FD公众号：梅长酥^FS\n" +
                "^XZ";
        return code;
    }

    //中文字符
    public static String getCnCharacter() {
        String code =
                "^XA\n" +
                "^CI28\n" +
                "^CW1,E:SIMSUN.FNT\n" +
                "^PW1000\n" +
                "^LL800\n" +
                "^LH0,0\n" +
                "^MD10\n" +
                "^FO50,50^A1,55,55^FD赐予我平静，^FS\n" +
                "^FO50,120^A1,55,55^FD去接受我无法改变的。^FS\n" +
                "^FO50,300^A1,55,55^FD给予我勇气^FS\n" +
                "^FO50,370^A1,55,80^FD去改变我能改变的^FS\n" +
                "^FO50,500^A1,55,55^FD赐我智慧^FS\n" +
                "^FO50,570^A1,55,55^FD分辨这两者的区别^FS\n" +
                "^FO650,570^A1,35,35^FD  掘金：梅长酥^FS\n" +
                "^FO650,620^A1,35,35^FD公众号：梅长酥^FS\n" +
                "^XZ";
        return code;
    }


}
