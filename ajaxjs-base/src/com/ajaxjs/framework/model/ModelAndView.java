/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.framework.model;

import java.util.HashMap;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.bval.jsr.ApacheValidationProvider;

public class ModelAndView extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	// BVal 与 JSR 接口结合，返回 ValidatorFactory 工厂
	private final static ValidatorFactory avf = Validation.byProvider(ApacheValidationProvider.class).configure().buildValidatorFactory();

	/**
	 * BVal 与 JSR 接口结合，返回 ValidatorFactory 工厂
	 * @return
	 */
	public static Validator getValidator() {
		return avf.getValidator();
	}
}
