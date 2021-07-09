package com.fatpanda.notes.controller;

import com.fatpanda.notes.common.result.annotation.ResponseResult;
import com.fatpanda.notes.pojo.entity.NoteTag;
import com.fatpanda.notes.service.NoteTagService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyy
 * @date 2021/7/9
 */
@ResponseResult
@RestController
@RequestMapping("noteTag")
@Api(tags = "noteTag")
public class NoteTagController {

    @Resource
    private NoteTagService noteTagService;

    @GetMapping("all")
    public List<NoteTag> findAll() {
        return noteTagService.findAll();
    }

}
