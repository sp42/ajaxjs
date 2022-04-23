//package com.ajaxjs.framework.address;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.ajaxjs.util.logger.LogHelper;
//
//
///**
// * 
// * 控制器
// */
//@RestController
//@RequestMapping("")
//public class AddressController {
//	private static final LogHelper LOGGER = LogHelper.getLog(AddressController.class);
//
//	@Autowired
//	private UserAddressService service;
//
//
//	/*
//	 * @POST
//	 * 
//	 * @MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
//	 * 
//	 * @Produces(MediaType.APPLICATION_JSON) public String
//	 * createUserAddress(@NotNull @HeaderParam(USER_ID) long userId, UserAddress
//	 * entity) { entity.setUserId(userId); return create(entity, service); }
//	 */
//	@POST
//	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
//	@Produces(MediaType.APPLICATION_JSON)
//	public String createUserAddress(@NotNull @BeanParam Address bean) {
//		bean.setOwner(LoginController.getUserId());
//		return create(bean);
//	}
//
//
//}
