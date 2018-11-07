package io.oacy.education.xunwu.service.implement;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import io.oacy.education.xunwu.service.QiniuService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class QiniuServiceImpl implements QiniuService, InitializingBean {
    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    @Autowired
    private Auth auth;

    @Value("${qiniu.Bucket}")
    private String bucket;

    private StringMap putPolicy;

    @Override
    public Response uploadFile(File file) throws QiniuException {
        AtomicReference<Response> response = new AtomicReference<>(this.uploadManager.put(file, null, getUploadToken()));
        AtomicInteger retry = new AtomicInteger();
        while (response.get().needRetry() && retry.get() < 3) {
            response.set(this.uploadManager.put(file, null, getUploadToken()));
            retry.getAndIncrement();
        }
        return response.get();
    }

    @Override
    public Response uploadFile(InputStream inputStream) throws QiniuException {
        AtomicReference<Response> response = new AtomicReference<>(this.uploadManager.put(inputStream, null, getUploadToken(), null, null));
        AtomicInteger retry = new AtomicInteger();
        while (response.get().needRetry() && retry.get() < 3) {
            response.set(this.uploadManager.put(inputStream, null, getUploadToken(), null, null));
            retry.getAndIncrement();
        }
        return response.get();
    }

    @Override
    public Response delete(String key) throws QiniuException {
        AtomicReference<Response> response = new AtomicReference<>(bucketManager.delete(this.bucket, key));
        AtomicInteger retry = new AtomicInteger();
        while (response.get().needRetry() && retry.getAndIncrement() < 3) {
            response.set(bucketManager.delete(bucket, key));
        }
        return response.get();
    }

    @Override
    public void afterPropertiesSet() {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
    }

    /**
     * 获取上传凭证
     * @return
     */
    private String getUploadToken() {
        return this.auth.uploadToken(bucket, null, 3600, putPolicy);
    }
}
