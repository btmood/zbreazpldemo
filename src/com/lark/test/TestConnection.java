package com.lark.test;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;

public class TestConnection {

    //打印机的IP地址
    static String theIpAddress = "10.10.40.200";


    public static void main(String[] args) throws Exception {
        TestConnection testConnection = new TestConnection();
        testConnection.printZPL();
    }

    public void printZPL() throws Exception {
        //与打印机创建连接
        Connection thePrinterConn = new TcpConnection(theIpAddress, TcpConnection.DEFAULT_ZPL_TCP_PORT);
        try {
            thePrinterConn.open();
            ZebraPrinter zebraPrinter = ZebraPrinterFactory.getInstance(thePrinterConn);
            //发送打印命令，参数里的是ZPL名字字符串
            zebraPrinter.sendCommand(MyTestOnDiffElementZPL.getRectangle());
            zebraPrinter.sendCommand(MyTestOnDiffElementZPL.getLine());
            zebraPrinter.sendCommand(MyTestOnDiffElementZPL.getEnCharacter());
            zebraPrinter.sendCommand(MyTestOnDiffElementZPL.getCnCharacter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
