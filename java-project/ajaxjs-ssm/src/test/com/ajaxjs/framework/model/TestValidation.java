package test.com.ajaxjs.framework.model;

import static org.junit.Assert.assertNotNull;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.bval.jsr.ApacheValidationProvider;
import org.junit.Test;

import test.com.ajaxjs.framework.business.News;

public class TestValidation {
	@Test
	public void testValid() {
		News news = new News();
		ValidatorFactory avf = Validation.byProvider(ApacheValidationProvider.class).configure().buildValidatorFactory();
		Validator validator = avf.getValidator();
		news.setName("dsdsa");
		news.setIntro("觉");
		Set<ConstraintViolation<News>> constraintViolations = validator.validate(news);

		for (ConstraintViolation<News> constraintViolation : constraintViolations) {
			System.out.println(constraintViolation.getMessage());
		}
		
		assertNotNull(constraintViolations);
	}
}
