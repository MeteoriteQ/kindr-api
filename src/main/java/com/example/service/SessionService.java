package com.example.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.example.Entity.*;
import com.example.repository.*;
import com.example.util.Ids;

@Service
public class SessionService {
    private final UserSessionRepository sessions; private final UserRepository users;
    public SessionService(UserSessionRepository sessions,UserRepository users){this.sessions=sessions;this.users=users;}
    public String create(User user){String token=Ids.token();UserSession s=new UserSession();s.setToken(token);s.setUserId(String.valueOf(user.getId()));s.setExpiresAt(LocalDateTime.now().plusDays(7));sessions.save(s);return token;}
    public User fromToken(String token){
        if(token==null||token.isBlank())return null;
        var o=sessions.findById(token);if(o.isEmpty())return null;
        var s=o.get();if(s.getExpiresAt().isBefore(LocalDateTime.now())){sessions.delete(s);return null;}
        try{return users.findById(Long.valueOf(s.getUserId())).orElse(null);}catch(NumberFormatException ex){return null;}
    }
    public void delete(String token){if(token!=null&&!token.isBlank())sessions.deleteById(token);}
}
