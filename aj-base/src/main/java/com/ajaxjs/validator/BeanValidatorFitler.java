package com.ajaxjs.validator;

import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

public class BeanValidatorFitler implements FilterAction {

	@Override
	public boolean before(FilterContext ctx) {
		return true;
	}

	@Override
	public boolean after(FilterAfterArgs ctx) {
		return true;
	}

}
