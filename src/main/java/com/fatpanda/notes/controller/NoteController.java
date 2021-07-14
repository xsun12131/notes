package com.fatpanda.notes.controller;

import com.fatpanda.notes.common.model.entity.SearchDto;
import com.fatpanda.notes.common.result.annotation.ResponseResult;
import com.fatpanda.notes.common.result.entity.PageResult;
import com.fatpanda.notes.pojo.dto.NoteDto;
import com.fatpanda.notes.pojo.entity.Note;
import com.fatpanda.notes.pojo.entity.NoteSeries;
import com.fatpanda.notes.pojo.vo.NoteListVo;
import com.fatpanda.notes.service.NoteSeriesService;
import com.fatpanda.notes.service.NoteService;
import com.fatpanda.notes.service.NoteTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@ResponseResult
@RestController
@RequestMapping("note")
@Api(tags = "note")
public class NoteController {

    @Resource
    private NoteService noteService;
    @Resource
    private NoteTagService noteTagService;
    @Resource
    private NoteSeriesService noteSeriesService;

    @PostMapping()
    @ApiOperation("新增或修改")
    public Note save(@RequestBody NoteDto noteDto) {
        return noteService.save(noteDto);
    }

    @GetMapping("page")
    @ApiOperation("分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", example = "10"),
    })
    public PageResult<NoteListVo> findAll(Integer pageNum, Integer pageSize) {
        PageResult<NoteListVo> all = noteService.findAll(pageNum - 1, pageSize);
        return all;
    }

    @PostMapping("search")
    @ApiOperation("搜索")
    public PageResult<NoteListVo> search(@RequestBody SearchDto searchDto) {
        return noteService.search(searchDto);
    }

    @GetMapping()
    @ApiOperation("查询")
    @ApiImplicitParam(name = "id", paramType = "query")
    public Note getOne(@Valid @NotNull String id) {
        return noteService.getById(id);
    }

    @DeleteMapping("batch")
    @ApiOperation("批量删除")
    public Map deleteBatch(@RequestBody String[] ids) {
        Map<String, Object> map = new HashMap();
        map.put("成功删除的id", noteService.deleteNoteBatch(ids));
        return map;
    }

    @PostMapping("parseMd")
    @ApiOperation("解析markdown文件")
    public Note parseMd(MultipartFile multipartFile) {
        return noteService.parseMd(multipartFile);
    }

    @GetMapping("byTag")
    @ApiOperation("查找同标签的文章")
    public List<NoteListVo> findByTag(String tagName) {
        return noteTagService.findNoteByTag(tagName);
    }

    @GetMapping("findSameSeries")
    @ApiOperation("查找同系列文章")
    public List<NoteListVo> findSameSeries(String noteId) {
        return noteSeriesService.findSameSeries(noteId);
    }

    @PostMapping("addSeries")
    @ApiOperation("添加系列")
    public NoteSeries saveSeries(@RequestBody NoteSeries noteSeries) {
        return noteSeriesService.save(noteSeries);
    }

}
