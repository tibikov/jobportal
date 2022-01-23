package org.demo.constraint;

import java.util.UUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class UUIDValidator implements ConstraintValidator<UUIDConstraint, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			if(value != null) UUID.fromString(value);
			return true;	
		} catch(Exception e) {			
		}
		return false;
	}

}
