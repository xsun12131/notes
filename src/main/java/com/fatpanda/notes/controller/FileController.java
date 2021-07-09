package com.fatpanda.notes.controller;

import com.fatpanda.notes.common.result.annotation.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xyy
 * @date 2021/7/9
 */
@ResponseResult
@Controller
@RequestMapping("file")
@Api(tags = "file")
public class FileController {

    @PostMapping("upload")
    public void upload(MultipartFile[] multipartFiles) {
        System.out.println();
    }


}
