package com.ajaxjs.test.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.ajaxjs.util.Img;

public class TestImage {
	@Test
	public void 修改图片大小(){
		Img.setResize("c://temp//XMM.jpg", "c://temp//baitian---2.jpg", 20, 150);  
		
		assertNotNull(true);
	}
	
	@Test
	public void 图片裁切 (){
		Img.cut("c://temp//qz.jpg", "c://temp//qz_cut.jpg", 20, 150, 500, 500);  
		
		assertNotNull(true);
	}
	
	@Test
	public void 水印文字(){
		Img.mark("c://temp//XMM.jpg", "c://temp//baitian---.jpg",  
                "我们并肩坐在冰凉的台阶上，有风吹过，有爱来过。却最终被我们，一起错过。 原来，时间真的像流水，走得悄无声息。很多时光，很多事情，很多人，永远只能存在记忆里。");  
	
		assertNotNull(true);
	}
	
	@Test
	public void 水印图片(){
		Img.mark("c://temp//qz.jpg", "c://temp//baitian---3.jpg", new File("c://temp//XMM.jpg"));  
		
		assertNotNull(true);
	}
	@Test
	public void 截屏(){
//		Image.webscreenCut("c://temp//webscreenCut.jpg");  
		
		assertNotNull(true);
	}
}