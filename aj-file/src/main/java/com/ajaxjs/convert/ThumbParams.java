package com.ajaxjs.convert;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ThumbParams {
	/**
	 * 设置图片的大小，该设置项不能与 scala 同时存在
	 */
	private Size size;

	/**
	 * 设置图片的缩放，0.5 表示 50%，该设置项不能与 size 同时存在
	 */
	private Scale scale;

	/**
	 * 输出格式，默认为 jpg
	 */
	private String outputFormat;

	public Size getSize() {
		return size;
	}

	public ThumbParams setSize(Size size) {
		this.size = size;

		return this;
	}

	public Scale getScale() {
		return scale;
	}

	public ThumbParams setScale(Scale scale) {
		this.scale = scale;

		return this;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public ThumbParams setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;

		return this;
	}

	public static class Size {
		private Integer width;
		private Integer height;

		public Size() {
		}

		public Size(Integer width, Integer height) {
			this.width = width;
			this.height = height;
		}

		public Integer getWidth() {
			return width;
		}

		public Size setWidth(Integer width) {
			this.width = width;

			return this;
		}

		public Integer getHeight() {
			return height;
		}

		public Size setHeight(Integer height) {
			this.height = height;

			return this;
		}
	}

	public static class Scale {
		private Double width;
		private Double height;

		public Scale() {
		}

		public Scale(Double width, Double height) {
			this.width = width;
			this.height = height;
		}

		public Double getWidth() {
			return width;
		}

		public void setWidth(Double width) {
			this.width = width;
		}

		public Double getHeight() {
			return height;
		}

		public void setHeight(Double height) {
			this.height = height;
		}
	}
}
