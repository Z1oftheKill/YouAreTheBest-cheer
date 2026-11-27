package com.cheer.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheer.demo.common.BusinessException;
import com.cheer.demo.common.Result;
import com.cheer.demo.common.UserContext;
import com.cheer.demo.dto.QuoteResultDTO;
import com.cheer.demo.dto.QuoteSaveDTO;
import com.cheer.demo.entity.Quote;
import com.cheer.demo.mapper.QuoteMapper;
import com.cheer.demo.service.QuoteService;

@Service
@SuppressWarnings("null")
public class QuoteServiceImpl implements QuoteService {
    private final QuoteMapper quoteMapper;

    public QuoteServiceImpl(QuoteMapper quoteMapper) {
        this.quoteMapper = quoteMapper;
    }

    @Override
    public Result<List<QuoteResultDTO>> getQuotes(String keyword) {

        // 1. 构建查询条件
        LambdaQueryWrapper<Quote> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.and(StringUtils.hasText(keyword), w -> w.like(Quote::getContent, keyword));

        // 2. 查询数据库，得到 Entity
        List<Quote> quotes = quoteMapper.selectList(queryWrapper);

        // 3. Entity 转 DTO
        List<QuoteResultDTO> dtoList = quotes.stream()
                .map(quote -> {
                    QuoteResultDTO dto = new QuoteResultDTO();
                    dto.setId(quote.getId());
                    dto.setContent(quote.getContent());
                    dto.setSource(quote.getSource());
                    dto.setCreatedAt(
                            quote.getCreatedAt() == null
                                    ? null
                                    : quote.getCreatedAt().toString());
                    return dto;
                })
                .toList();

        // 4. 返回结果
        return Result.ok(dtoList);
    }

    @Override
    public Result<Quote> createQuote(QuoteSaveDTO dto) {
        Quote entity = new Quote();

        entity.setContent(dto.getContent());
        entity.setSource(dto.getSource());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUserId(UserContext.getUserId());

        quoteMapper.insert(entity);

        return Result.ok(quoteMapper.selectById(entity.getId()));
    }

    @Override
    public Result<Void> removeQuote(Long id) {
        Quote quote = quoteMapper.selectById(id);
        if (quote == null || !UserContext.getUserId().equals(quote.getUserId())) {
          throw new BusinessException(404, "记录不存在，或者该记录不属于当前登录用户");
        }
        quoteMapper.deleteById(id);
        return Result.ok();
    }
}
