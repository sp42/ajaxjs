/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import java.io.PrintWriter;

/**
 * Allow application specific information summary to take advantage of
 * application convention
 * 
 * @author xrao
 *
 */
interface ProcessListEntryProcessor {
	void processEntry(ProcessListEntry e);

	void dumpSummary(PrintWriter pw);
}
