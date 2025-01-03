package com._polar._polar_backend_spring.v1.exception;

import com._polar._polar_backend_spring.v1.exception.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String CONFLICTEXCEPTION_SEARCH = "データ検索の中に問題が生じました";
    private static final String CONFLICTEXCEPTION_SAVE = "データ保存の中に予期しないエラーが発生しました。";
    private static final String CONFLICTEXCEPTION_DELETE = "データ削除の中に予期しないエラーが発生しました。";
    private static final String CONFLICTEXCEPTION_UPDATE = "データ更新の中に予期しないエラーが発生しました。";
    private static final String NOTFOUNDEXCEPTION = "データを見当たらないです";
    private static final String UNAUTHORIZEDEXCEPTION = "アクセスする権限がありません";
    private static final String BADREQUESTEXCEPTION = "APIから求める形に当てはまっていません";

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
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot ");
        sb.append(request.getMethod());
        sb.append(" ");
        sb.append(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(sb.toString(), "Not Found", HttpStatus.NOT_FOUND.value()));
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("[Exception] DataIntegrityViolationException: ", e);

        String message = "予期しないエラーが発生しました。";
        if (e.getMessage().contains("search")) {
            message = CONFLICTEXCEPTION_SEARCH;
        } else if (e.getMessage().contains("save")) {
            message = CONFLICTEXCEPTION_SAVE;
        } else if (e.getMessage().contains("delete")) {
            message = CONFLICTEXCEPTION_DELETE;
        } else if (e.getMessage().contains("update")) {
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error("[Exception] RuntimeException: ", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("サーバーエラーが発生しました", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("[Exception] Unexpected Exception: ", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("内部エラーが発生しました", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
