package com.clj.demo.contorller;

import com.clj.demo.dto.CpabeDataResponse;
import com.clj.demo.entity.CpabeData;
import com.clj.demo.services.CpabeDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/cpabe")
public class CpabeDateControll {

    @Autowired
    private CpabeDataService cpabeDataService;

    /**             数据所有者操作           **/

    @ApiOperation(value = "上传文章",notes = "数据所有者权限")
    @PostMapping(path = "/uploadFile")
    @ResponseBody
    public CpabeDataResponse<CpabeData> uploadFile(@RequestBody CpabeData file) throws Exception {
        return cpabeDataService.newFile(file);
    }

    @ApiOperation(value = "获取个人所有文章",notes = "数据所有者权限")
    @GetMapping(path = "/getMyFiles")
    @ResponseBody
    public CpabeDataResponse<Iterable<CpabeData>> getMyFiles(){
        return cpabeDataService.getMyFiles();
    }


    /**             管理员操作              **/

    @ApiOperation(value = "删除选定文章",notes = "管理员权限")
    @DeleteMapping(path = "/deleteFile")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public CpabeDataResponse<CpabeData> deleteFile(@RequestParam Integer id){
        return cpabeDataService.deleteFile(id);
    }


    /**             密文数据下载操作         **/

    @ApiOperation(value = "下载单篇文章",notes = "数据访问通用权限")
    @GetMapping(path = "/downloadFile")
    @ResponseBody
    public CpabeDataResponse<CpabeData> downloadFile(@RequestParam Integer id) throws Exception {
        return cpabeDataService.downloadFile(id);
    }

    /**             数据查询通用操作         **/

    @ApiOperation(value = "显示所有文章",notes = "数据访问通用权限")
    @GetMapping(path = "/getAllFiles")
    @ResponseBody
    public CpabeDataResponse<Iterable<CpabeData>> getAllFiles(){
        return cpabeDataService.getAllFiles();
    }

    @ApiOperation(value = "根据标题查找单篇文章",notes = "数据访问通用权限")
    @GetMapping(path = "/getFileByTile")
    @ResponseBody
    public CpabeDataResponse<Iterable<CpabeData>> getFileByTile(@RequestParam String tile){
        return cpabeDataService.getFileByTile(tile);
    }

    @ApiOperation(value = "根据id查找单篇文章",notes = "数据访问通用权限")
    @GetMapping(path = "/getFileById")
    @ResponseBody
    public CpabeDataResponse<CpabeData> getFileById(@RequestParam Integer id){
        return cpabeDataService.getFileById(id);
    }
}
