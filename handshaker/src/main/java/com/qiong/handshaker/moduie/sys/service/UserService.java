package com.qiong.handshaker.moduie.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiong.handshaker.define.exception.vaiid.QLogicException;
import com.qiong.handshaker.define.result.QResponse;
import com.qiong.handshaker.moduie.sys.User;
import com.qiong.handshaker.moduie.sys.mapper.UserMapper;
import com.qiong.handshaker.tool.result.QResponseTool;
import com.qiong.handshaker.tool.security.QSecurityMvcTool;
import com.qiong.handshaker.utils.basic.QTypedUtil;
import com.qiong.handshaker.vo.sys.VoUserOptionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    UserMapper mapper;

    @Autowired
    QSecurityMvcTool securityMvcTool;

    /**
    * 新增，修改 用户 的功能
    * @params
    * @return
    */
    public QResponse<Object> posUser(User entity) {
        User old = this.sameUser(entity.getEmail());
        // 加密密码
        entity.setPassword(securityMvcTool.encodePass(entity.getPassword()));
        return (old == null) ?
                QResponseTool.restfull(this.save(entity), entity) :
                QResponseTool.genBad("相同用户", "已存在相同的用户了");
    }
    public QResponse<Object> pacUser(User entity, Long uid) {
        if (QTypedUtil.serLong(uid) == null) throw new QLogicException("用户 ID 非法");
        User old = this.getById(uid);

        // 邮箱 未 改动
        if (old.getEmail().equals(entity.getEmail())) {
            old = null;
        }
        // 邮箱 改动 了
        else {
            old = this.sameUser(entity.getEmail()); // 是否 存在 改动 邮箱 后的 用户
        }
        // 加入 UID
        entity.setId(uid);
        return (old == null) ?
                QResponseTool.restfull(this.updateById(entity), entity) :
                QResponseTool.genBad("相同用户", "已存在相同的用户了");
    }


    /**
    * 是否相同 注册 用户
    * @params
    * @return
    */
    public User sameUser(String email) {
        if (!StringUtils.hasText(email)) return null;
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getEmail, email);
        return mapper.selectOne(qw);
    }

    /**
    * 拿 用戶 信息
    * @params
    * @return
    */
    public User userInfo(Long uid) {
        User user = mapper.info(uid);
        if (user == null) throw new QLogicException("找不到用户");
        user.setPassword("");
        return user;
    }

    /**
    * 自定義 深度 分頁
    * @params
    * @return
    */
    public IPage<User> pageDeep(
            IPage<User> ip,
            QueryWrapper<User> iqw) {
        // 查詢 紀錄
        ip.setRecords(mapper.pageDeep(ip, iqw));
        // 查詢 數量
        ip.setTotal(QTypedUtil.serLong(mapper.pageDeepCount(ip, iqw), 0L));
        return ip;
    }
}