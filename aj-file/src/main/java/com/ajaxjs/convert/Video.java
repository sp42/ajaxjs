package com.ajaxjs.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.ajaxjs.util.logger.LogHelper;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteStreams;
import com.google.common.net.MediaType;

import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.job.FFmpegJob.State;

/**
 * 视频转换	
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Video {
	private static final LogHelper LOGGER = LogHelper.getLog(Video.class);

	/**
	 * 
	 */
	private FFmpegExecutor executor;

	public MediaType convert(File input, File output) {
		FFmpegBuilder builder = new FFmpegBuilder().setInput(input.getAbsolutePath())
				.addOutput(new FFmpegOutputBuilder().setFilename(output.getAbsolutePath()).addExtraArgs("-r", "24").setFormat("mp4"));

		FFmpegJob exeJob = executor.createJob(builder, progress -> LOGGER.info(progress));
		exeJob.run();

		if (!Objects.equals(exeJob.getState(), State.FINISHED))
			throw new RuntimeException("Ffmpeg job not finished, state=" + exeJob.getState().name());

		@SuppressWarnings("deprecation")
		Hasher hasher = Hashing.md5().newHasher();

		try (InputStream in = new FileInputStream(output)) {
			ByteStreams.readBytes(in, new ByteProcessor<Object>() {
				@Override
				public boolean processBytes(byte[] buf, int off, int len) throws IOException {
					hasher.putBytes(buf, off, len);

					return true;
				}

				@Override
				public Object getResult() {
					return null;
				}
			});
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return MediaType.parse("video/mp4");
	}
}
