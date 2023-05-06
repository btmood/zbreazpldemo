# 斑马打印机ZPL语言编程实战

> csdn `http://t.csdn.cn/AZmsz`

## 什么是ZPL语言？
#### ZPL
>ZPL是斑马条码打印机工业型号用的编程语言。利用这些编程语言，编辑好一个打印的指令集，发送给条码打印机，条码打印机就会把ZPL所绘制的标签打印出来。
#### EPL
>EPL也是斑马打印机的一种编程语言，主要用于条码打印机桌面小机器。在安装斑马驱动的时候，可以看到驱动分为`GK888t`以及`GK888t(EPL)`两种驱动。

**需要注意的是，安装不同类型的驱动后，使用官方提供的`zebra designer3`软件绘制标签，能导出的编程语言是不同的**
- `GK888t`对应ZPL
- `GK888t(EPL)`对应EPL

EPL看起来就像一堆乱码，不用过多了解，画标签主要使用ZPL。接下来详细介绍ZPL语言的基础语法

## 使用ZPL指令打印

## 起点：通过Java执行ZPL命令

1. 准备工作

- 导入斑马SDK：`ZSDK_API.jar`（文章最后提供）

2. 代码案例
```
//打印机的IP地址
static final String theIpAddress = "10.10.40.200";

//打印方法
public static void printZPL() throws Exception {
    //连接打印机
    Connection thePrinterConn = new TcpConnection(theIpAddress, TcpConnection.DEFAULT_ZPL_TCP_PORT);
    try {
        thePrinterConn.open();
        ZebraPrinter zebraPrinter = ZebraPrinterFactory.getInstance(thePrinterConn);
        //sendCommand()方法里的参数就是ZPL命令字符串。这里我通过方法返回ZPL了
        zebraPrinter.sendCommand(ZPLCodeConverter.getTag05_2(  "1","1","1", "12", "218", "1280", "8000", "07151-3A", "2020-11-11"));
        zebraPrinter.sendCommand(ZPLCodeConverter.getTag05_2_cn(  "1","1","1", "12", "218", "1280", "8000", "07151-3A", "2020-11-11"));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public staic void main(String[] arges) {
    printZPL();
}
```

## ZPL语法

### 标签页面整体配置
```
^XA     //标签开始
^LH0,0     //标签左上角的坐标(x轴，y轴)
^MD10      //打印标签的颜色深度（范围0~30）
^PR 	//打印速度
^LL 	//标签长度
^PW 	//标签宽度
^LR 	//标签反转（'Y'/'N'）
^XZ     //标签结束
```

- `^XA`、`^XZ`是一对指令，代表ZPL命令的开始和结束，所有的其他ZPL指令都要写在这两个指令间。
- `^LH`代表标签左上角的坐标，通过这个指令，你可以定义标签在纸上的位置
    - `^LH10,20`：代表标签上边距为10，左边距为20
- `^MD10`：标签打印的颜色深度，值越大，颜色越深。范围：0~30.实际使用中根据字体不同，这个指令可能不会生效。
- ~~`^PR`：标签打印速度，这个指令基本不使用~~
- `^LL`：标签的长度，可以根据标签纸的长度来调整
    - `^LL500`：标签长度为500
- `^PW`：标签的宽度
    - `^PW800`：标签的宽度为800
- `^LR`：标签反转，可以控制标签在纸上显示的方向。值：'Y'/'N'

### 矩形--->线条
说完了控制标签整体样式的指令后，就该开始介绍怎样绘制标签了。

一般标签里都会用横和竖来搭建标签的框架，在ZPL中使用`^FO`、`^GB`指令来画线条


#### 矩形
1. 画一个矩形
    - 以下命令可以绘制一个矩形
```
//命令
^FO50,500^GB700,200,6^FS
//解释
^FO(x轴坐标)，(y轴坐标)^GB(x轴拉伸)，(y轴拉伸)，(线条宽度)^FS
```
- `^FO50,500`：代表这个元素左上角在标签里的坐标。`^FO`可以理解为标识这是一个元素（e.g.线条、字符、图片）
- `^GB700,200,6`：用于设置矩形的长和高、矩形边的宽度
    - `700`：矩形的长
    - `200`：矩形的高
    - `6`：矩形边的宽度
- `^FS`：这个指令主要有两个功能
    - 其他指令间的分隔
    - ZPL注释，类似java的行注释//
2. 案例
    - **注意**：ZPL里并没有`//`,使用它来做注释只是为了方便（用^FS显得太乱！）
    - **如果你要用案例代码测试，请去掉相关注释**
 - 代码
    
```
^XA //标签开始
^PW1000 //标签宽度为1000mm
^LL800 //标签长度为800
^LH0,0 //标签内容左上角的坐标
^MD10 //标签打印深度
//一个左上角坐标为（50，50），长为700，宽为200，边宽度为9的矩形
^FO50,50^GB700,200,9^FS
^XZ //标签结束
```

- 效果

![矩形](https://img-blog.csdnimg.cn/img_convert/635fdc4cbfcea7a85ba85512e5a6c1a1.png)

#### 横、竖

既然已经把矩形画出来了，那么怎么画一条竖或者横呢？留作思考题，课后思考。
1. 横
    - 很简单，把矩形的高设置为1就行了
2. 竖
    - 同理，把矩形的长设置为1就行了
3. 案例

- 代码

```
^XA
^PW1000
^LL800
^LH0,0
^MD10
^FO50,50^GB700,200,9^FS //矩形
^FO50,300^GB700,1,9^FS //横1
^FO50,350^GB700,1,9^FS //横2
^FO50,400^GB1,100,9^FS //竖1
^FO100,400^GB1,100,9^FS //竖2
^FO150,400^GB1,100,9^FS //竖3
^FO200,400^GB1,100,9^FS // 竖4
^XZ
```
- 效果

![横、宽](https://img-blog.csdnimg.cn/img_convert/4b787b7b3f64eb3e3039eead16d0d590.png)


### 字符

搭完框架以后就需要填入内容了，在斑马打印机内部存储了一些字体，可以选择不同的字体打印。

对于英文及符号，使用默认的`zebra 0`字体，可以任意改变字体大小，字体格式等

但是对于**中文处理**，斑马只只提供了一种字体：`SIMSUN.FNT`，也就是`宋体`。用这种字体会产生一个问题：字符大小只能按照倍数放大，而不是和你想的那样能伸缩自如，可大可小。并且字体格式业务也无法按照想法改变。所以中文字符处理有两种处理方式：
- 调整标签结构以适应字体大小
- 使用高级语言画出一张标签的图片，将这张图片转换成ZPL指令的格式，再打印这张图片。

**对于第二种方式会单独介绍**

#### 英文字符

***案例***

```
^XA
^PW1000
^LL800
^LH0,0
^MD10
^FO50,50^A0,55,55^FDSERENITY to accept the things^FS
^FO50,120^A0,130,55^FD i cannot change^FS
^FO50,300^A0,55,55^FDCOURAGE to change the things^FS
^FO50,370^A0,55,130^FD i can^FS
^FO50,550^A0,55,55^FDWISDOM to know the difference^FS
^XZ
```
- `^FO50,50`：代表这段文字左上角的坐标（`^FO`前面画线段也用到了，主要用于设置坐标）
- `^A0,55,55`
    - `^A`：用来选择字体，有两种方式。
        1. `^A@字体名`，这种方式一般不用，至于字体名有哪些，你可以打印打印机的配置信息，里面包含了打印机当前安装了哪些字体，至于怎么打印可以看上面介绍安装打印机的部分。
        2. `^A数字或字母（A-Z,0-9）`：`^A`后面默认跟的是数字0，代表默认字体，上面的例子用的就是0。后面跟的数字我们是可以自定义的,需要用`^CW`命令。    比如`^CW1,E:SIMSUN.FNT`，此时使用`^A1`就代表后面的文字使用`E:SIMSUN.FNT`字体，我们给数字1赋值了一个字体。**后面讲中文字体的时候会着重介绍**
    - 第一个`55`：让字符沿Y轴拉伸
    - 第二个`55`：让字体沿X轴拉伸
- `^FD`：后面跟着你需要打印的文字
- `^FS`：用来分割ZPL指令

***英文打印效果***


![](https://img-blog.csdnimg.cn/img_convert/dc959411e5be266e4c0c0a3509b9d077.png)

#### 中文字符

##### ZPL指令打印中文字符存在的一些问题

对于中文，我们需要使用斑马提供的中文字体，即`SIMSUN.FNT`（宋体）。

和默认的英文字体不同的是，这种字体**没法按照数字随意控制字符大小**。比如：设置字体尺寸为30-35大小其实都一样。设置成36-45，字体大小也都一样。可以理解为某个区间内字体大小都相同。

对于这种情况可以使用高级语言画一张标签的图片，再把这张图片转换为ZPL指令，接着打印这个ZPL指令就好了。

##### 使用ZPL指令打印中文

***案例***
```
^XA
^CI28
^CW1,E:SIMSUN.FNT
^PW1000
^LL800
^LH0,0
^MD10\n
^FO50,50^A1,55,55^FD赐予我平静，^FS
^FO50,120^A1,55,55^FD去接受我无法改变的。^FS
^FO50,300^A1,55,55^FD给予我勇气^FS
^FO50,370^A1,55,80^FD去改变我能改变的^FS
^FO50,500^A1,55,55^FD赐我智慧^FS
^FO50,570^A1,55,55^FD分辨这两者的区别^FS
^XZ
```
- `^CI28`：调用用来打印的国际字符集
    - `14`：只能打印每行都是中文
    - `26`：可以打印每行既有中文也有英文
    - `28`：代表UTF-8字符，中文打印一般用这个字符集
    - 经过试验，打印中文的话只有28有用，用其他字符集可能会乱码。
- `^CW1,E:SIMSUN.FNT`：使用`SIMSUN.FNT`作为打印的字体，并且把这个字体用数字1标识。之后可以用`^A1`来调用这个字体。你也可以用2、3任意数字标识这个字体。
    1. 如何知道用什么字体？  
        中文一般使用`SIMSUN.FNT`(宋体)。
    2. 打印机包含哪些字体？  
        既然你都看到这里了，想必已经了解如何打印这台打印机的配置信息了，这台打印机所拥有的字体就包含在那里面，可以随意使用。
- `^A1`：1即是我们配置的字体，代表这行字使用这个字体。

***中文打印效果***

![在这里插入图片描述](https://img-blog.csdnimg.cn/3c346f97edbf4e8bb48cfb6178c56c89.jpeg#pic_center)


##### 使用`java`绘制中文标签DEMO

1. java图片流转ZPL指令
- 调用`getFont2ImgZPL(arg1,arg2,arg3)`方法可以将java图片流转换成ZPL指令
    - 这里我去掉了代表标签开始的`^XA`，及代表图片坐标的`^FOX,Y`,和代表标签结尾的`^XZ`，以方便和其他ZPL指令拼接，你可以自己修改相关代码
```
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
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
    private ZPLConveter (){}

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
```

2. 使用java绘制标签图片，并拼接成可打印的ZPL指令返回
    - 使用`Graphics2D`类
    - 使用`getMyTag()`方法可以返回需要打印的完整ZPL指令

```java
public static String getMyTag(String type, String grade, String color, String rolls, String thickness, String grossWeight, String width, String length, String rollNO, String date) throws IOException {
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
        myDrawLine(g2d, 3,10, 160, 760, 160);
        myDrawLine(g2d, 3,10, 240, 760, 240);
        myDrawLine(g2d, 3,10, 320, 760, 320);
        myDrawLine(g2d, 3,10, 400, 760, 400);
        myDrawLine(g2d, 3,10, 480, 760, 480);
        myDrawLine(g2d, 3,10, 650, 760, 650);
        //按顺序的竖线
        myDrawLine(g2d, 3,10, 10, 10, 650);
        myDrawLine(g2d, 3,197, 160, 197, 480);
        myDrawLine(g2d, 3,384, 160, 384, 480);
        myDrawLine(g2d, 3,571, 160, 571, 480);
        myDrawLine(g2d, 3,760, 10, 760, 650);

        //按顺序的字
        //行3
        myDrawString(g2d, 20, new Font("宋体", Font.BOLD, 53), type, 210,230, 1);
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), grade, 640,230, 1);
        //行4
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), color, 210,310, 1);
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), rolls + " ROLL", 579,310, 0.89);
        //行5
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), thickness + "mic", 210,390, 0.89);
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), grossWeight + " KG", 579,390, 0.89);
        //行6
        myDrawString(g2d, 20, new Font("宋体", Font.PLAIN, 53), width + "mm", 210,470, 1);
        myDrawString(g2d, 20, new Font("宋体", Font.BOLD, 53), length + "M", 579,470, 0.89);

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
                        "^BY4,3,84^FT124,595^BCN,,Y,N\n" + //条码
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
```

3. 使用`Java`绘制标签的弊端

`Java`的`Graphics2D`类只能调整字体大小，无法把字体向X轴或者Y轴拉伸。

在上方DEMO画字符的方法中，通过rate参数可以调整字体间的间距，但如果调的太小会导致字符重叠在一起。

如何解决？

- 换其他方式绘制标签
- 使用小的字体
- 对于固定的表头数据，如果需要拉伸字体可以使用`zebra designer3`画出这些字，再导出ZPL，把这些ZPL拼接到打印方法中。这些导出的ZPL指令相当于是图片

### 条码 

条码有多种类型，这里以做常用的`CODE128`码为例。

##### 案例：使用ZPL打印标签

1. 画一个条码

```
^BY2.5,600,100
^FO25,480^BC^FD123456123456^FS
```

- `^BY2.5,600,100`：打印出一个宽度为2.5，高度为100的条码
    - `^BY`：控制标签的样式
    - `2.5`：标签的宽度，这个参数类似控制中文字体大小的那个参数，只能按比例放大或缩小标签，无法通过改变数字大小达到精准控制大小的目的。
    - `600`：没有实际含义，可随意填写
    - `100`：条码的长度
- `^FO25,480^BC^FD123456123456^FS`
    - `^F025,480`：设置左上角标签的坐标为(25,480)
    - `^BC`：表示使用`code128`编码，可以替换这个指令来输出不同编码格式的条码
    - `^FD123456123456`：将根据^FD后面的文字或数字生成条码
    - `^FS`：用来分割ZPL指令

2. 效果


##### 使用ZPL打印标签存在的问题

如上所说，我们无法通过改变`^BY`后面的数字达到精准控制标签宽度的需求（只能按比例放大标签）。对于某些样式的标签，单纯使用ZPL是不够的。这里提供两种思路

1. 先使用java或者其他语言生成标签图片，通过代码将次图片转换为ZPL命令，再把这个ZPL命令拼接到总的ZPL命令上
2. 使用Zebra Designer先画出图片，导出ZPL，将此ZPL代码拼接到主的ZPL代码中。你可以看到Zebra Designer生成的代码中有标签文字，替换该文字即可。

---

> 源码：https://github.com/btmood/zbreazpldemo



