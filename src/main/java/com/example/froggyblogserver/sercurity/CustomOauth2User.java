package com.example.froggyblogserver.sercurity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOauth2User extends DefaultOAuth2User {


    public CustomOauth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey) {
        super(authorities, attributes, nameAttributeKey);
    }

    @Override
    public String getName() {
        return super.getAttribute("name");
    }
    public String getEmail() {
        return super.getAttribute("email");
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return super.getAttributes();
    }

    @Override
    public <A> A getAttribute(String name) {
        return super.getAttribute(name);
    }

}
