package backend.core.api_news.exceptions;

import backend.core.api_news.exceptions.http404.ResourceNotFoundCustomException;
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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public final class GlobalHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    // ---------- TRATAMENTO DE EXCEÇÕES CUSTOMIZADAS ---------- //
    // ---------- 404 Not Found ---------- //
    @ExceptionHandler(ResourceNotFoundCustomException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundCustomException exception) {

        var message = messageSource
                .getMessage(exception.getMessageKey(), new Object[]{exception.getValue()}, LocaleContextHolder.getLocale());

        // ProblemDetail RFC 7807
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("https://microservice2026.com/errors/resource-not-found"));
        problemDetail.setTitle(message);
        problemDetail.setProperty("timestamp", OffsetDateTime.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    // ---------- TRATAMENTO DE EXCEÇÕES DEFAULT ---------- //
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders httpHeaders,
                                                                  HttpStatusCode httpStatusCode,
                                                                  WebRequest webRequest) {
        var fieldErrors = getFieldErrors(exception);

        // ProblemDetail RFC 7807
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatusCode);
        problemDetail.setType(URI.create("https://microservice2026.com/errors/invalid-fields"));
        problemDetail.setTitle(getMessage("exception.request.attribute.invalid"));
        problemDetail.setProperty("fields", fieldErrors);
        problemDetail.setProperty("timestamp", OffsetDateTime.now());

        return super.handleExceptionInternal(exception, problemDetail, httpHeaders, httpStatusCode, webRequest);
    }

    // ---------- Métodos assessórios ---------- //
    private Map<String, List<String>> getFieldErrors(BindException ex) {

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

    private String getMessage(String messageKey) {
        return this.messageSource.getMessage(messageKey, new Object[]{}, LocaleContextHolder.getLocale());
    }

    private ProblemDetail createProblemDetail(HttpStatus httpStatus,
                                              String type,
                                              String title,
                                              Map<String, List<String>> fieldErrors) {
        // ProblemDetail RFC 7807
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setType(URI.create(type));
        problemDetail.setTitle(title);
        problemDetail.setProperty("fields", fieldErrors);
        problemDetail.setProperty("timestamp", OffsetDateTime.now());

        return problemDetail;
    }
}
