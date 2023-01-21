package com.batch_read;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

//import org.apache.poi.openxml4j.opc.OPCPackage;
//import org.apache.poi.openxml4j.opc.PackageAccess;

/**
 * 用于读excel文件的FileParser，它在EnumerationEngineeFactory被调用并且它支持读超过几十万行的XLS文件
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class XLSEnumerationTask extends EnumerationEnginee {

	public XLSEnumerationTask(BlockingQueue<Map> queue, String txtFileName, String colNames, boolean skipHeader, BatchDTO taskContext) {
		super(queue, txtFileName, colNames, taskContext.isHeadSkip(), taskContext);
	}

	@Override
	public void enumerate(String fileName, String strKeys) throws Exception {
		File xlsxFile = new File(fileName);
		if (xlsxFile.exists()) {
//			// The package open is instantaneous, as it should be.
////			OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
			Map dataMap = new HashMap();
////			XLSXParser xlsxParser = new XLSXParser(p, queue, true);
//
////			xlsxParser.process();
		}
	}

	@Override
	public void run() {
		try {
			enumerate(super.fileName, super.colNames);
		} catch (Exception e) {
//			logger.error("read excel file error, parse excel quit because :" + e.getMessage(), e);
			try {
				Thread.interrupted();
			} catch (Exception ee) {
			}
		} finally {
			try {
				// queue.put(DUMMY);
				queue.put(DUMMY);
			} catch (Exception ex) {
			}
		}

	}

}