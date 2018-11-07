package io.oacy.education.xunwu.service.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import io.oacy.education.xunwu.ApplicationTests;
import io.oacy.education.xunwu.service.QiniuService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

import static org.junit.Assert.*;

public class QiniuServiceImplTest extends ApplicationTests {

    @Autowired
    QiniuService qiNiuService;

    @Test
    public void uploadFile() throws QiniuException {
        File file=new File("C:\\Users\\donghua\\Documents\\WorkSpacekiller\\springboot-education\\oacy-education-xunwu\\src\\main\\resources\\tmp\\xiaoqian.jpeg");
        if (file.exists()) {
            Response response=qiNiuService.uploadFile(file);
            Assert.assertTrue(response.isOK());
        }

    }

    @Test
    public void delete() throws QiniuException {
        Response response=qiNiuService.delete("FuicYLmCF4uuVt8HMKAomGQPohzg");
        Assert.assertTrue(response.isOK());

    }

    @Test
    public void afterPropertiesSet() {
    }
}