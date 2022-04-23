package com.ajaxjs.net;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.junit.Test;

import com.ajaxjs.net.http.Post;

public class TestPost {
	@Test
	public void testPost() {
		String result = Post.post("http://localhost:8080/post", new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
				put("foo", "bar");
			}
		}).toString();

		result = Post.post("http://localhost:8080/post", "a=1&b=2&c=3").toString();
		assertNotNull(result);
	}
}
