package demo.base.di;

import com.ajaxjs.util.ioc.Resource;

public class WhyUseInterface {
	interface 能说汉语的人 {
		public void speakChinese();
	}
	
	interface 能说英语的人 {
		public void speakEnglish();
	}
	
	interface 翻译 extends 能说汉语的人, 能说英语的人 {
	}
	
	interface 能说法语的人 {
		public void speakFrench();
	}

	class 英国人 implements 翻译 {
		@Override
		public void speakEnglish() {}

		@Override
		public void speakChinese() {}
	}
	
	class 法国人 implements 翻译, 能说法语的人 {
		@Override
		public void speakEnglish() {}

		@Override
		public void speakFrench() {}

		@Override
		public void speakChinese() {}
	}
	
	@Resource
	private 翻译 translator; // 注射翻译进来
	
	public void doTranslat() {
		translator.speakChinese();
		translator.speakEnglish();
	}
}
