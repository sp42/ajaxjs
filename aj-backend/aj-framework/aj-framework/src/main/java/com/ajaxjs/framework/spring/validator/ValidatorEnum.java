package com.ajaxjs.framework.spring.validator;

import org.springframework.util.StringUtils;

/**
 * @author volicy.xu
 */
public enum ValidatorEnum {
    IdCard {
        @Override
        public void validated(Object value, String errorMsg) {
            if (value == null || !ValidatorHelper.isIDCard(value.toString()))
                throw new ValidatorException(errorMsg);
        }
    },
    UserMail {
        @Override
        public void validated(Object value, String errorMsg) {
            if (value == null || !ValidatorHelper.isEmail(value.toString()))
                throw new ValidatorException(errorMsg);
        }
    },
    NotBank {
        @Override
        public void validated(Object value, String errorMsg) {
            if (value == null || !StringUtils.hasText(value.toString()))
                throw new ValidatorException(errorMsg);
        }
    },
    NotNull {
        @Override
        public void validated(Object value, String errorMsg) {
            if (value == null || !StringUtils.hasText(value.toString()))
                throw new ValidatorException(errorMsg);
        }
    },
    MobileNo {
        @Override
        public void validated(Object value, String errorMsg) {
            if (value == null || !ValidatorHelper.isMobile(value.toString()))
                throw new ValidatorException(errorMsg);
        }
    };

    public static ValidatorEnum getInstance(String annotationName) {
        ValidatorEnum[] values = ValidatorEnum.values();

        for (ValidatorEnum validatorEnum : values) {
            if (validatorEnum.name().equals(annotationName))
                return validatorEnum;
        }

        return null;
    }

    public abstract void validated(Object value, String errorMsg);
}
