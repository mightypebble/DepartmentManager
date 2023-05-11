package com.example.myapp.security;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.entities.User;
import com.example.myapp.repositories.UserRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LoginAttemptService {
	
	public static final int MAX_ATTEMPT = 5;
    private LoadingCache<String, Integer> attemptsCache;
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private UserRepository userRepository;
	
	public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    public void loginFailed(final String key, User user) {
        int attempts;
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        if (user.getFailedLoginAttempts() > 9) {
        	final Date date = new Date();
        	date.setTime(date.getTime() + TimeUnit.HOURS.toMillis(4));
        	user.setBanExpirationDate(date);
        }
        userRepository.save(user);
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public String isBlocked(User user) {
    	if (user.getFailedLoginAttempts() > 9) return "blocked";
        try {
            if  (attemptsCache.get(getClientIP()) >= MAX_ATTEMPT) return "temp";
        } catch (final ExecutionException e) {
            return "false";
        }
		return null;
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
    
    public void deleteCache() {
    	attemptsCache.invalidateAll();
    }

}
