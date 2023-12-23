package org.book.bookmall.utils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.book.bookmall.dao.MyShiroRealm;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import java.util.LinkedHashMap;
import java.util.Map;
@Configuration
/**
 * shiro????
 */
public class ShiroConfig {

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public SecurityManager securityManager(EhCacheManager ehCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        //securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setCacheManager(ehCacheManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //???
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/book/**", "anon");
        filterChainDefinitionMap.put("/upload/**", "anon");
        filterChainDefinitionMap.put("/page/**", "anon");
        filterChainDefinitionMap.put("/user/info", "user");
        filterChainDefinitionMap.put("/user/**", "anon");//???????????

        filterChainDefinitionMap.put("/index/**",   "anon");//????
        filterChainDefinitionMap.put("/newBooks/**",   "anon");//????
        filterChainDefinitionMap.put("/hotBooks/**",   "anon");//????
        filterChainDefinitionMap.put("/recommenderBooks/**",   "anon");//????
        filterChainDefinitionMap.put("/bookCategories/**",   "anon");//??
        filterChainDefinitionMap.put("/admin/adminLogin/**",   "anon");
        filterChainDefinitionMap.put("/cart/addition", "anon");//?????
        filterChainDefinitionMap.put("/cart/items", "user");
        filterChainDefinitionMap.put("/cart/getCart", "anon");
        filterChainDefinitionMap.put("/cart/buy/num/update", "anon");
        filterChainDefinitionMap.put("/cart/deletion/**", "anon");
        filterChainDefinitionMap.put("/cart/clear", "anon");
        filterChainDefinitionMap.put("/cart/checkBook", "anon");
        filterChainDefinitionMap.put("/cart/orderCart", "anon");
        filterChainDefinitionMap.put("/order/payOrder", "anon");
        filterChainDefinitionMap.put("/order/createOrder", "anon");
        filterChainDefinitionMap.put("/order/updateOrderStatus", "anon");
        filterChainDefinitionMap.put("/order/list", "anon");
        filterChainDefinitionMap.put("/order/deleteOrder", "anon");
        filterChainDefinitionMap.put("/user/update", "anon");

        filterChainDefinitionMap.put("/", "anon");

        //???? ???,??????????Shiro????????
        filterChainDefinitionMap.put("/user/logout", "logout");
        filterChainDefinitionMap.put("/user/logout", "logout");
        //<!-- ??????????????????/**?????? -->:??????????????????;
        //<!-- authc:??url????????????; anon:??url????????-->

        //filterChainDefinitionMap.put("/admin/**", "roles[admin]");//perms[system]
        filterChainDefinitionMap.put("/**", "authc");


        // ????????????Web???????"/login.jsp"??
        shiroFilterFactoryBean.setLoginUrl("/page/login");
        // ???????????
        shiroFilterFactoryBean.setSuccessUrl("/index");

        //?????;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        myShiroRealm.setCachingEnabled(true);
        //????????????AuthenticationInfo?????false
        myShiroRealm.setAuthenticationCachingEnabled(true);
        //??AuthenticationInfo??????? ?ehcache.xml?????????
        myShiroRealm.setAuthenticationCacheName("authenticationCache");
        //??????????AuthorizationInfo?????false
        myShiroRealm.setAuthorizationCachingEnabled(true);
        //??AuthorizationInfo???????  ?ehcache.xml?????????
        myShiroRealm.setAuthorizationCacheName("authorizationCache");
        return myShiroRealm;
    }

    /**
     *  ??shiro aop????.
     *  ??????;??????????;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    /**
     * ?????
     * ????????????Shiro?SimpleAuthenticationInfo?????
     * ?
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//????:????MD5??;
        hashedCredentialsMatcher.setHashIterations(1);//???????????????? md5(md5(""));
        return hashedCredentialsMatcher;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public EhCacheManager ehCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache.xml");
        return em;
    }
}
