package com.zero.customer.web.controller;

import com.zero.common.util.JsonHelper;
import com.zero.common.vo.BaseReturnVo;
import com.zero.common.vo.ReturnVo;
import com.zero.customer.util.DocumentUtil;
import com.zero.customer.vo.dto.MessageBody;
import com.zero.customer.vo.dto.NotificationDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author yezhaoxing
 * @date 2017/11/13
 */
@RestController
@RequestMapping("/test")
@Slf4j
@Api(description = "上传接口")
public class UploadController {

    @Resource
    private DocumentUtil documentUtil;

    @PostMapping("/upload.json")
    @ApiOperation("上传")
    public BaseReturnVo upload(MultipartFile file) throws IOException {
        documentUtil.createDocument(file);
        return BaseReturnVo.success();
    }

    @PostMapping("/callback.json")
    @ApiOperation("回调接口")
    public ReturnVo<String> callback(@RequestBody NotificationDto notificationDto) throws IOException {
        log.info(notificationDto.toString());
        MessageBody messageBody = JsonHelper.readValue(notificationDto.getMessageBody(), MessageBody.class);
        String downloadUrl = documentUtil.getDownloadUrl(messageBody.getDocumentId());
        return ReturnVo.success(downloadUrl);
    }
}
