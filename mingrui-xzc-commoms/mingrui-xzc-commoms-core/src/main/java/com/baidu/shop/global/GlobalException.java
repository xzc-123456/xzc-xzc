package com.baidu.shop.global;

import com.baidu.shop.base.Result;
import com.baidu.shop.status.HTTPStatus;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName GlobalException
 * @Description: TODO
 * @Author xuezhaochang
 * @Date 2020/12/24
 * @Version V1.0
 **/
@RestControllerAdvice
@Slf4j
public class GlobalException {
    @ExceptionHandler(value = RuntimeException.class)
    public Result<JsonObject> testException(Exception e){
        log.error("code : {} , message : {}", HTTPStatus.ERROR,e.getMessage());
        return new Result<JsonObject>(HTTPStatus.ERROR,e.getMessage(),null);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String,Object> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) throws Exception{
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",HTTPStatus.PARAMS_VALIDATE_ERROR);

        List<String> megList = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().stream().forEach(error ->{
            megList.add("field -->" + error.getField() + ":" + error.getDefaultMessage());
            log.error("field -->" + error.getField() + ":" + error.getDefaultMessage());
        });
        String message = megList.parallelStream().collect(Collectors.joining(","));
        map.put("message",message);
        return map;
    }
}
