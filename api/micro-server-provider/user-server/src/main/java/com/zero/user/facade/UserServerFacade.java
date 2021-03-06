package com.zero.user.facade;

import com.zero.common.constants.PointConstant;
import com.zero.common.exception.BaseException;
import com.zero.common.po.UserCheckCount;
import com.zero.common.util.DateHelper;
import com.zero.common.util.NumberUtil;
import com.zero.user.enums.CustomerCodeEnum;
import com.zero.user.service.UserCheckCountService;
import com.zero.user.service.UserPointRecordService;
import com.zero.user.service.UserPointService;
import com.zero.user.vo.CheckRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yezhaoxing
 * @date : 2017/4/17
 */
@Service
@Slf4j
public class UserServerFacade {

    @Autowired
    private UserCheckCountService userCheckCountService;
    @Autowired
    private UserPointService userPointService;
    @Autowired
    private UserPointRecordService userPointRecordService;

    public void check(Integer userId) throws BaseException {
        UserCheckCount userCheckCount = userCheckCountService.getByUserId(userId);
        Date now = DateHelper.getCurrentDateTime();
        // 第一次签到
        if (userCheckCount == null) {
            UserCheckCount tmp = new UserCheckCount();
            tmp.setUserId(userId);
            tmp.setCheckTime(now);
            tmp.setContinueCount(1);
            tmp.setMaxCount(1);
            tmp.setSum(1);
            tmp.setHistory(NumberUtil.moveByte(0, 1));
            userCheckCountService.insert(tmp);
            log.info("userId={} first time check", userId);
            // 签到增加积分
            userPointRecordService.increasePoint(userId, PointConstant.POINT_TYPE_CHECK, PointConstant.POINT_CHECK);
        } else {// 非第一次签到
            Date checkTime = userCheckCount.getCheckTime();
            boolean sameDate = DateHelper.isSameDate(checkTime, now);
            // 同一天签到不加分
            if (!sameDate) {
                int daysBetween = DateHelper.daysBetween(checkTime, now);
                UserCheckCount tmp = new UserCheckCount();
                tmp.setId(userCheckCount.getId());
                tmp.setCheckTime(now);
                tmp.setSum(userCheckCount.getSum() + 1);
                tmp.setHistory(NumberUtil.moveByte(userCheckCount.getHistory(), daysBetween));
                if (daysBetween > 1) {
                    tmp.setContinueCount(1);
                } else {
                    int continueCount = userCheckCount.getContinueCount() + 1;
                    tmp.setContinueCount(continueCount);
                    if (continueCount > userCheckCount.getMaxCount()) {
                        tmp.setMaxCount(continueCount);
                    }
                    Integer point = continueCheckScore(continueCount);
                    if (point != null) {
                        userPointRecordService.increasePoint(userId, PointConstant.POINT_TYPE_CONTINUE_CHECK, point);
                    }
                }
                userCheckCountService.updateById(tmp);
                log.info("userId={} continue check day={}", userId, tmp.getContinueCount());
                // 签到增加积分
                userPointRecordService.increasePoint(userId, PointConstant.POINT_TYPE_CHECK, PointConstant.POINT_CHECK);
            } else {
                throw new BaseException(CustomerCodeEnum.CHECK_REPEAT, "今天已经签到过了!");
            }
        }
    }

    /**
     * 连续签到获取积分规则
     */
    private Integer continueCheckScore(int continueCheckCount) {
        Integer rtn;
        if (continueCheckCount == PointConstant.CONTINUE_CHECK_DAY_3) {
            rtn = PointConstant.POINT_CONTINUE_3;
        } else if (continueCheckCount == PointConstant.CONTINUE_CHECK_DAY_7) {
            rtn = PointConstant.POINT_CONTINUE_7;
        } else if (continueCheckCount == PointConstant.CONTINUE_CHECK_DAY_30) {
            rtn = PointConstant.POINT_CONTINUE_30;
        } else {
            rtn = null;
        }
        return rtn;
    }

    public CheckRecordVo queryCheckRecord(Integer userId) {
        UserCheckCount userCheckCount = userCheckCountService.getByUserId(userId);
        CheckRecordVo rtn = new CheckRecordVo();
        if (userCheckCount == null) {
            rtn.setHasCheck(false);
        } else {
            Date now = DateHelper.getCurrentDateTime();
            rtn.setHasCheck(DateHelper.isSameDate(now, userCheckCount.getCheckTime()));
            rtn.setLastCheckTime(userCheckCount.getCheckTime());
            String checkHistory = NumberUtil.toFullBinaryString(userCheckCount.getHistory());
            rtn.setCheckHistory(checkHistory.substring(checkHistory.indexOf("1")));
        }
        return rtn;
    }
}
