package saiga.utils.exceptions;

public class TypesInError extends RuntimeException {
    public TypesInError(String title_or_description_is_null) {
        super(title_or_description_is_null);
    }
}
