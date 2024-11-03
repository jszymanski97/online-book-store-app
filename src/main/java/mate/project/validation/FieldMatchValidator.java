package mate.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String password;
    private String repeatPassword;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.repeatPassword = constraintAnnotation.repeatPassword();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(object);
        Object firstValue = beanWrapper.getPropertyValue(password);
        Object secondValue = beanWrapper.getPropertyValue(repeatPassword);
        if (firstValue == null && secondValue == null) {
            return false;
        }
        return Objects.equals(firstValue, secondValue);
    }
}
