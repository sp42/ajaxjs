/*
 *  Copyright 2013-2015 www.snakerflow.com.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.snaker.engine.impl;

import org.snaker.engine.Completion;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 默认的任务、实例完成时触发的动作
 * @author yuqs
 * @since 2.2.0
 */
public class GeneralCompletion implements Completion {
	public static final LogHelper LOGGER = LogHelper.getLog(GeneralCompletion.class);

	@Override
    public void complete(HistoryTask task) {
    	LOGGER.info("The task[{0}] has been user[{1}] has completed", task.getId(), task.getOperator());
    }

	@Override
    public void complete(HistoryOrder order) {
    	LOGGER.info("The order[{0}] has completed", order.getId());
    }
}
