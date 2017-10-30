package com.zc.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

public class ShiroJDBCMine {

    @Test
    public void testJdbcRealm() {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:ini/shiro-jdbc-mine.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken("test1", "test1"));
        } catch (AccountException e) {
            System.out.println(e.getMessage());
        }
        String username =  (String) subject.getPrincipal();
        System.out.println(username);
        System.out.println(subject.hasRole("customer"));
        System.out.println(subject.isPermitted("customer:update"));
    }

}
