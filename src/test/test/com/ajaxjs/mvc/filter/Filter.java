package test.com.ajaxjs.mvc.filter;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;

public class Filter implements FilterAction {

	@Override
	public boolean before(MvcRequest request, MvcOutput response, IController controller) {
		System.out.println("before");
		return true;
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, IController controller, boolean isSkip) {
		System.out.println("after");
		
	}

}
