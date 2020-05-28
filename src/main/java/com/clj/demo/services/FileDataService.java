package com.clj.demo.services;

import com.alibaba.fastjson.JSON;
import com.clj.demo.dto.FileDataResponse;
import com.clj.demo.entity.CombineKey;
import com.clj.demo.entity.FileData;
import com.clj.demo.entity.User;
import com.clj.demo.exception.AuthorizationException;
import com.clj.demo.exception.BaseDataException;
import com.clj.demo.repository.FileDataRepository;
import com.clj.demo.repository.UserRepository;
import com.clj.demo.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileDataService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserInfoService currentUserInfoService;

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private MailService mailService;


    /**             数据所有者操作         **/
    public FileDataResponse<FileData> newFile(FileData file) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String strStartTime = sdf.format(new Date());
        User user = currentUserInfoService.getUserInfo();
        FileData p = new FileData(file.getTitle(), file.getContent(), file.getCipher(),file.getKeywordVector(),strStartTime,file.getAuthorizeRole(), user);
        fileDataRepository.save(p);
        keywordService.addKeywordSum("totalFiles");
        List<String> keywords = getKeywords(jsonStringToMap(file.getKeywordVector()));
        for(String word: keywords){
            keywordService.addKeywordSum(word);
        }
        return new FileDataResponse<>(200,"文章上传成功！",p);
    }

    public FileDataResponse<Iterable<FileData>> getMyFiles(){
        return new FileDataResponse<>(200,"文章数据获取成功！", fileDataRepository.findFilesDataByUid(currentUserInfoService.getUserInfo().getId()));
    }

    public FileDataResponse<FileData> getFileById(Integer id){
        return new FileDataResponse<>(200,"文章数据获取成功！", fileDataRepository.findFilesDataByIdAndUid(id,currentUserInfoService.getUserInfo().getId()).get());
    }

    public FileDataResponse<FileData> getFileByTile(String tile){
        return new FileDataResponse<>(200,"文章数据获取成功！", fileDataRepository.findFilesDataByTileAndUid(tile,currentUserInfoService.getUserInfo().getId()).get());
    }

    public  FileDataResponse<FileData> shareFileByTile(String email, String tile) throws Exception {
        User user = currentUserInfoService.getUserInfo();
        FileData fileData = fileDataRepository.findFilesDataByTileAndUid(tile,user.getId()).get();
        User shared = userRepository.findUserByEmail(email).get();
        String cipher = RSAUtils.encryKey(fileData.getCipher(),shared.getPkey());
        mailService.sendShareFilesData(email, user, cipher, fileData);
        return new FileDataResponse<>(200,"文章分享成功！", fileData);
    }

    /**************  数据查询者 *****************/
    public FileDataResponse<Iterable<FileData>> findFiles(List<CombineKey> listCkey, Integer k) throws Exception {
        Iterable<FileData> filesRole = fileDataRepository.findFilesDataByRole(currentUserInfoService.getUserInfo().getRole());
        Integer totalFileNum = keywordService.getKeyword("totalFiles").getData().getSum();

/*      Gson gson = new Gson();
        List<CombineKey> listCkey = gson.fromJson(str, new TypeToken<List<CombineKey>>(){}.getType()); */
        Map<Integer, List<String>> map = new HashMap<>();
        for(CombineKey ck: listCkey){
            map.put(ck.getId(),ck.getList());
        }
        List<FileData> filesMatchRank = matchAndRank(totalFileNum,filesRole,map,k);
        return new FileDataResponse<>(200,"文章数据获取成功！", filesMatchRank);
    }


    /**             管理员操作             **/
    public FileDataResponse<Iterable<FileData>> getAllFiles(){
        return new FileDataResponse<>(200,"文章数据获取成功！", fileDataRepository.findAllOderByDesc());
    }

    public FileDataResponse<FileData> findFile(Integer id) {
        Optional<FileData> p = fileDataRepository.findById(id);
        if (!p.isPresent()){
            throw new BaseDataException("该文章不存在！");
        }
        fileDataRepository.save(p.get());
        return new FileDataResponse<>(200,"文章查找成功！",p.get());
    }

    public FileDataResponse<FileData> deleteFile(Integer id) {
        Optional<FileData> p = fileDataRepository.findById(id);
        if (!p.isPresent()){
            throw new BaseDataException("该文章不存在！");
        }
        else {
            User u = currentUserInfoService.getUserInfo();
            if (u.getRole().equals("ADMIN")) {
                fileDataRepository.deleteById(id);
                keywordService.subKeywordSum("totalFiles");
                List<String> keywords = getKeywords(jsonStringToMap(p.get().getKeywordVector()));
                for(String word: keywords){
                    keywordService.subKeywordSum(word);
                }
                return new FileDataResponse<>(200, "文章删除成功！", p.get());
            }
            throw new AuthorizationException("您没有删除此文章的权限！");
        }
    }


    /*************          其他操作       ***********/
    //关键词向量转map数据
    public List<Map<String, Double>> jsonStringToMap(String str) {

        //str = "[{\"社会中\":0.05472214583367988},{\"融合\":0.05472214583367988},{\"三四十年代\":0.05472214583367988},{\"中篇小说\":0.05472214583367988},{\"隐秘\":0.05472214583367988},{\"普通人\":0.05472214583367988},{\"笔触\":0.05472214583367988},{\"揭示\":0.05472214583367988},{\"情结\":0.05472214583367988},{\"男权\":0.05472214583367988},{\"作者\":0.05472214583367988},{\"文化\":0.05472214583367988},{\"摆脱\":0.05472214583367988},{\"部\":0.05472214583367988},{\"白玫瑰\":0.05472214583367988},{\"交汇\":0.05472214583367988},{\"人生\":0.05472214583367988},{\"红玫瑰\":0.05472214583367988},{\"心理\":0.05472214583367988},{\"婚姻生活\":0.05472214583367988},{\"展现\":0.05472214583367988},{\"中西方\":0.05472214583367988},{\"诸多\":0.05472214583367988},{\"20世纪\":0.05472214583367988},{\"传统\":0.05472214583367988},{\"难以\":0.05472214583367988},{\"时\":0.05472214583367988},{\"情感\":0.05472214583367988},{\"中国\":0.05472214583367988},{\"悲剧\":0.05472214583367988},{\"中一\":0.05472214583367988},{\"细腻\":0.05472214583367988},{\"人们\":0.05472214583367988},{\"广为流传\":0.05472214583367988},{\"现代\":0.05472214583367988},{\"女性\":0.03648143055578659},{\"作品\":0.03648143055578659},{\"张爱玲\":0.03648143055578659}]";
        List<Object> list = JSON.parseArray(str.trim());
        List< Map<String,Double>> listw = new ArrayList<>();
        for (Object object : list){
            Map <String,Double> ret = (Map<String,Double>) object;//取出list里面的值转为map
            listw.add(ret);
        }
        return listw;
       /* Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, Double>>>(){}.getType();
        return  gson.fromJson(str.trim(), type);*/
    }

    //提取map数据中的keyword集合
    public List<String> getKeywords(List<Map<String, Double>> map){
        List<String> keysetList = new ArrayList<String>();
        for(Map<String, Double> tmpMap : map){
            for(String tmpStr : tmpMap.keySet()){
                keysetList.add(tmpStr);
            }
        }
        return  keysetList;
    }

    //查询排序算法
    public List<FileData> matchAndRank(Integer totalFileNum, Iterable<FileData> files, Map<Integer, List<String>> map, int k) throws Exception {
        Map<FileData, Double> fileSimMap = new HashMap<>();
        for (FileData file : files) {
            log.info("str {}",file.getKeywordVector());
            List<Map<String, Double>> keywordVector = jsonStringToMap(file.getKeywordVector());
            Set<String> keywordContain = matchWithIi(getKeywords(keywordVector), map, k);
            double fileSumWeight = 0.0;
            for (String keyword : keywordContain) {
                Integer sum = keywordService.getKeyword(keyword).getData().getSum();
                for (Map<String, Double> keyVectmap : keywordVector) {
                    for (Map.Entry<String, Double> keyVaule : keyVectmap.entrySet()) {
                        if (keyword.equals(keyVaule.getKey())) {
                            Double tf_idf = keyVaule.getValue()*((double)totalFileNum)/((double)sum);
                            fileSumWeight +=tf_idf;
                        }
                    }
                }
            }
            double fileSim = getRankSim(map.size(), k, keywordVector.size(), fileSumWeight);
            fileSimMap.put(file,fileSim);
        }
        return mapOrderByVaule(fileSimMap);
    }

    //计算sim值
    public double getRankSim(int m, int t, int vectL, double fileSumWeight){

        //计算当前文件对应关键字向量与查询向量的余弦值
        Double tmpCOS = Math.sqrt( (double)m / (double)t );

        //计算当前文件对应关键字向量与查询向量的向量映射长度
        Double tmpLenghtLi = Math.sqrt(vectL) * tmpCOS;
        Double qArrLenght = Math.sqrt(t);
        Double dist = 0.0;
        if(tmpLenghtLi >= qArrLenght){
            dist = tmpLenghtLi - qArrLenght;
        }
        else {
            dist = 2*(qArrLenght - tmpLenghtLi);
        }

        //综合考量计算文本向量相似度公式
        return tmpCOS*tmpCOS*(1+1/(dist+1))*fileSumWeight;
    }

    //依据value(sim)值对map数据排序
    public List<FileData> mapOrderByVaule(Map<FileData, Double> fileSimMap){
        List<Map.Entry<FileData,Double>> list = fileSimMap.entrySet().stream()
                .sorted(Map.Entry.<FileData,Double> comparingByValue().reversed())
                .collect(Collectors.toList());

        List<FileData> filesRank = new ArrayList<>();
        for(Map.Entry<FileData,Double> tmpMap : list){
            filesRank.add(tmpMap.getKey());
        }
        return filesRank;
    }

    //匹配算法核心
    public Set<String> matchWithIi(List<String> keysetList, Map<Integer, List<String>> map, int k) throws Exception {
        BigInteger n = readRSApkN();
        Set<String> keywordContain = new HashSet<>();

        List<String> queryArrayK = map.get(k);

        List<List<String>> combineKeywords = new ArrayList<List<String>>();
        List<String> tempKeyword = new ArrayList<String>();
        combine(0, k, keysetList, tempKeyword, combineKeywords);
        for(List<String> tempList : combineKeywords){
            //System.out.println(tempList);
            BASE64Decoder decoder = new BASE64Decoder();
            BigInteger res = BigInteger.valueOf(1);
            for( String str : tempList){
                BigInteger tmp = new BigInteger(1,decoder.decodeBuffer(str));
                res = res.multiply(tmp).mod(n);
            }
            BASE64Encoder encoder = new BASE64Encoder();
            String resBase64 = encoder.encode(res.toByteArray());

            for(String queryArr : queryArrayK){
                if(queryArr.equals(resBase64)) {
                    for (String str : tempList) {
                        keywordContain.add(str);
                    }
                }
            }
        }
        return keywordContain;
    }

    public  BigInteger readRSApkN () throws Exception {
        File file1 = new File("C:\\Users\\76477\\IdeaProjects\\TextDemo\\KeyRSApkN.txt");
        String pkN = readFile(file1);
        return new BigInteger(1, base642Byte(pkN));
    }

    public  String readFile(File file) throws Exception {
        StringBuffer buffer = new StringBuffer();
        BufferedReader br= new BufferedReader(new FileReader(file));
        String s = null;

        while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            buffer.append(s.trim());
        }
        br.close();
        return buffer.toString();
    }

    public  void combine(int index, int k, List<String> list, List<String> tempKeyword, List<List<String>>combineKeywords) {
        if(k == 1){
            for (int i = index; i < list.size(); i++) {
                tempKeyword.add(list.get(i));
                List<String> tempKeywords = new ArrayList<String>();
                tempKeywords.addAll(tempKeyword);

                combineKeywords.add(tempKeywords);
                tempKeyword.remove(list.get(i));
            }
        }else if(k > 1 && k<= list.size()){
            for (int i = index; i <= list.size() - k; i++) {
                tempKeyword.add(list.get(i));
                combine(i + 1,k - 1, list, tempKeyword, combineKeywords);
                tempKeyword.remove(list.get(i));
            }
        }else{
            return ;
        }
    }

    //字节数组转Base64编码
    public static String byte2Base64(byte[] bytes){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    //Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) throws IOException{
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64Key);
    }
}