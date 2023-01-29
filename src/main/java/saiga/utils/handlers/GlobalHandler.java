package saiga.utils.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.exceptions.TypesInError;

import static saiga.payload.MyResponse._ALREADY_EXISTS;
import static saiga.payload.MyResponse._BAD_REQUEST;
import static saiga.payload.MyResponse._ILLEGAL_TYPES;
import static saiga.payload.MyResponse._NOT_FOUND;

@ControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<?> handleExists(AlreadyExistsException e) {
        return _ALREADY_EXISTS.setMessage(e.getMessage()).handleResponse();
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException e) {
        return _NOT_FOUND.setMessage(e.getMessage()).handleResponse();
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<?> handleNotFound(NotFoundException e) {
        return _NOT_FOUND.setMessage(e.getMessage()).handleResponse();
    }

    @ExceptionHandler(value = {TypesInError.class})
    public ResponseEntity<?> handleIllegalTypes(TypesInError e) {
        return _ILLEGAL_TYPES.setMessage(e.getMessage()).handleResponse();
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        return new ResponseEntity<>(ex.getMessage(), headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        StringBuilder messages = new StringBuilder();
        methodArgumentNotValidException.getBindingResult()
                .getAllErrors().forEach(it -> messages.append(it.getDefaultMessage()).append(", "));

        return _BAD_REQUEST.setMessage(messages.substring(0, messages.length() - 2)).handleResponse();
    }
}
