package com.balance.service;

import com.balance.model.Token;
import com.balance.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by da_20 on 29/5/2017.
 */
@Service("TokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public Token findTokenByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public Iterable<Token> listAllTokens() {
        return tokenRepository.findAll();
    }

    @Override
    public Token getTokenById(Integer id) {
        return tokenRepository.findOne(id);
    }

    @Override
    public void deleteToken(Integer id) {
        tokenRepository.delete(id);
    }
}
