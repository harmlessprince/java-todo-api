package todoapplication.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ApiError {
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy, hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private List<String> errors;
//    private List<ApiSubError> subErrors;

    public ApiError() {
        super();
        timestamp = LocalDateTime.now();

    }

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        this();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
//    public ApiError(HttpStatus status, String message, List<ApiSubError> subErrors) {
//        this();
//        this.status = status;
//        this.message = message;
//        this.subErrors = subErrors;
//    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

//    public List<ApiSubError> getSubErrors() {
//        return subErrors;
//    }
//
//    public void setSubErrors(List<ApiSubError> subErrors) {
//        this.subErrors = subErrors;
//    }
}
