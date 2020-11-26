package com.ajaxjs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class TestMockito {
	@SuppressWarnings("rawtypes")
	@Test
	public void testStub() {
		List mock = mock(List.class);
		when(mock.get(0)).thenReturn(1);
		assertEquals("预期返回 1", 1, mock.get(0));// mock.get(0) 返回 1
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testArgumentMatchers() {
		List mock = mock(List.class);
		when(mock.get(anyInt())).thenReturn(888);
		assertEquals("预期返回 888", 888, mock.get(1));
		assertEquals("预期返回 888", 888, mock.get(99));
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testVoid() {
		List mock = mock(List.class);
		doNothing().when(mock).clear();
		mock.clear();
	}

	@Test
	public void testAnwser() {
		HttpServletRequest request = mock(HttpServletRequest.class);

		final Map<String, Object> hash = new HashMap<>();
		hash.put("errMsg", "Foo");

		Answer<Object> anwser = new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();// 执行方法的送入的参数
				return hash.get(args[0].toString());
			}
		};

		when(request.getAttribute("isRawOutput")).thenReturn(true);
		when(request.getAttribute("errMsg")).thenAnswer(anwser);
		when(request.getAttribute("msg")).thenAnswer(anwser);

		assertEquals(true, request.getAttribute("isRawOutput"));// 已给出实现，跳过 anwser
		assertEquals("Foo", request.getAttribute("errMsg"));// 经过 anwser 返回
		assertNull(request.getAttribute("msg")); // 没有给出实现
	}

	@Test
	public void testAnwserVoid() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		final Map<String, Object> hash = new HashMap<>();

		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				// Object mock = invocation.getMock();
				System.out.println(args[1]);
				hash.put(args[0].toString(), args[1]);
				return "called with arguments: " + args;
			}
		}).when(request).setAttribute(anyString(), anyString());

		request.setAttribute("isRawOutput", true);
		request.setAttribute("errMsg", "bar");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testVerify() {
		Map mock = mock(Map.class);

		mock.get("foo");
		verify(mock).get("foo");// 是否调用了一次？如果 times 不传入，则默认是 1
		mock.get("foo");
		mock.get("bar");
		verify(mock, times(2)).get("foo"); // 调用了两次
		verify(mock, times(3)).get(anyString()); // 不指定具体值，符合的类型即可

		when(mock.put(anyInt(), anyString())).thenReturn("world");
		mock.put(1, "hello");
		verify(mock).put(anyInt(), eq("hello"));

		verify(mock, times(2)).get(Matchers.eq("foo"));// 关注参数有否传入
	}

	@Test
	public void testSpy() {
		List<Integer> list = new ArrayList<>();
		List<Integer> spy = spy(list);

		when(spy.size()).thenReturn(100);
		assertEquals(100, spy.size());

		spy.add(0);
		spy.add(1);
		assertTrue(0 == spy.get(0));
		verify(spy).add(0);
		verify(spy).add(1);

		doReturn(99).when(spy).get(99);
		assertTrue(99 == spy.get(99));

	}
}
