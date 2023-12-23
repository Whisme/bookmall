package org.book.bookmall.dao;
import org.book.bookmall.entity.Privilege;
import org.book.bookmall.entity.Role;
import org.book.bookmall.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.book.bookmall.utils.UserResourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;
import java.util.List;
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private CustomMapper customMapper;

    @Autowired
    private UserResourceProperties userResourceProperties;

    /**
     * ????
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //????-->MyShiroRealm.doGetAuthorizationInfo()
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();


        User user = (User) principalCollection.getPrimaryPrincipal();
        List<Role> roles = customMapper.findRolesByUserId(user.getUserId());

        for (Role role : roles) {
            authorizationInfo.addRole(role.getCode());
            List<Privilege> privileges = customMapper.findPrivilegesByRoleId(role.getRoleId());
            for (Privilege privilege : privileges) {
                authorizationInfo.addStringPermission(privilege.getCode());
            }
        }

        return authorizationInfo;
    }

    /**
     * ????
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {


        String username = (String) authenticationToken.getPrincipal();

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", username).andEqualTo("active", userResourceProperties.getActive());
        List<User> users = userMapper.selectByExample(example);

        if (users != null && users.size() != 0) {

            User user = users.get(0);
            AuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                    user,
                    user.getPassword(),
                    this.getName()
            );
            return simpleAuthenticationInfo;
        }

        return null;
    }
}
