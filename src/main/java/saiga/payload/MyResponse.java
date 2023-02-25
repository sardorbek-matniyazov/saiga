package saiga.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyResponse {
    private String message = "OK";
    private boolean active = true;
    @JsonIgnore
    private int code = 200;
    private final Map<String, Object> body = new HashMap<>();

    public MyResponse(HttpStatus created) {
        if (created.value() >= 400)
            this.active = false;
        this.code = created.value();
        this.message = "Not Ok";
    }

    public MyResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public MyResponse addData(String key, Object value) {
        body.put(key, value);
        return this;
    }

    public ResponseEntity<Object> handleResponse() {
        return ResponseEntity.status(this.getCode()).body(this);
    }

    public static MyResponse _CREATED() { return new MyResponse(HttpStatus.CREATED);}
    public static MyResponse _UPDATED() { return new MyResponse(HttpStatus.OK);}
    public static MyResponse _ALREADY_EXISTS() {
        return new MyResponse(HttpStatus.BAD_REQUEST);
    }
    public static MyResponse _BAD_REQUEST() {
        return new MyResponse(HttpStatus.BAD_REQUEST);
    }
    public static MyResponse _NOT_FOUND() {
        return new MyResponse(HttpStatus.BAD_REQUEST);
    }
    public static MyResponse _ILLEGAL_TYPES() {
        return new MyResponse(HttpStatus.BAD_REQUEST);
    }
    public static MyResponse _OK() {
        return new MyResponse();
    }
}
