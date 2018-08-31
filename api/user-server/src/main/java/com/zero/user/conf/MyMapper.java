package com.zero.user.conf;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * 继承自己的MyMapper<br/>
 * 特别注意，该接口不能被扫描到，否则会出错
 *
 * @author yezhaoxing
 * @date 2017/7/29
 */
public interface MyMapper<T>
        extends Mapper<T>, MySqlMapper<T>, ConditionMapper<T>, IdsMapper<T>, InsertListMapper<T> {
}
