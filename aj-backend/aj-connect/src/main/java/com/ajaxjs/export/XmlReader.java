package com.ajaxjs.export;

import com.ajaxjs.export.entity.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 读取 XML 文件工具
 */
public class XmlReader {

    // 获取样式
    @SuppressWarnings("rawtypes")
    public static Map<String, Style> getStyle(Document document) {
        // 创建一个LinkedHashMap用于存放style，按照id查找
        Map<String, Style> styleMap = new LinkedHashMap<>();
        // 新建一个Style类用于存放节点数据
        Style style;
        // 获取根节点
        Element root = document.getRootElement();
        // 获取根节点下的Styles节点
        Element styles = root.element("Styles");
        // 获取Styles下的Style节点
        List styleList = styles.elements("Style");

        for (Object o : styleList) {
            // 新建一个Style类用于存放节点数据
            style = new Style();
            Element e = (Element) o;
            String id = e.attributeValue("ID");
            // 设置style的id
            style.setId(id);
            if (e.attributeValue("Name") != null) {
                String name = e.attributeValue("Name");
                // 设置style的name
                style.setName(name);
            }

            // 获取Style下的NumberFormat节点
            Element eNumberFormat = e.element("NumberFormat");
            if (eNumberFormat != null) {
                Style.NumberFormat numberFormat = new Style.NumberFormat();
                numberFormat.setFormat(eNumberFormat.attributeValue("Format"));
                style.setNumberFormat(numberFormat);
            }

            Style.Alignment alignment = new Style.Alignment();
            // 获取Style下的Alignment节点
            Element eAlignment = e.element("Alignment");

            if (eAlignment != null) {
                // 设置 alignment 的相关属性，并且设置style的aliment属性
                alignment.setHorizontal(eAlignment.attributeValue("Horizontal"));
                alignment.setVertical(eAlignment.attributeValue("Vertical"));
                alignment.setWrapText(eAlignment.attributeValue("WrapText"));
                style.setAlignment(alignment);
            }

            // 获取Style下的Borders节点
            Element Borders = e.element("Borders");

            if (Borders != null) {
                // 获取Borders下的Border节点
                List Border = Borders.elements("Border");
                // 用迭代器遍历Border节点
                Iterator<?> borderIterator = Border.iterator();
                List<Style.Border> lborders = new ArrayList<>();

                while (borderIterator.hasNext()) {
                    Element bd = (Element) borderIterator.next();
                    Style.Border border = new Style.Border();
                    border.setPosition(bd.attributeValue("Position"));

                    if (bd.attribute("LineStyle") != null) {
                        border.setLinestyle(bd.attributeValue("LineStyle"));
                        int weight = Integer.parseInt(bd.attributeValue("Weight"));
                        border.setWeight(weight);
                        border.setColor(bd.attributeValue("Color"));
                    }

                    lborders.add(border);
                }

                style.setBorders(lborders);
            }

            // 设置font的相关属性，并且设置style的font属性
            Style.Font font = new Style.Font();
            Element eFont = e.element("Font");
            font.setFontName(eFont.attributeValue("FontName"));

            if (eFont.attributeValue("Size") != null) {
                double size = Double.parseDouble(eFont.attributeValue("Size"));
                font.setSize(size);
            }

            if (eFont.attribute("Bold") != null) {
                int bold = Integer.parseInt(eFont.attributeValue("Bold"));
                font.setBold(bold);
            }

            font.setColor(eFont.attributeValue("Color"));
            style.setFont(font);
            // 设置Interior的相关属性，并且设置style的interior属性
            Style.Interior interior = new Style.Interior();

            if (e.element("Interior") != null) {
                Element einterior = e.element("Interior");
                interior.setColor(einterior.attributeValue("Color"));
                interior.setPattern(einterior.attributeValue("Pattern"));
            }

            style.setInterior(interior);

            if (e.element("Protection") != null) {
                Element protectione = e.element("Protection");
                Style.Protection protection = new Style.Protection();
                protection.setModifier(protectione.attributeValue("Protected"));
                style.setProtection(protection);
            }

            styleMap.put(id, style);
        }

        return styleMap;
    }

    @SuppressWarnings("unchecked")
    public static List<Worksheet> getWorksheet(Document document) {
        List<Worksheet> worksheets = new ArrayList<>();
        Element root = document.getRootElement();
        // 读取根节点下的Worksheet节点
        List<Element> sheets = root.elements("Worksheet");

        if (CollectionUtils.isEmpty(sheets))
            return worksheets;


        for (Element sheet : sheets) {
            Worksheet worksheet = new Worksheet();
            String name = sheet.attributeValue("Name");
            worksheet.setName(name);
            Table table = getTable(sheet);
            worksheet.setTable(table);
            worksheets.add(worksheet);
        }

        return worksheets;
    }

    private static Table getTable(Element sheet) {
        Element tableElement = sheet.element("Table");
        if (tableElement == null)
            return null;

        Table table = new Table();
        String expandedColumnCount = tableElement.attributeValue("ExpandedColumnCount");
        if (expandedColumnCount != null)
            table.setExpandedColumnCount(Integer.parseInt(expandedColumnCount));

        String expandedRowCount = tableElement.attributeValue("ExpandedRowCount");
        if (expandedRowCount != null)
            table.setExpandedRowCount(Integer.parseInt(expandedRowCount));

        String fullColumns = tableElement.attributeValue("FullColumns");
        if (fullColumns != null)
            table.setFullColumns(Integer.parseInt(fullColumns));


        String fullRows = tableElement.attributeValue("FullRows");
        if (fullRows != null)
            table.setFullRows(Integer.parseInt(fullRows));

        String defaultColumnWidth = tableElement.attributeValue("DefaultColumnWidth");
        if (defaultColumnWidth != null)
            table.setDefaultColumnWidth(Double.valueOf(defaultColumnWidth).intValue());


        String defaultRowHeight = tableElement.attributeValue("DefaultRowHeight");
        if (defaultRowHeight != null)
            table.setDefaultRowHeight(Double.valueOf(defaultRowHeight).intValue());

        // 读取列
        List<Column> columns = getColumns(tableElement, expandedColumnCount, defaultColumnWidth);
        table.setColumns(columns);

        // 读取行
        List<Row> rows = getRows(tableElement);
        table.setRows(rows);
        return table;
    }

    @SuppressWarnings("unchecked")
    private static List<Row> getRows(Element tableElement) {
        List<Element> rowElements = tableElement.elements("Row");
        if (CollectionUtils.isEmpty(rowElements))
            return null;

        List<Row> rows = new ArrayList<>();
        for (Element rowElement : rowElements) {
            Row row = new Row();
            String height = rowElement.attributeValue("Height");
            if (height != null)
                row.setHeight(Double.valueOf(height).intValue());

            String index = rowElement.attributeValue("Index");
            if (index != null)
                row.setIndex(Integer.valueOf(index));

            List<Cell> cells = getCells(rowElement);
            row.setCells(cells);
            rows.add(row);
        }
        return rows;
    }

    @SuppressWarnings("unchecked")
    private static List<Cell> getCells(Element rowElement) {
        List<Element> cellElements = rowElement.elements("Cell");
        if (CollectionUtils.isEmpty(cellElements))
            return null;

        List<Cell> cells = new ArrayList<>();
        for (Element cellElement : cellElements) {
            Cell cell = new Cell();
            String styleID = cellElement.attributeValue("StyleID");
            if (styleID != null)
                cell.setStyleID(styleID);

            String mergeAcross = cellElement.attributeValue("MergeAcross");
            if (mergeAcross != null) {
                cell.setMergeAcross(Integer.valueOf(mergeAcross));
            }

            String mergeDown = cellElement.attributeValue("MergeDown");
            if (mergeDown != null)
                cell.setMergeDown(Integer.valueOf(mergeDown));


            String index = cellElement.attributeValue("Index");
            if (index != null)
                cell.setIndex(Integer.valueOf(index));

            Element commentElement = cellElement.element("Comment");
            if (commentElement != null) {
                Comment comment = new Comment();
                String author = commentElement.attributeValue("Author");
                Element fontElement = commentElement.element("Font");
                Element dataElement = commentElement.element("Data");

                if (dataElement != null) {
                    Data data = new Data();
                    data.setText(dataElement.getStringValue());
                    comment.setData(data);
                }

                if (fontElement != null) {
                    Font font = new Font();
                    font.setText(fontElement.getText());
                    font.setBold(1);
                    String color = fontElement.attributeValue("Color");
                    if (color != null)
                        font.setColor(color);

                    comment.setFont(font);
                }

                comment.setAuthor(author);
                cell.setComment(comment);
            }

            Element dataElement = cellElement.element("Data");
            if (dataElement != null) {
                Data data = new Data();
                String type = dataElement.attributeValue("Type");
                String xmlns = dataElement.attributeValue("xmlns");
                data.setType(type);
                data.setXmlns(xmlns);
                data.setText(dataElement.getText());
                Element bElement = dataElement.element("B");
                Integer bold = null;
                Element fontElement = null;
                if (bElement != null) {
                    fontElement = bElement.element("Font");
                    bold = 1;
                }

                Element uElement = dataElement.element("U");

                if (uElement != null)
                    fontElement = uElement.element("Font");

                if (fontElement == null)
                    fontElement = dataElement.element("Font");

                if (fontElement != null) {
                    Font font = new Font();
                    String face = fontElement.attributeValue("Face");
                    if (face != null)
                        font.setFace(face);

                    String charSet = fontElement.attributeValue("CharSet");
                    if (charSet != null)
                        font.setCharSet(charSet);

                    String color = fontElement.attributeValue("Color");
                    if (color != null) {
                        font.setColor(color);
                    }
                    if (bold != null) {
                        font.setBold(bold);
                    }
                    font.setText(fontElement.getText());
                    data.setFont(font);
                }

                cell.setData(data);
            }
            cells.add(cell);
        }
        return cells;
    }

    @SuppressWarnings("unchecked")
    private static List<Column> getColumns(Element tableElement, String expandedRowCount, String defaultColumnWidth) {
        List<Element> columnElements = tableElement.elements("Column");
        if (CollectionUtils.isEmpty(columnElements)) {
            return null;
        }
        if (ObjectUtils.isEmpty(expandedRowCount)) {
            return null;
        }
        int defaultWidth = 60;
        if (!ObjectUtils.isEmpty(defaultColumnWidth))
            defaultWidth = Double.valueOf(defaultColumnWidth).intValue();

        List<Column> columns = new ArrayList<>();
        int indexNum = 0;

        for (Element element : columnElements) {
            Column column = new Column();
            String index = element.attributeValue("Index");

            if (index != null) {
                if (indexNum < Integer.parseInt(index) - 1) {
                    for (int j = indexNum; j < Integer.parseInt(index) - 1; j++) {
                        column = new Column();
                        column.setIndex(indexNum);
                        column.setWidth(defaultWidth);
                        columns.add(column);
                        indexNum += 1;
                    }
                }

                column = new Column();
            }

            column.setIndex(indexNum);
            String autoFitWidth = element.attributeValue("AutoFitWidth");

            if (autoFitWidth != null)
                column.setAutofitwidth(Double.valueOf(autoFitWidth).intValue());

            String width = element.attributeValue("Width");
            if (width != null)
                column.setWidth(Double.valueOf(width).intValue());

            columns.add(column);
            indexNum += 1;
        }

        if (columns.size() < Integer.parseInt(expandedRowCount)) {
            for (int i = columns.size() + 1; i <= Integer.parseInt(expandedRowCount); i++) {
                Column column = new Column();
                column.setIndex(i);
                column.setWidth(defaultWidth);
                columns.add(column);
            }
        }

        return columns;
    }
}
