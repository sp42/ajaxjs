package com.ajaxjs.export.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Style {
	private String id;
	private String parent;
	private String name;
	private Alignment alignment;
	private List<Border> borders;
	private Font font;
	private Interior interior;
	private NumberFormat numberFormat;
	private Protection protection;

	public Style(String id, Alignment alignment, List<Border> borders, Font font, Interior interior) {
		this.id = id;
		this.alignment = alignment;
		this.borders = borders;
		this.font = font;
		this.interior = interior;
	}

	public Style(String id, NumberFormat numberFormat) {
		this.id = id;
		this.numberFormat = numberFormat;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Alignment {
		private String horizontal;
		private String vertical;
		private String wrapText;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Border {
		private String position;
		private String linestyle;
		private int weight;
		private String color;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Font {
		private String fontName;
		private double size;
		private int bold;
		private String color;
		private Integer CharSet;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Interior {
		private String color;
		private String pattern;

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class NumberFormat {
		private String format;

	}

	// 权限修饰
	@Data
	@NoArgsConstructor
	public static class Protection {
		private String modifier;

	}

}
