package com.heang.springmybatistest.controller;


import com.heang.springmybatistest.common.api.ApiResponse;
import com.heang.springmybatistest.common.api.ApiStatus;
import com.heang.springmybatistest.common.api.Code;
import com.heang.springmybatistest.common.api.Common;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

//    core builder (most flexible)
    protected  <T> ResponseEntity<ApiResponse<T>> apiResponseResponseEntity(
            HttpStatus httpStatus,
            T data,
            HttpHeaders httpHeaders,
            Common common
    ){
        ApiResponse<T> body = new ApiResponse<>(
                new ApiStatus(Code.SUCCESS),
                data,
                common
        );
        return ResponseEntity.status(httpStatus).headers(httpHeaders).body(body);
    }


//    1), success with data only
    protected <T> ResponseEntity<ApiResponse<T>> ok(T data){
        return apiResponseResponseEntity(HttpStatus.OK, data, new HttpHeaders(), null);
    }


//    2), success with data and common header info.
    protected <T> ResponseEntity<ApiResponse<T>> ok(T data, Common common){
        return apiResponseResponseEntity(HttpStatus.OK, data, new HttpHeaders(), common);
    }


//    3), success, nobody
    protected ResponseEntity<ApiResponse<Void>> ok(){
        return apiResponseResponseEntity(HttpStatus.OK, null, new HttpHeaders(), null);
    }


    protected <T> ResponseEntity<ApiResponse<T>> created(T data, Common common){
        return apiResponseResponseEntity(HttpStatus.CREATED, data, new HttpHeaders(), common);
    }


}
