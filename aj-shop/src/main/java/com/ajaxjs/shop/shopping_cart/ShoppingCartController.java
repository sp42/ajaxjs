package com.ajaxjs.shop.shopping_cart;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping_cart")
public interface ShoppingCartController extends ShoppingCartService {

}
