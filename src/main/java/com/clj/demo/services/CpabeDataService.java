package com.clj.demo.services;

import com.clj.demo.cpabe.cpabekey.Cpabe;
import com.clj.demo.dto.CpabeDataResponse;
import com.clj.demo.entity.CpabeData;
import com.clj.demo.entity.User;
import com.clj.demo.exception.AuthorizationException;
import com.clj.demo.exception.BaseDataException;
import com.clj.demo.repository.CpabeDateRepository;
import com.clj.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class CpabeDataService {

    @Autowired
    private CpabeDateRepository cpabeDateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserInfoService currentUserInfoService;

    static String dir = "C:\\Users\\76477\\IdeaProjects\\demo\\src\\main\\java\\com\\clj\\demo\\cpabe\\cpabekey\\para";

    static String pubfile = dir + "/pub_key";
    static String mskfile = dir + "/master_key";

    /**             数据所有者特有操作         **/
    public CpabeDataResponse<CpabeData> newFile(CpabeData file) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String strStartTime = sdf.format(new Date());
        User user = currentUserInfoService.getUserInfo();
        String cipher = Cpabe.enc(pubfile,  file.getPlicy(), file.getCipher());
        CpabeData p = new CpabeData(file.getTitle(), file.getContent(), cipher, strStartTime, file.getPlicy(), user);
        cpabeDateRepository.save(p);
        return new CpabeDataResponse<>(200,"文章上传成功！",p);
    }

    public CpabeDataResponse<Iterable<CpabeData>> getMyFiles(){
        return new CpabeDataResponse<>(200,"文章数据获取成功！", cpabeDateRepository.findCpabeDataByUid(currentUserInfoService.getUserInfo().getId()));
    }


    /**             管理员特有操作             **/
    public CpabeDataResponse<CpabeData> deleteFile(Integer id) {
        Optional<CpabeData> p = cpabeDateRepository.findById(id);
        if (!p.isPresent()){
            throw new BaseDataException("该文章不存在！");
        }
        else {
            User u = currentUserInfoService.getUserInfo();
            if (u.getRole().equals("ADMIN")) {
                cpabeDateRepository.deleteById(id);
                return new CpabeDataResponse<>(200, "文章删除成功！", p.get());
            }
            throw new AuthorizationException("您没有删除此文章的权限！");
        }
    }


    /**             密文数据下载操作         **/
    public CpabeDataResponse<CpabeData> downloadFile(Integer id) throws Exception {
        User u = currentUserInfoService.getUserInfo();
        CpabeData p =  cpabeDateRepository.findById(id).get();
        String sk = Cpabe.keygen(pubfile,mskfile,u.getAttr());
        p.setNote(sk);
        return new CpabeDataResponse<>(200,"文章数据获取成功！",cpabeDateRepository.findById(id).get());
    }


    /**             数据查询通用操作         **/
    public CpabeDataResponse<Iterable<CpabeData>> getAllFiles(){
        return new CpabeDataResponse<>(200,"文章数据获取成功！", cpabeDateRepository.findAllOderByDesc());
    }

    public CpabeDataResponse<CpabeData> getFileById(Integer id){
        return new CpabeDataResponse<>(200,"文章数据获取成功！", cpabeDateRepository.findById(id).get());
    }

    public CpabeDataResponse<Iterable<CpabeData>> getFileByTile(String tile){
        return new CpabeDataResponse<>(200,"文章数据获取成功！", cpabeDateRepository.findCpabeDataByTile(tile));
    }



}
