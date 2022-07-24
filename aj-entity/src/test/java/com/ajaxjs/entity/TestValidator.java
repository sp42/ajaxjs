package com.ajaxjs.entity;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import org.apache.bval.jsr.ApacheValidationProvider;
import org.junit.Test;

import com.ajaxjs.entity.enum_validation.EnumNamePattern;

public class TestValidator {
	enum Type {
		NEWS, SHOP, BOOk;
	}

	class Bean {
		private String name;

		@NotNull
		@EnumNamePattern(regexp = "NEWS|SHOP")
		private Type type;

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Test
	public void test() {
		ValidatorFactory avf = Validation.byProvider(ApacheValidationProvider.class).configure().buildValidatorFactory();
		Validator validator = avf.getValidator();

		Bean bean = new Bean();
		bean.setName("Hi");
		bean.setType(Type.BOOk);

		Set<ConstraintViolation<Bean>> constraint = validator.validate(bean);

		for (ConstraintViolation<Bean> violation : constraint) {
			System.out.println(violation.getPropertyPath() + " : " + violation.getMessage());
		}
	}
}
