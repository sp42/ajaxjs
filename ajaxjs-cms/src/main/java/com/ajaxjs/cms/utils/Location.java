package com.ajaxjs.cms.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Location {
	public static void main(String[] args) {
		List<LocalAddress> ilist = new ArrayList<>();
        Double localDouble  = 0.0;
    
        //定义一个二维数组存放经纬度
        Double[][] doubles = { { 31.1963877723, 121.4940175770 },
                { 31.2020280000, 121.5006010000 },
                { 31.1963702573, 121.4940084124 },
                { 31.1951946273, 121.4991236524 },
                { 31.1983746273, 121.4895436524 },
                { 31.2068062375, 121.4686363819 },
                { 31.2127140000, 121.4751610000 },
                { 31.2067706666, 121.4686028298 },
                { 31.2056732366, 121.4737227198 },
                { 31.2087332366, 121.4640927198 },
                { 31.2103126101, 121.4457401593 },
                { 31.2166680000, 121.4521640000 } };
        //门店的经纬度
        Double[] initlocal = {31.1221751783,121.5011213954 };

        for (int i = 0; i < doubles.length; i++) {
            Double z = getShortLocal(doubles[i][0], doubles[i][1], initlocal[0], initlocal[1]);        
            //获取最短距离后把经纬度和距离存放到对象中
            LocalAddress localaddress = new LocalAddress(doubles[i][0], doubles[i][1], z);
            //将对象用list保存
            ilist.add(localaddress);            
        }
        
        List<LocalAddress> shotlocal =  getLocalList(ilist);
        Double lat=shotlocal.get(0).getLat();
        Double lon= shotlocal.get(0).getLon();
        localDouble = shotlocal.get(0).getDistance();
        System.err.println("最近的距离：" + localDouble + "。对应的经纬是：" +"(" + lat + "," + lon + ")");
    }

	/**
	 * 获取最短的距离
	 * 
	 * @param lat
	 * @param lon
	 * @param initlat
	 * @param initlon
	 * @return
	 */
	public static Double getShortLocal(Double lat, Double lon, Double initlat, Double initlon) {
		Double x = (initlat - lat) * (initlat - lat);
		Double y = (initlon - lon) * (initlon - lon);
		Double z = Math.sqrt(x + y);
		return z;
	}

	/**
	 * 排序
	 * 
	 * @return
	 */
	public static List<LocalAddress> getLocalList(List<LocalAddress> ilist) {
		Collections.sort(ilist, new Comparator<LocalAddress>() {
			@Override
			public int compare(LocalAddress arg0, LocalAddress arg1) {
				Double double1 = arg0.getDistance();
				Double double2 = arg1.getDistance();
				if (double1 > double2) {
					return 1;
				} else if (double1 == double2) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		return ilist;
	}

	public static class LocalAddress {
		private Double lat;
		private Double lon;
		private Double distance;

		public LocalAddress(Double lat, Double lon, Double distance) {
			super();
			this.lat = lat;
			this.lon = lon;
			this.distance = distance;
		}

		public Double getLat() {
			return lat;
		}

		public void setLat(Double lat) {
			this.lat = lat;
		}

		public Double getLon() {
			return lon;
		}

		public void setLon(Double lon) {
			this.lon = lon;
		}

		public Double getDistance() {
			return distance;
		}

		public void setDistance(Double distance) {
			this.distance = distance;
		}
	}
}
