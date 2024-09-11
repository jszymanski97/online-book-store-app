package mate.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CoverImageValidator implements ConstraintValidator<CoverImage, String> {
    private static final String PATTERN_OF_COVER_IMAGE = "([^\\s]+(\\.(?i)(jpeg|jpg))$)";

    @Override
    public boolean isValid(String coverImage,
                           ConstraintValidatorContext constraintValidatorContext) {
        return coverImage != null && Pattern.compile(PATTERN_OF_COVER_IMAGE)
                .matcher(coverImage).matches();
    }
}
