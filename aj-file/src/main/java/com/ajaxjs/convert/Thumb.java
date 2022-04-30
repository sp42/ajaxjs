package com.ajaxjs.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.map.MapTool;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.common.net.MediaType;

import net.coobird.thumbnailator.Thumbnails;

public class Thumb {
	public MediaType convert(File input, String params, File output) {
		ThumbParams convertParams = null;

		if (StringUtils.hasText(params)) {
			Map<String, Object> map = JsonHelper.parseMap(params);
			convertParams = MapTool.map2Bean(map, ThumbParams.class);
		} else
			convertParams = new ThumbParams().setOutputFormat("jpg");

		try (FileInputStream inputStream = new FileInputStream(input)) {
			Thumbnails.Builder<InputStream> builder = Thumbnails.fromInputStreams(Collections.singletonList(inputStream));

			if (convertParams.getSize() != null) {
				ThumbParams.Size size = convertParams.getSize();

				if (size.getWidth() != null && size.getHeight() != null)
					builder.size(size.getWidth(), size.getHeight());

			} else if (convertParams.getScale() != null) {
				ThumbParams.Scale scale = convertParams.getScale();
				if (scale.getWidth() != null && scale.getHeight() != null)
					builder.scale(scale.getWidth(), scale.getHeight());

			}

			if (StringUtils.hasText(convertParams.getOutputFormat()))
				builder.outputFormat(convertParams.getOutputFormat());

			try (@SuppressWarnings("deprecation")
			HashingOutputStream outputStream = new HashingOutputStream(Hashing.md5(), new FileOutputStream(output))) {
				builder.toOutputStream(outputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return MediaType.parse("image/" + convertParams.getOutputFormat());
	}
}
