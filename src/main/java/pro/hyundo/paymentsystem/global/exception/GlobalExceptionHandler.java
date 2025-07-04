package pro.hyundo.paymentsystem.global.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pro.hyundo.paymentsystem.global.response.ErrorResponse;

@ConfigurationProperties("error-trace")
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 스택 트레이스 출력 여부를 설정합니다.
     * true로 설정하면 예외 발생 시 스택 트레이스를 포함한 응답을 반환합니다.
     */
    @Value("${pay.error-trace.stack-trace-enabled:false}")
    private boolean stackTraceEnabled;

    /**
     * @Valid 어노테이션을 사용한 DTO의 유효성 검증 실패 시 발생하는 예외를 처리합니다.
     */
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("BAD_REQUEST ::: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                null,
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * IOException 발생 시 처리합니다.
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ErrorResponse handleIOExceptions(Exception ex) {
        log.error("ERROR ::: [IOException] ", ex);
        return createErrorResponse(ex, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 처리되지 않은 모든 예외를 처리합니다.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ErrorResponse handleAllExceptions(Exception ex) {
        log.error("ERROR ::: [AllException] ", ex);
        return createErrorResponse(ex, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse createErrorResponse(Exception ex, String message, HttpStatus status) {
        List<StackTraceElement> stackTraces = stackTraceEnabled ? Arrays.asList(ex.getStackTrace()) : null;
        return new ErrorResponse(stackTraces, message, status);
    }

}
