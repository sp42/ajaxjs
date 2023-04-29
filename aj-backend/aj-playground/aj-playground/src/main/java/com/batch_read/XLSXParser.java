//package com.batch_read;
//
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.concurrent.BlockingQueue;
//
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
//import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
//import org.apache.poi.openxml4j.opc.OPCPackage;
//import org.apache.poi.openxml4j.opc.PackageAccess;
//import org.apache.poi.ss.usermodel.BuiltinFormats;
//import org.apache.poi.ss.usermodel.DataFormatter;
//import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
//import org.apache.poi.xssf.eventusermodel.XSSFReader;
//import org.apache.poi.xssf.model.StylesTable;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFRichTextString;
//
//import org.xml.sax.Attributes;
//import org.xml.sax.ContentHandler;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//import org.xml.sax.XMLReader;
//import org.xml.sax.helpers.DefaultHandler;
//
///**
// */
//public class XLSXParser {
////	private final Logger logger = LoggerFactory.getLogger(getClass());
//
//	/**
//	 * The type of the data value is indicated by an attribute on the cell. The
//	 * value is usually in a "v" element within the cell.
//	 */
//	enum xssfDataType {
//		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
//	}
//
//	int countrows = 0;
//
//	/**
//	 * Derived from http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api
//	 * <p/>
//	 * Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at
//	 * http://www.ecma-international.org/publications/standards/Ecma-376.htm
//	 * <p/>
//	 * A web-friendly version is http://openiso.org/Ecma/376/Part4
//	 */
//	class MyXSSFSheetHandler extends DefaultHandler {
//
//		/**
//		 * Table with styles
//		 */
//		private StylesTable stylesTable;
//		private Map<String, String> dataMap = new HashMap<String, String>();
//		/**
//		 * Table with unique strings
//		 */
//		private ReadOnlySharedStringsTable sharedStringsTable;
//
//		/**
//		 * Destination for data
//		 */
//		// private final PrintStream output;
//
//		/**
//		 * Number of columns to read starting with leftmost
//		 */
//		// private final int minColumnCount;
//
//		// Set when V start element is seen
//		private boolean vIsOpen;
//
//		// Set when cell start element is seen;
//		// used when cell close element is seen.
//		private xssfDataType nextDataType;
//
//		// Used to format numeric cell values.
//		private short formatIndex;
//		private String formatString;
//		private final DataFormatter formatter;
//
//		private int thisRow = 0;
//		private int thisColumn = -1;
//		// The last column printed to the output stream
//		private int lastColumnNumber = -1;
//
//		// Gathers characters as they are seen.
//		private StringBuffer value;
//
//		/**
//		 * Accepts objects needed while parsing.
//		 * 
//		 * @param styles  Table of styles
//		 * @param strings Table of shared strings
//		 * @param cols    Minimum number of columns to show
//		 * @param target  Sink for output
//		 */
//
//		public MyXSSFSheetHandler(StylesTable styles, ReadOnlySharedStringsTable strings, Map<String, String> dataMap) {
//			this.stylesTable = styles;
//			this.sharedStringsTable = strings;
//			// this.minColumnCount = cols;
//			this.value = new StringBuffer();
//			this.nextDataType = xssfDataType.NUMBER;
//			this.formatter = new DataFormatter();
//			this.dataMap = dataMap;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
//		 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
//		 */
//		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
//
//			if ("inlineStr".equals(name) || "v".equals(name)) {
//				vIsOpen = true;
//				// Clear contents cache
//				value.setLength(0);
//			}
//			// c => cell
//			else if ("c".equals(name)) {
//				// Get the cell reference
//				String r = attributes.getValue("r");
//				int firstDigit = -1;
//				for (int c = 0; c < r.length(); ++c) {
//					if (Character.isDigit(r.charAt(c))) {
//						firstDigit = c;
//						break;
//					}
//				}
//				thisColumn = nameToColumn(r.substring(0, firstDigit));
//
//				// Set up defaults.
//				this.nextDataType = xssfDataType.NUMBER;
//				this.formatIndex = -1;
//				this.formatString = null;
//				String cellType = attributes.getValue("t");
//				String cellStyleStr = attributes.getValue("s");
//				if ("b".equals(cellType))
//					nextDataType = xssfDataType.BOOL;
//				else if ("e".equals(cellType))
//					nextDataType = xssfDataType.ERROR;
//				else if ("inlineStr".equals(cellType))
//					nextDataType = xssfDataType.INLINESTR;
//				else if ("s".equals(cellType))
//					nextDataType = xssfDataType.SSTINDEX;
//				else if ("str".equals(cellType))
//					nextDataType = xssfDataType.FORMULA;
//				else if (cellStyleStr != null) {
//					// It's a number, but almost certainly one
//					// with a special style or format
//					int styleIndex = Integer.parseInt(cellStyleStr);
//					XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
//					this.formatIndex = style.getDataFormat();
//					this.formatString = style.getDataFormatString();
//					if (this.formatString == null)
//						this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
//				}
//			}
//
//		}
//
//		/**
//		 * 取值
//		 * 
//		 * @param str
//		 * @return
//		 */
//		public String checkNumber(String str) {
//			str = str.trim();
//			String str2 = "";
//			if (str != null && !"".equals(str)) {
//				for (int i = 0; i < str.length(); i++) {
//					if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
//						str2 += str.charAt(i);
//					}
//				}
//			}
//			return str2.trim();
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
//		 * java.lang.String, java.lang.String)
//		 */
//
//		public void endElement(String uri, String localName, String name) throws SAXException {
//			String thisStr = null;
//			// System.out.println("endElement----->" + name);
//			// v => contents of a cell
//			if ("v".equals(name)) {
//				// Process the value contents as required.
//				// Do now, as characters() may be called more than once
//				switch (nextDataType) {
//
//				case BOOL:
//					char first = value.charAt(0);
//					thisStr = first == '0' ? "FALSE" : "TRUE";
//					break;
//
//				case ERROR:
//					thisStr = "\"ERROR:" + value.toString() + '"';
//					break;
//
//				case FORMULA:
//					// A formula could result in a string value,
//					// so always add double-quote characters.
//					thisStr = '"' + value.toString() + '"';
//					break;
//
//				case INLINESTR:
//					// TODO: have seen an example of this, so it's untested.
//					XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
//					if (rtsi != null) {
//						thisStr = rtsi.toString().trim();
//						thisStr = thisStr.substring(1, thisStr.length() - 1);
//					}
//					break;
//
//				case SSTINDEX:
//					String sstIndex = value.toString();
//					try {
//						int idx = Integer.parseInt(sstIndex);
//						XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
//						if (rtss != null) {
//							/*
//							 * thisStr = rtss.toString().trim() .replaceAll("\\s*", "");
//							 */
//							thisStr = checkNumber(rtss.toString().trim());
//							/*
//							 * thisStr = thisStr .substring(1, thisStr.length() - 1);
//							 */
//						}
//					} catch (NumberFormatException ex) {
//						logger.error("Failed to parse SST index '" + sstIndex + "': " + ex.toString(), ex);
//					}
//					break;
//
//				case NUMBER:
//					String n = value.toString();
//					if (this.formatString != null)
//						thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
//					else
//						thisStr = n;
//					break;
//
//				default:
//					thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
//					break;
//				}
//
//				// Output after we've seen the string contents
//				// Emit commas for any fields that were missing on this row
//				if (lastColumnNumber == -1) {
//					lastColumnNumber = 0;
//				}
//				// for (int i = lastColumnNumber; i < thisColumn; ++i) {
//				// System.out.print(" col: " + i + " ");
//				// }
//				// Might be the empty string.
//				// output.print(thisStr);
//				// System.out.println(thisStr);
//				// System.out.println("thisRow...." + thisRow);
//				if (thisRow > 0 && thisStr != null && thisStr.trim().length() > 0) {
//					// logger.info("dataMap.put()");
//					dataMap.put(String.valueOf(thisColumn), thisStr);
//
//				}
//				// Update column
//				if (thisColumn > -1)
//					lastColumnNumber = thisColumn;
//
//			} else if ("row".equals(name)) {
//				try {
//					if (dataMap.keySet().size() > 0) {
//						dataMap = MapUtil.sortByValue(dataMap);
//						if (toQueue) {
//							queue.put(dataMap);
//						}
//					}
//				} catch (Exception e) {
//					logger.error("put data into queue error: " + e.getMessage(), e);
//				}
//				thisRow++;
//				dataMap = new HashMap<String, String>();
//				lastColumnNumber = -1;
//
//			}
//
//		}
//
//		/**
//		 * Captures characters only if a suitable element is open. Originally was just
//		 * "v"; extended for inlineStr also.
//		 */
//		public void characters(char[] ch, int start, int length) throws SAXException {
//			if (vIsOpen)
//				value.append(ch, start, length);
//		}
//
//		/**
//		 * Converts an Excel column name like "C" to a zero-based index.
//		 * 
//		 * @param name
//		 * @return Index corresponding to the specified name
//		 */
//		private int nameToColumn(String name) {
//			int column = -1;
//			for (int i = 0; i < name.length(); ++i) {
//				int c = name.charAt(i);
//				column = (column + 1) * 26 + c - 'A';
//			}
//			return column;
//		}
//
//	}
//
//	// /
//
//	private OPCPackage xlsxPackage;
//	private BlockingQueue<Map> queue = null;
//	private boolean toQueue = false;
//
//	// private int minColumns;
//
//	// private PrintStream output;
//
//	/**
//	 * Creates a new XLSX -> XML converter
//	 * 
//	 * @param pkg        The XLSX package to process
//	 * @param output     The PrintStream to output the CSV to
//	 * @param minColumns The minimum number of columns to output, or -1 for no
//	 *                   minimum
//	 */
//	public XLSXParser(OPCPackage pkg, BlockingQueue<Map> queue, boolean toQueue) {
//		this.xlsxPackage = pkg;
//		this.queue = queue;
//		this.toQueue = toQueue;
//		// this.minColumns = minColumns;
//	}
//
//	/**
//	 * Parses and shows the content of one sheet using the specified styles and
//	 * shared-strings tables.
//	 * 
//	 * @param styles
//	 * @param strings
//	 * @param sheetInputStream
//	 */
//	public void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream)
//			throws IOException, ParserConfigurationException, SAXException {
//
//		InputSource sheetSource = new InputSource(sheetInputStream);
//		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
//		SAXParser saxParser = saxFactory.newSAXParser();
//		XMLReader sheetParser = saxParser.getXMLReader();
//		Map<String, String> dataMap = new HashMap<String, String>();
//		ContentHandler handler = new MyXSSFSheetHandler(styles, strings, dataMap);
//		sheetParser.setContentHandler(handler);
//		sheetParser.parse(sheetSource);
//	}
//
//	/**
//	 * Initiates the processing of the XLS workbook file to CSV.
//	 * 
//	 * @throws IOException
//	 * @throws OpenXML4JException
//	 * @throws ParserConfigurationException
//	 * @throws SAXException
//	 */
//	public void process() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
//
//		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
//		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
//
//		StylesTable styles = xssfReader.getStylesTable();
//		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
//		int index = 0;
//		while (iter.hasNext()) {
//			InputStream stream = iter.next();
//			String sheetName = iter.getSheetName();
//			// System.out.println(sheetName + " [index=" + index + "]:");
//			processSheet(styles, strings, stream);
//			stream.close();
//			++index;
//		}
//	}
//
//	public static void main(String[] args) throws Exception {
//		/*
//		 * if (args.length < 1) { System.err.println("Use:");
//		 * System.err.println("  XLSX2CSV <xlsx file> [min columns]"); return; }
//		 */
//
//		// File xlsxFile = new File(args[0]);
//		File xlsxFile = new File("d:/test.xlsx");
//		if (!xlsxFile.exists()) {
//			System.err.println("Not found or not a file: " + xlsxFile.getPath());
//			return;
//		}
//
//		int minColumns = -1;
//		// if (args.length >= 2)
//		// minColumns = Integer.parseInt(args[1]);
//
//		minColumns = 2;
//		// The package open is instantaneous, as it should be.
//		OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ);
//		XLSXParser xlsxParser = new XLSXParser(p, null, false);
//		xlsxParser.process();
//	}
//
//}