package com.example.bootopen.common.utils.web.config;

import com.example.bootopen.common.utils.convert.RespVoConvert;
import com.example.bootopen.common.utils.web.api.WebBaseRespVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ValidationException;

/**
 * 异常处理
 *
 * @author chaijd
 */
@ControllerAdvice
@Slf4j
public class TransExceptionHandler {

//    @ResponseBody
//    @ExceptionHandler(TransWebRuntimeException.class)
//    public Object baseExceptions(TransWebRuntimeException e) {
//        log.error("WebRuntime异常", e);
//
//        //model.addAttribute("message", e.getMessage());
//        //return coverJson(e.getErrorCode(), e.getErrorMsg());
//        return coverResult(e.getErrorCode(), e.getErrorMsg());
//    }
//
//    @ResponseBody
//    @ExceptionHandler(TransControllerException.class)
//    public Object controllerExceptions(TransControllerException e, Model model) {
//        log.error("controller异常", e);
//        return coverResult(e.getErrorCode(), e.getErrorMsg());
//    }

    @ResponseBody
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public Object bindExceptions(Exception exception) {
        log.error("bindExceptions，{}", exception.getMessage());
        BindingResult bindResult = null;
        if (exception instanceof BindException) {
            bindResult = ((BindException) exception).getBindingResult();
        } else if (exception instanceof MethodArgumentNotValidException) {
            bindResult = ((MethodArgumentNotValidException) exception).getBindingResult();
        }
        String errCode = "100001";
        String errMsg = "参数异常";

        if (bindResult != null && bindResult.hasErrors()) {
            errMsg = bindResult.getAllErrors().get(0).getDefaultMessage();
//            if (errMsg.contains("NumberFormatException")) {
//                log.error("validation异常，{}", exception.getMessage());
//            }
        }

        return coverResult(errCode, errMsg);
    }

    @ResponseBody
    @ExceptionHandler(value = {ValidationException.class})
    public Object validationExceptions(ValidationException exception) {
        //log.error("validation异常", exception);
        log.error("validationExceptions，{}", exception.getMessage());
        return coverResult("100100", exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleExceptions(Exception e) {

        //model.addAttribute("message", e.getMessage());
        //return "redirect:/500";
        if (e instanceof ValidationException) {
            //log.error("Exception系统异常{},{}", e.getMessage(), e.getStackTrace());
            log.error("ValidationException 系统异常，{}", e.getMessage());
            return coverResult("100001", e.getMessage());
        }
        log.error("Exception系统异常：", e);
        return coverResult(null, null);
    }

    private WebBaseRespVo coverResult(String errCode, String errMsg) {
        if (StringUtils.isBlank(errCode)) {
            errCode = "100001";
        }
        if (StringUtils.isBlank(errMsg)) {
            errMsg = "未知异常";
        }
        return RespVoConvert.convertResp(errCode, errMsg);
    }

}
