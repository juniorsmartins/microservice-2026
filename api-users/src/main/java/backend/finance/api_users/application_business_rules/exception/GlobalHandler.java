package backend.finance.api_users.application_business_rules.exception;

import backend.finance.api_users.application_business_rules.exception.http400.BadRequestCustomException;
import backend.finance.api_users.application_business_rules.exception.http404.ResourceNotFoundCustomException;
import backend.finance.api_users.application_business_rules.exception.http409.ResourceConflictRulesCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public final class GlobalHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    // ---------- TRATAMENTO DE EXCEÇÕES DEFAULT ---------- //
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders httpHeaders,
                                                                  HttpStatusCode httpStatusCode,
                                                                  WebRequest webRequest) {
        // ProblemDetail RFC 7807
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatusCode);
        problemDetail.setType(URI.create("https://nomad.com/errors/invalid-fields"));
        problemDetail.setTitle(getMessage("exception.request.attribute.invalid"));

        var fields = getFields(ex);

        problemDetail.setProperty("fields", fields);

        return super.handleExceptionInternal(ex, problemDetail, httpHeaders, httpStatusCode, webRequest);
    }

    // ---------- Métodos assessórios ---------- //
    private String getMessage(String messageKey) {
        return this.messageSource.getMessage(messageKey, new Object[]{}, LocaleContextHolder.getLocale());
    }

    private Map<String, List<String>> getFields(BindException ex) {
//        return ex.getBindingResult()
//                .getAllErrors()
//                .stream()
//                .collect(Collectors.toMap(objectError -> ((FieldError) objectError).getField(),
//                        objectError -> this.messageSource.getMessage(objectError, LocaleContextHolder.getLocale())));
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()),
                                Collectors.toList()
                        )
                ));
    }

    // ---------- TRATAMENTO DE EXCEÇÕES CUSTOMIZADAS ---------- //
    // ---------- 400 Bad Request ---------- //
    @ExceptionHandler(BadRequestCustomException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestCustom(BadRequestCustomException ex, WebRequest webRequest) {

        // ProblemDetail RFC 7807
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("https://nomad.com/errors/bad-request"));

        var message = messageSource
                .getMessage(ex.getMessageKey(), new Object[]{ex.getValue0(), ex.getValue1()}, LocaleContextHolder.getLocale());

        problemDetail.setTitle(message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    // ---------- 404 Not-Found ---------- //
    @ExceptionHandler(ResourceNotFoundCustomException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundCustomException ex) {

        // ProblemDetail RFC 7807
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("https://nomad.com/errors/resource-not-found"));

        var message = messageSource
                .getMessage(ex.getMessageKey(), new Object[]{ex.getValue()}, LocaleContextHolder.getLocale());

        problemDetail.setTitle(message);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    // ---------- 409 Conflict ---------- //
    @ExceptionHandler(ResourceConflictRulesCustomException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceConflictRulesCustomException ex) {

        // ProblemDetail RFC 7807
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setType(URI.create("https://nomad.com/errors/resource-conflict-rules"));

        var message = messageSource
                .getMessage(ex.getMessageKey(), new Object[]{ex.getValue()}, LocaleContextHolder.getLocale());

        problemDetail.setTitle(message);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }
}
