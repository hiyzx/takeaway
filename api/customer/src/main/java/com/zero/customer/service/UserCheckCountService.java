package com.zero.customer.service;

import com.zero.user.dao.UserCheckCountMapper;
import com.zero.user.po.UserCheckCount;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yezhaoxing
 * @date 2017/08/17
 */
@Service
public class UserCheckCountService {

    @Resource
    private UserCheckCountMapper userCheckCountMapper;

    public UserCheckCount getByUserId(Integer userId) {
        Example example = new Example(UserCheckCount.class);
        example.createCriteria().andEqualTo("userId", userId);
        List<UserCheckCount> userCheckCounts = userCheckCountMapper.selectByExample(example);
        return userCheckCounts.isEmpty() ? null : userCheckCounts.get(0);
    }
}
