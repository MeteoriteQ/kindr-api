package com.example.Exception;

import java.util.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException e){Map<String,String> m=new LinkedHashMap<>();e.getBindingResult().getFieldErrors().forEach(x->m.put(x.getField(),x.getDefaultMessage()));return ResponseEntity.badRequest().body(m);}
    @ExceptionHandler(NoSuchElementException.class) public ResponseEntity<?> notFound(Exception e){return ResponseEntity.status(404).body(Map.of("error",msg(e,"Not found.")));}
    @ExceptionHandler(SecurityException.class) public ResponseEntity<?> forbidden(Exception e){HttpStatus s="Please sign in to continue.".equals(e.getMessage())?HttpStatus.UNAUTHORIZED:HttpStatus.FORBIDDEN;return ResponseEntity.status(s).body(Map.of("error",msg(e,"Access denied.")));}
    @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class}) public ResponseEntity<?> bad(Exception e){return ResponseEntity.badRequest().body(Map.of("error",msg(e,"Invalid request.")));}
    @ExceptionHandler(DataIntegrityViolationException.class) public ResponseEntity<?> conflict(DataIntegrityViolationException e){e.printStackTrace();return ResponseEntity.status(409).body(Map.of("error",e.getMostSpecificCause()==null?"Database constraint error":e.getMostSpecificCause().getMessage()));}
    @ExceptionHandler(Exception.class) public ResponseEntity<?> other(Exception e){e.printStackTrace();return ResponseEntity.status(500).body(Map.of("error","Server error."));}
    private static String msg(Exception e,String fallback){return e.getMessage()==null?fallback:e.getMessage();}
}
