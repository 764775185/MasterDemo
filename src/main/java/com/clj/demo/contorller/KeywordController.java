package com.clj.demo.contorller;

import com.clj.demo.dto.KeywordResponse;
import com.clj.demo.entity.Keyword;
import com.clj.demo.services.KeywordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/keyword")
public class KeywordController {

    @Autowired
    private KeywordService keywordService;

    @ApiOperation(value = "新增关键字信息")
    @PostMapping(path = "/newKeyword")
    @ResponseBody
    public KeywordResponse<Keyword> newKeyword(@RequestBody Keyword keyword){
        return keywordService.newKeyword(keyword);
    }

    @ApiOperation(value = "获取关键字信息")
    @GetMapping(path = "/getKeyword")
    @ResponseBody
    public KeywordResponse<Keyword> getKeyword(@RequestBody String word){
        return keywordService.getKeyword(word);
    }

    @ApiOperation(value = "增加关键字统计数")
    @PutMapping(path = "/addKeywordSum")
    @ResponseBody
    public KeywordResponse<Keyword> addKeywordSum(@RequestBody String word){
        return keywordService.addKeywordSum(word);
    }

    @ApiOperation(value = "减少关键字统计数")
    @PutMapping(path = "/subKeywordSum")
    @ResponseBody
    public KeywordResponse<Keyword> subKeywordSum(@RequestBody String word){
        return keywordService.subKeywordSum(word);
    }

    @ApiOperation(value = "删除关键字信息")
    @PutMapping(path = "/deleteKeyword")
    @ResponseBody
    public KeywordResponse<Keyword> deleteKeyword(@RequestBody Integer id){
        return keywordService.deleteKeyword(id);
    }
}
