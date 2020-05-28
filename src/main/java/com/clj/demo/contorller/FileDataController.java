package com.clj.demo.contorller;

import com.clj.demo.dto.FileDataResponse;
import com.clj.demo.entity.CombineKey;
import com.clj.demo.entity.FileData;
import com.clj.demo.services.FileDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/api/file")
public class FileDataController {

    @Autowired
    private FileDataService fileDataService;

    /**             数据所有者操作           **/
    @ApiOperation(value = "上传文章",notes = "数据所有者权限")
    @PostMapping(path = "/uploadFile")
    @ResponseBody
    public FileDataResponse<FileData> uploadFile( FileData file){
        return fileDataService.newFile(file);
    }

    @ApiOperation(value = "获取个人所有文章",notes = "数据所有者权限")
    @GetMapping(path = "/getMyFiles")
    @ResponseBody
    public FileDataResponse<Iterable<FileData>> getMyFiles(){
        return fileDataService.getMyFiles();
    }

    @ApiOperation(value = "根据标题获取个人文章",notes = "数据所有者权限")
    @GetMapping(path = "/getMyFileByTile")
    @ResponseBody
    public FileDataResponse<FileData> getFileByTile(String tile){return fileDataService.getFileByTile(tile);}

    @ApiOperation(value = "主动分享个人文章",notes = "数据所有者权限")
    @GetMapping(path = "/shareFile")
    @ResponseBody
    public FileDataResponse<FileData> shareFile( String email, String tile) throws Exception {return fileDataService.shareFileByTile(email,tile);}

    /**             数据查询者操作           **/
    @ApiOperation(value = "按关键字查找文章",notes = "数据查询者权限")
    @GetMapping(path = "/findFiles")
    @ResponseBody
    public FileDataResponse<Iterable<FileData>> findFiles(@RequestBody List<CombineKey> list, Integer k) throws Exception {
        return fileDataService.findFiles(list, k);
    }

    /**             管理员操作              **/
    @ApiOperation(value = "显示所有文章",notes = "管理员权限")
    @GetMapping(path = "/getAllFiles")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public FileDataResponse<Iterable<FileData>> getAllFiles(){
        return fileDataService.getAllFiles();
    }

    @ApiOperation(value = "查找单篇文章",notes = "管理员权限")
    @GetMapping(path = "/getFile")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public FileDataResponse<FileData> getFile(@RequestParam Integer id){
        return fileDataService.findFile(id);
    }

    @ApiOperation(value = "删除选定文章",notes = "管理员权限")
    @DeleteMapping(path = "/deleteFile")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public FileDataResponse<FileData> deleteFile(@RequestParam Integer id){
        return fileDataService.deleteFile(id);
    }

}
