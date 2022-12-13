package saiga.payload.dao;

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

    public static final MyResponse _CREATED = new MyResponse(HttpStatus.CREATED);
    public static final MyResponse _UPDATED = new MyResponse(HttpStatus.OK);
    public static final MyResponse _ALREADY_EXISTS = new MyResponse(HttpStatus.BAD_REQUEST);
    public static final MyResponse _BAD_REQUEST = new MyResponse(HttpStatus.BAD_REQUEST);
    public static final MyResponse _NOT_FOUND = new MyResponse(HttpStatus.BAD_REQUEST);
    public static final MyResponse _ILLEGAL_TYPES = new MyResponse(HttpStatus.BAD_REQUEST);


    private String message = "OK";
    private boolean active = true;
    @JsonIgnore
    private int code = 200;
    private Map<String, Object> body = new HashMap<>();

    public MyResponse(HttpStatus created) {
        if (created.value() >= 400)
            this.active = false;
        this.code = created.value();
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
}
