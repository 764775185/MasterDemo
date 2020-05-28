package com.clj.demo.services;

import com.clj.demo.dto.KeywordResponse;
import com.clj.demo.entity.Keyword;
import com.clj.demo.exception.BaseDataException;
import com.clj.demo.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KeywordService {
    @Autowired
    private KeywordRepository keywordRepository;

    public KeywordResponse<Keyword> newKeyword(Keyword keyword) {
        keywordRepository.save(keyword);
        return new KeywordResponse<>(200,"关键字添加成功！",keyword);
    }

    public KeywordResponse<Keyword> getKeyword(String word){
        Optional<Keyword> keyword = keywordRepository.findKeywordByWord(word);
        if(keyword.isPresent()){
            return new KeywordResponse<>(200,"关键字查找成功！",keyword.get());
        }
        throw new BaseDataException("该关键字不存在！");
    }

    public KeywordResponse<Keyword> addKeywordSum(String word) {
        Optional<Keyword> keyword = keywordRepository.findKeywordByWord(word);
        if(keyword.isPresent()){
            keyword.get().setSum(keyword.get().getSum()+1);
            keywordRepository.save(keyword.get());
            return new KeywordResponse<>(200,"含该关键字的文件数增加1！",keyword.get());
        }
        else{
            Keyword tmpKeyword = new Keyword(word,1);
            newKeyword(tmpKeyword);
            return new KeywordResponse<>(200,"关键字添加成功！",tmpKeyword);
        }
    }

    public KeywordResponse<Keyword> subKeywordSum(String word) {
        Optional<Keyword> keyword = keywordRepository.findKeywordByWord(word);
        if(keyword.isPresent() && keyword.get().getSum()>0){
            keyword.get().setSum(keyword.get().getSum()-1);
            keywordRepository.save(keyword.get());
            return new KeywordResponse<>(200,"含该关键字的文件数减少1！",keyword.get());
        }
        else if(keyword.isPresent() && keyword.get().getSum()==0){
            keywordRepository.delete(keyword.get());
        }
        throw new BaseDataException("该关键字不存在！");
    }

    public KeywordResponse<Keyword> deleteKeyword(Integer id) {
        Optional<Keyword> keyword = keywordRepository.findById(id);
        if(keyword.isPresent()){
            keywordRepository.deleteById(id);
            return new KeywordResponse<>(200,"该关键字删除成功！",keyword.get());
        }
        throw new BaseDataException("该关键字不存在！");
    }
}
