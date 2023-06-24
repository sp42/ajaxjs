package com.ajaxjs.export.entity;

import lombok.Data;
//import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * @author 大脑补丁
 * @project freemarker-excel
 * @description: 合并单元格信息
 * @create 2020-04-14 16:54
 */
@Data
public class CellRangeAddressEntity {

//	private CellRangeAddress cellRangeAddress;

	private List<Style.Border> borders;

}
