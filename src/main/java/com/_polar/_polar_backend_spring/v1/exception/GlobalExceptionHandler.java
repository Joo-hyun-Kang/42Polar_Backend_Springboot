package com._polar._polar_backend_spring.v1.exception;

import com._polar._polar_backend_spring.v1.exception.dto.ErrorResponse;
import com._polar._polar_backend_spring.v1.exception.dto.SpringValidationResponse;
import com._polar._polar_backend_spring.v1.exception.exceptions.CustomValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
    public static final String CONFLICTEXCEPTION_SEARCH = "データ検索の中に問題が生じました";
    public static final String CONFLICTEXCEPTION_SAVE = "データ保存の中に予期しないエラーが発生しました。";
    public static final String CONFLICTEXCEPTION_DELETE = "データ削除の中に予期しないエラーが発生しました。";
    public static final String CONFLICTEXCEPTION_UPDATE = "データ更新の中に予期しないエラーが発生しました。";
    public static final String NOTFOUNDEXCEPTION = "データを見当たらないです";
    public static final String UNAUTHORIZEDEXCEPTION = "アクセスする権限がありません";
    public static final String BADREQUESTEXCEPTION = "APIから求める形に当てはまっていません";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("[Exception] IllegalArgumentException: ", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(BADREQUESTEXCEPTION, "Bad Request", HttpStatus.BAD_REQUEST.value()));
    }

    //間違ったURLをリクエストする場合：例えば、http://localhost:8080/api/v1/categories////123/keywords
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        log.error("[Exception] NoResourceFoundException: ", e);

        //"Cannot GET /api/v1/categories//%EF%BC%91%EF%BC%91/keywords"
        String sb = "Cannot " +
                request.getMethod() +
                " " +
                request.getRequestURI();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(sb, "Not Found", HttpStatus.NOT_FOUND.value()));
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("[Exception] EntityNotFoundException: ", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(NOTFOUNDEXCEPTION, "Not Found", HttpStatus.NOT_FOUND.value()));
    }

    //データベースにクエリ結果、何もない場合
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResponse> handleEntityEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error("[Exception] EmptyResultDataAccessException: ", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(NOTFOUNDEXCEPTION, "Not Found", HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(SQLException e) {
        log.error("[Exception] DataIntegrityViolationException: ", e);

        String message = "予期しないエラーが発生しました。";
        if (e.getMessage().contains(CONFLICTEXCEPTION_SEARCH)) {
            message = CONFLICTEXCEPTION_SEARCH;
        } else if (e.getMessage().contains(CONFLICTEXCEPTION_SAVE)) {
            message = CONFLICTEXCEPTION_SAVE;
        } else if (e.getMessage().contains(CONFLICTEXCEPTION_DELETE)) {
            message = CONFLICTEXCEPTION_DELETE;
        } else if (e.getMessage().contains(CONFLICTEXCEPTION_UPDATE)) {
            message = CONFLICTEXCEPTION_UPDATE;
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(message, "Conflict", HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("[Exception] AccessDeniedException: ", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(UNAUTHORIZEDEXCEPTION, "Unauthorized", HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SpringValidationResponse> handleTypeAndBeanValidationExceptions(MethodArgumentNotValidException ex, Locale locale, HttpServletRequest request) {
        ArrayList<String> messages = getMessagesFromMessageSource(ex.getBindingResult(), locale);
        log.error("[Exception] MethodArgumentNotValidException: " + request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body((new SpringValidationResponse(messages,"Bad Request", HttpStatus.BAD_REQUEST.value())));
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<SpringValidationResponse> handleCustomValidationException(CustomValidationException ex, Locale locale, HttpServletRequest request) {
        ArrayList<String> messages = getMessagesFromMessageSource(ex.getBindingResult(), locale);
        log.error("[Exception] CustomValidationException: " + request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new SpringValidationResponse(messages, "Bad Request", HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String errorMessage = "Malformed JSON request";

        log.error("[Exception] HttpMessageNotReadableException: " + ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                errorMessage,
                request.getRequestURI() + " Bad Request",
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        //こちらで処理しなければ、Servletから処理されて404HTMLの返し
        log.error("[Exception] RuntimeException: ", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("サーバーエラーが発生しました", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e, BindingResult bindingResult) {
        //こちらで処理しなければ、Servletから処理されて404HTMLの返し
        log.error("[Exception] Unexpected Exception: ", e);
        System.out.println(bindingResult);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("内部エラーが発生しました", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    private ArrayList<String> getMessagesFromMessageSource(BindingResult ex, Locale locale) {
        ArrayList<String> messages = new ArrayList<>();
        BindingResult bindingResult = ex;

        for (FieldError error : bindingResult.getFieldErrors()) {
            String errorMessage = messageSource.getMessage(error, locale);
            messages.add(error.getField() + " " + errorMessage);
        }

        return messages;
    }
}
