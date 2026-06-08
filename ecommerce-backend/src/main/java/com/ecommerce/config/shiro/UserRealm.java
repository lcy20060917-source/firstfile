package com.ecommerce.config.shiro;

import com.ecommerce.domain.User;
import com.ecommerce.repository.UserRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    private final UserRepository userRepository;

    public UserRealm(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String username = (String) principals.getPrimaryPrincipal();
        User user = userRepository.findByUsername(username);
        if (user != null) {
            info.addRole(user.getRole());
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String username = (String) token.getPrincipal();
        log.debug("Shiro 认证: username={}", username);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被禁用");
        }

        return new SimpleAuthenticationInfo(username, user.getPassword(), getName());
    }
}
