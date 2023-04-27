package saiga.utils.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import saiga.config.SecurityConf;
import saiga.service.telegram.TgMainService;
import saiga.utils.exceptions.AlreadyExistsException;
import saiga.utils.exceptions.BadRequestException;
import saiga.utils.exceptions.NotFoundException;
import saiga.utils.exceptions.TypesInError;
import saiga.utils.statics.MessageResourceHelperFunction;

import javax.validation.ConstraintViolationException;
import java.net.UnknownHostException;
import java.nio.file.AccessDeniedException;
import java.util.logging.Logger;

import static saiga.payload.MyResponse._ALREADY_EXISTS;
import static saiga.payload.MyResponse._BAD_REQUEST;
import static saiga.payload.MyResponse._ILLEGAL_TYPES;
import static saiga.payload.MyResponse._NOT_FOUND;

@ControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    private final MessageResourceHelperFunction messageResourceHelper;
    private final TgMainService tgMainService;

    private final java.util.logging.Logger logger = Logger.getLogger(SecurityConf.class.getName());

    @Autowired
    public GlobalHandler(MessageResourceHelperFunction messageResourceHelper, TgMainService tgMainService) {
        this.messageResourceHelper = messageResourceHelper;
        this.tgMainService = tgMainService;
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<?> handleExists(AlreadyExistsException e) {
        return _ALREADY_EXISTS().setMessage(e.getMessage()).handleResponse();
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException e) {
        return _NOT_FOUND().setMessage(e.getMessage()).handleResponse();
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<?> handleNotFound(NotFoundException e) {
        return _NOT_FOUND().setMessage(e.getMessage()).handleResponse();
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<?> handleBadRequest(BadRequestException e) {
        return _BAD_REQUEST().setMessage(e.getMessage()).handleResponse();
    }

    @ExceptionHandler(value = {TypesInError.class})
    public ResponseEntity<?> handleIllegalTypes(TypesInError e) {
        return _ILLEGAL_TYPES().setMessage(e.getMessage()).handleResponse();
    }

    @ExceptionHandler(value = {UnknownHostException.class})
    public void handleHostError (UnknownHostException e) {
        logger.warning(String.format(
                "Can't send message to client %s!",
                e.getMessage()
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public HttpEntity<?> onConstraintValidationException(ConstraintViolationException e) {
        return _BAD_REQUEST().setMessage(messageResourceHelper.apply(e.getMessage())).handleResponse();
    }

    @ExceptionHandler(value = {AccessDeniedException.class, AuthenticationException.class, AuthenticationException.class})
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException e) {
        return _BAD_REQUEST().setMessage(e.getMessage()).handleResponse();
    }

    @ExceptionHandler(value = {RuntimeException.class, Exception.class, Error.class})
    public ResponseEntity<?> handleErrors(Exception e) {
        tgMainService.sendErrorMessage(e.getMessage());
        return _BAD_REQUEST().setMessage(e.getMessage()).handleResponse();
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
                .getFieldErrors().forEach(
                        it -> {
                            messages.append(
                                    String.format(
                                            messageResourceHelper.apply(it.getDefaultMessage()),
                                            it.getField()
                                    )
                            ).append(", ");
                        }
                );

        return _BAD_REQUEST().setMessage(messages.substring(0, messages.length() - 2)).handleResponse();
    }
}
