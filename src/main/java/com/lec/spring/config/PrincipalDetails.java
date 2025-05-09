package com.lec.spring.config;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//시큐리티가 /user/login (POST) 주소요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인(인증) 진행이 완료되면 '시큐리티 session' 에 넣어주게 된다.
//우리가 익히 알고 있는 같은 session 공간이긴 한데..
//시큐리티가 자신이 사용하기 위한 공간을 가집니다.
//=> Security ContextHolder 라는 키값에다가 session 정보를 저장합니다.
//여기에 들어갈수 있는 객체는 Authentication 객체이어야 한다.
//Authentication 안에 User 정보가 있어야 됨.
//User 정보 객체는 ==> UserDetails 타입 객체이어야 한다.


//따라서 로그인한 User 정보를 꺼내려면
//Security Session 에서
//   => Authentication 객체를 꺼내고, 그 안에서
//        => UserDetails 정보를 꺼내면 된다.
//Principal(접근 주체)은 object 타입이기 때문에 어느거든 넣을 수 있다.
//지금은 UserDetail 을 넣은것
public class PrincipalDetails implements UserDetails, OAuth2User {
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public User user;

    // 로그인한 사용자 정보
    public User getUser() {
        return user;
    }

    //일반 로그인용 생성자
    public PrincipalDetails(User user) {
        System.out.println("🍕UserDetails(user) 생성: " + user);
        this.user = user;
    }

    //OAuth2 로그인용 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        System.out.println("""
                🍕UserDetails(user, oauth attributes) 생성:
                user: %s
                attributes: %s
                """.formatted(user, attributes));
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("🌭getAuthorities() 호출");

        Collection<GrantedAuthority> collect = new ArrayList<>();

        //DB 에서 user의 권한(들) 읽어오기
        List<Authority> list = userService.selectAuthoritiesByUserId(user.getId());

        for(Authority auth : list){
//            collect.add(() -> auth.getName());
            collect.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return auth.getName();
                }

                //thymeleaf 등에서 활용할 문자열
                @Override
                public String toString() {
                    return auth.getName();
                }
            });
        }

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정이 만료된 건 아닌지?
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    //계정이 잠긴 건 아닌지?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정 credential 이 완료된 건 아닌지?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //활성화 되었는지
    @Override
    public boolean isEnabled() {
        return true;
    }

    //--------------------------------------------
    // OAuth2User 를 implement 하게 되면 구현할 메소드들

    private Map<String, Object> attributes; // <- OAuth2User 의 getAttributes() 값

    //OAuth2User 를 implement
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName(){
        return null; //이번 예제에서는 사용 안함
    }
}
