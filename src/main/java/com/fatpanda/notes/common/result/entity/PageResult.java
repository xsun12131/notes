package com.fatpanda.notes.common.result.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fatpanda
 * @date 2020/10/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Api("分页数据")
public class PageResult<T> {

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    @ApiModelProperty(value = "分页数据")
    private List<T> content;

    @ApiModelProperty(value = "总条数")
    private Long total;

    @ApiModelProperty(value = "总页数")
    private Integer totalPage;

    public PageResult listToPageResult(List<T> listAll, Integer pageNum, Integer pageSize) {
        //对返回list做分页
        List<T> returnList = new ArrayList<>();
        for (int i = 0; i < listAll.size(); i++) {
            //分页处理
            if (i < (pageNum * pageSize) || i >= ((pageNum + 1) * pageSize)) {
                continue;
            }
            returnList.add(listAll.get(i));
        }

        this.setTotal(Long.parseLong(listAll.size() + ""));
        this.setPageSize(pageSize);
        this.setPageNum(pageNum);
        this.setContent(returnList);
        int totalPage = (int) (this.getTotal() / pageSize);
        this.setTotalPage(this.getTotal() % pageSize == 0 ? totalPage : totalPage + 1);
        return this;
    }

    /**
     * @param page jpa page
     * @return custom pageResult
     */
    public static PageResult pageToPageResult(Page page) {
        PageResult pageResult = new PageResult();
        pageResult.setContent(page.getContent());
        pageResult.setPageNum(page.getNumber() + 1);
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotalElements());
        pageResult.setTotalPage(page.getTotalPages());

        return pageResult;
    }

    /**
     * @param page jpa page
     * @return custom pageResult
     */
    public static PageResult pageToPageResult(Page page, List handleList) {
        PageResult pageResult = new PageResult();
        pageResult.setContent(handleList);
        pageResult.setPageNum(page.getNumber() + 1);
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotalElements());
        pageResult.setTotalPage(page.getTotalPages());

        return pageResult;
    }

}
