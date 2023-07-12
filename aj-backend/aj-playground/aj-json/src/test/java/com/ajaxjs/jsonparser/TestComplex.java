//package com.ajaxjs.jsonparser;
//
//import com.ajaxjs.framework.IBaseModel;
//import com.ajaxjs.util.map.JsonHelper;
//import org.junit.Test;
//
//import java.util.List;
//
///**
// * 复杂的 map 转换到 bean
// *
// * @author xinzhang
// *
// */
//public class TestComplex {
//	// @formatter:off
//		String json ="{\n" +
// 		"	\"auxPoints\" : null,\n" +
// 		"	\"geoCode\" : null,\n" +
// 		"	\"geoId\" : null,\n" +
// 		"	\"geoName\" : \"低空侦察区域\",\n" +
// 		"	\"geoType\" : \"AREA\",\n" +
// 		"	\"points\" : [\n" +
// 		"		{\n" +
// 		"			\"altitude\" : null,\n" +
// 		"			\"distance\" : null,\n" +
// 		"			\"latitude\" : 39.921140,\n" +
// 		"			\"longitude\" : 116.425570,\n" +
// 		"			\"payload\" : null,\n" +
// 		"			\"radius\" : null,\n" +
// 		"			\"speed\" : null,\n" +
// 		"			\"turnMode\" : \"Incision\",\n" +
// 		"			\"visited\" : null\n" +
// 		"		},\n" +
// 		"		{\n" +
// 		"			\"altitude\" : null,\n" +
// 		"			\"distance\" : null,\n" +
// 		"			\"latitude\" : 39.941220,\n" +
// 		"			\"longitude\" : 116.479996,\n" +
// 		"			\"payload\" : null,\n" +
// 		"			\"radius\" : null,\n" +
// 		"			\"speed\" : null,\n" +
// 		"			\"turnMode\" : \"Incision\",\n" +
// 		"			\"visited\" : null\n" +
// 		"		},\n" +
// 		"		{\n" +
// 		"			\"altitude\" : null,\n" +
// 		"			\"distance\" : null,\n" +
// 		"			\"latitude\" : 39.921677,\n" +
// 		"			\"longitude\" : 116.519440,\n" +
// 		"			\"payload\" : null,\n" +
// 		"			\"radius\" : null,\n" +
// 		"			\"speed\" : null,\n" +
// 		"			\"turnMode\" : \"Incision\",\n" +
// 		"			\"visited\" : null\n" +
// 		"		},\n" +
// 		"		{\n" +
// 		"			\"altitude\" : null,\n" +
// 		"			\"distance\" : null,\n" +
// 		"			\"latitude\" : 39.870193,\n" +
// 		"			\"longitude\" : 116.477130,\n" +
// 		"			\"payload\" : null,\n" +
// 		"			\"radius\" : null,\n" +
// 		"			\"speed\" : null,\n" +
// 		"			\"turnMode\" : \"Incision\",\n" +
// 		"			\"visited\" : null\n" +
// 		"		},\n" +
// 		"		{\n" +
// 		"			\"altitude\" : null,\n" +
// 		"			\"distance\" : null,\n" +
// 		"			\"latitude\" : 39.856464,\n" +
// 		"			\"longitude\" : 116.433914,\n" +
// 		"			\"payload\" : null,\n" +
// 		"			\"radius\" : null,\n" +
// 		"			\"speed\" : null,\n" +
// 		"			\"turnMode\" : \"Incision\",\n" +
// 		"			\"visited\" : null\n" +
// 		"		},\n" +
// 		"		{\n" +
// 		"			\"altitude\" : null,\n" +
// 		"			\"distance\" : null,\n" +
// 		"			\"latitude\" : 39.880264,\n" +
// 		"			\"longitude\" : 116.409650,\n" +
// 		"			\"payload\" : null,\n" +
// 		"			\"radius\" : null,\n" +
// 		"			\"speed\" : null,\n" +
// 		"			\"turnMode\" : \"Incision\",\n" +
// 		"			\"visited\" : null\n" +
// 		"		},\n" +
// 		"		{\n" +
// 		"			\"altitude\" : null,\n" +
// 		"			\"distance\" : null,\n" +
// 		"			\"latitude\" : 39.892100,\n" +
// 		"			\"longitude\" : 116.411370,\n" +
// 		"			\"payload\" : null,\n" +
// 		"			\"radius\" : null,\n" +
// 		"			\"speed\" : null,\n" +
// 		"			\"turnMode\" : \"Incision\",\n" +
// 		"			\"visited\" : null\n" +
// 		"		},\n" +
// 		"		{\n" +
// 		"			\"altitude\" : null,\n" +
// 		"			\"distance\" : null,\n" +
// 		"			\"latitude\" : 39.902237,\n" +
// 		"			\"longitude\" : 116.412735,\n" +
// 		"			\"payload\" : null,\n" +
// 		"			\"radius\" : null,\n" +
// 		"			\"speed\" : null,\n" +
// 		"			\"turnMode\" : \"Incision\",\n" +
// 		"			\"visited\" : null\n" +
// 		"		}\n" +
// 		"	]\n" +
// 		"}";
////@formatter:on
//
//	@Test
//	public void testComplex() {
//		Geo geo = JsonHelper.parseMapAsBean(json, Geo.class);
//		System.out.println(geo.getPoints().get(0).getClass());
//	}
//
//	public static class Geo implements IBaseModel {
//		/**
//		 * 信息 Id
//		 */
//		private String geoId;
//
//		/**
//		 * 信息名称，人眼可读
//		 *
//		 */
//		private String geoName;
//
//		/**
//		 * 编号
//		 */
//		private String geoCode;
//
//		/**
//		 * 定义
//		 */
//		private List<GeoPoint> points;
//
//		/**
//		 * 辅助点，用于子区域中间线
//		 */
//		private List<GeoPoint> auxPoints;
//
//		public String getGeoId() {
//			return geoId;
//		}
//
//		public void setGeoId(String geoId) {
//			this.geoId = geoId;
//		}
//
//		public String getGeoName() {
//			return geoName;
//		}
//
//		public void setGeoName(String geoName) {
//			this.geoName = geoName;
//		}
//
//		public List<GeoPoint> getPoints() {
//			return points;
//		}
//
//		public void setPoints(List<GeoPoint> points) {
//			this.points = points;
//		}
//
//		public List<GeoPoint> getAuxPoints() {
//			return auxPoints;
//		}
//
//		public void setAuxPoints(List<GeoPoint> auxPoints) {
//			this.auxPoints = auxPoints;
//		}
//
//		public String getGeoCode() {
//			return geoCode;
//		}
//
//		public void setGeoCode(String geoCode) {
//			this.geoCode = geoCode;
//		}
//
//	}
//
//	/**
//	 * 仅表示一个点
//	 *
//	 * @author Frank Cheung sp42@qq.com
//	 *
//	 */
//	public static class GeoPoint implements IBaseModel {
//		/**
//		 * 经度
//		 */
//		private Float longitude;
//
//		/**
//		 * 维度
//		 */
//		private Float latitude;
//
//		/**
//		 * 海拔
//		 */
//		private Float altitude;
//
//		public Float getLongitude() {
//			return longitude;
//		}
//
//		public void setLongitude(Float longitude) {
//			this.longitude = longitude;
//		}
//
//		public Float getLatitude() {
//			return latitude;
//		}
//
//		public void setLatitude(Float latitude) {
//			this.latitude = latitude;
//		}
//
//		public Float getAltitude() {
//			return altitude;
//		}
//
//		public void setAltitude(Float altitude) {
//			this.altitude = altitude;
//		}
//	}
//
//}
