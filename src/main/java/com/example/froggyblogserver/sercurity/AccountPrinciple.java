package com.example.froggyblogserver.sercurity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.froggyblogserver.entity.AccountEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccountPrinciple implements UserDetails{

    private String id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    public static AccountPrinciple build(AccountEntity account){
        List<GrantedAuthority> authorities = account.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toList());
        return new AccountPrinciple(
                account.getId(),
                account.getEmail(),
                account.getPassword(),
                authorities);
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    
}
