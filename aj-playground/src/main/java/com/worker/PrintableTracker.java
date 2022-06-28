package com.worker;

/**
 * 打印抓取对象
 * 
 * 该对象是一个线程，并且是单实例。不停地从PrintQueue中取PrintJob，打印成功，继续打印下一个；否则，将当前的移到队列的尾部，继续打印下一个。如果队列中没有PrintJob了，则结束。当再次添加PrintJob到PrintQueue中的时候，会启动线程继续打印
 * 
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class PrintableTracker {

}
