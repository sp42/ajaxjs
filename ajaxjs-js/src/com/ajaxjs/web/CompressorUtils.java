package com.ajaxjs.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * JS、CSS压缩工具 https://blog.csdn.net/jianggujin/article/details/80202559
 * 
 * @author jianggujin
 *
 */
public class CompressorUtils {

	public void compressJS(File js, Writer out) throws Exception {
		compressJS(js, out, -1, true, true, false, false);
	}

	public void compressJS(File js, Writer out, int linebreakpos, boolean munge, boolean verbose, boolean preserveAllSemiColons, boolean disableOptimizations) throws IOException {
		try (InputStreamReader in = new InputStreamReader(new FileInputStream(js), "UTF-8");) {
			JavaScriptCompressor compressor = new JavaScriptCompressor(in, new ErrorReporter() {
				@Override
				public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
					System.err.println("[ERROR] in " + js.getAbsolutePath() + line + ':' + lineOffset + ':' + message);
				}

				@Override
				public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
					System.err.println("[ERROR] in " + js.getAbsolutePath() + line + ':' + lineOffset + ':' + message);
				}

				@Override
				public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
					error(message, sourceName, line, lineSource, lineOffset);
					return new EvaluatorException(message);
				}
			});

			compressor.compress(out, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
		}
	}

	public void compressCSS(File css, Writer out) throws Exception {
		compressCSS(css, out, -1);
	}

	public void compressCSS(File css, Writer out, int linebreakpos) throws IOException {
		try (InputStreamReader in = new InputStreamReader(new FileInputStream(css), "UTF-8");) {
			CssCompressor compressor = new CssCompressor(in);

			compressor.compress(out, linebreakpos);
		}
	}
}