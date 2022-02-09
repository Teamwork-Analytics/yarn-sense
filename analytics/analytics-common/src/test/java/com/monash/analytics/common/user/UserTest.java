package com.monash.analytics.common.user;

//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

//import com.imooc.meetingfilm.backend.common.dao.entity.MoocBackendUserT;
//import com.imooc.meetingfilm.backend.common.dao.mapper.MoocBackendUserTMapper;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monash.analytics.common.AnalyticsCommonApplicationTests;
import com.monash.analytics.common.dao.entity.MoocBackendUserT;
import com.monash.analytics.common.dao.mapper.MoocBackendUserTMapper;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

public class UserTest extends AnalyticsCommonApplicationTests {

    @Resource
    private MoocBackendUserTMapper backendUserMapper;

    @Test
    public void add() {
        for (int i = 0; i < 5; i++) {
            MoocBackendUserT user = new MoocBackendUserT();
            user.setUserName("admin" + i);
            user.setUserPwd("admin" + i);
            user.setUserPhone("1311111111" + i);
            backendUserMapper.insert(user);
        }
    }

    @Test
    public void select() {
        QueryWrapper<MoocBackendUserT> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "admin");
        List<MoocBackendUserT> userTList = backendUserMapper.selectList(queryWrapper);
        userTList.forEach(System.out::println);
    }

    @Test
    public void selectByPage() {

        Page<MoocBackendUserT> page = new Page<>(1, 2);

        QueryWrapper<MoocBackendUserT> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "admin");
        IPage<MoocBackendUserT> iPage = backendUserMapper.selectPage(page, queryWrapper);
        iPage.getRecords().forEach(System.out::println);
    }

    @Test
    public void update() {
        QueryWrapper<MoocBackendUserT> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", "admin4");
//        queryWrapper.like("user_name", "admin");
        MoocBackendUserT user2 = new MoocBackendUserT();
        user2.setUserName("admin22222");
        user2.setUserPwd("xinyu22222");
        user2.setUserPhone("18500000000");
        backendUserMapper.update(user2, queryWrapper);
    }

    @Test
    public void delete() {
        backendUserMapper.deleteById(2);
    }
}
