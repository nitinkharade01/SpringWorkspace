//package com.example.XLSXDataLoader.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.multipart.MaxUploadSizeExceededException;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//    
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntime(RuntimeException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//    }
//    
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ResponseEntity<String> handleMaxSize(MaxUploadSizeExceededException e) {
//        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File too large");
//    }
//    
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGeneral(Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
//    }
//}
