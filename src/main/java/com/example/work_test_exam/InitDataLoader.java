package com.example.work_test_exam;

import com.example.work_test_exam.persistence.dao.PrivilegeRepository;
import com.example.work_test_exam.persistence.dao.RoleRepository;
import com.example.work_test_exam.persistence.dao.UserRepository;
import com.example.work_test_exam.persistence.model.Privilege;
import com.example.work_test_exam.persistence.model.Role;
import com.example.work_test_exam.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class InitDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege contentSitePrivilege
                = createPrivilegeIfNotFound("CONTENT_SITE_PRIVILEGE");
        Privilege loggedinSitePrivilege
                = createPrivilegeIfNotFound("LOGGEDIN_SITE_PRIVILEGE");
        Privilege adminSitePrivilege
                = createPrivilegeIfNotFound("ADMIN_SITE_PRIVILEGE");

        Role adminRole =createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(contentSitePrivilege,loggedinSitePrivilege,adminSitePrivilege));
        Role contentEditorRole =createRoleIfNotFound("ROLE_CONTENT_EDITOR", Arrays.asList(contentSitePrivilege));
        Role loggedinUserRole =createRoleIfNotFound("ROLE_LOGGEDIN_USER", Arrays.asList(loggedinSitePrivilege));


        User admin = new User();
        admin.setPassword(passwordEncoder.encode("test"));
        admin.setUserName("Admin");
        admin.setRoles(Arrays.asList(adminRole));

        User user1 = new User();
        user1.setPassword(passwordEncoder.encode("test1"));
        user1.setUserName("User 1");
        user1.setRoles(Arrays.asList(contentEditorRole,loggedinUserRole));

        User user2 = new User();
        user2.setPassword(passwordEncoder.encode("test2"));
        user2.setUserName("User 2");
        user2.setRoles(Arrays.asList(contentEditorRole));

        User user3 = new User();
        user3.setPassword(passwordEncoder.encode("test3"));
        user3.setUserName("User 3");
        user3.setRoles(Arrays.asList(loggedinUserRole));

        userRepository.deleteAll();
        userRepository.saveAll(Arrays.asList(admin, user1, user2, user3));

        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    public Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}