package com.cheer.demo.service;

import java.util.List;

import com.cheer.demo.common.Result;
import com.cheer.demo.dto.QuoteResultDTO;
import com.cheer.demo.dto.QuoteSaveDTO;
import com.cheer.demo.entity.Quote;

public interface QuoteService {
   Result<List<QuoteResultDTO>> getQuotes(String keyword);

   Result<Quote> createQuote(QuoteSaveDTO dto);

   Result<Void> removeQuote(Long id);
}
