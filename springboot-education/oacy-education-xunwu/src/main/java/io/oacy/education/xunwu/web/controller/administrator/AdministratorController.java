package io.oacy.education.xunwu.web.controller.administrator;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import io.oacy.education.xunwu.base.Response;
import io.oacy.education.xunwu.service.QiniuService;
import io.oacy.education.xunwu.web.dto.QiniuPutRet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class AdministratorController {

    @Autowired
    private QiniuService qiNiuService;

    @Autowired
    private Gson gson;


    /**
     * 后台管理中心
     *
     * @return
     */
    @GetMapping("/admin/center")
    public String adminCenterPage() {
        return "admin/center";
    }

    /**
     * 欢迎页
     *
     * @return
     */
    @GetMapping("/admin/welcome")
    public String welcomePage() {
        return "admin/welcome";
    }

    /**
     * 管理员登录页
     *
     * @return
     */
    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin/login";
    }

    /**
     * 房源列表页
     *
     * @return
     */
    @GetMapping("/admin/house/list")
    public String houseListPage() {
        return "admin/house-list";
    }

    /**
     * 新增房源功能页
     *
     * @return
     */
    @GetMapping("admin/add/house")
    public String addHousePage() {
        return "admin/house-add";
    }

    /**
     * 上传图片接口
     *
     * @param file
     * @return
     */
    @PostMapping(value = "admin/upload/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Response uploadPhoto(@RequestParam("file") MultipartFile file) {
        //校验文件
        if (file.isEmpty()) {
            return Response.ofStatus(Response.Status.NOT_VALID_PARAM);
        }
        //得到文件名
        String fileName = file.getOriginalFilename();

        try {
            InputStream inputStream = file.getInputStream();
            com.qiniu.http.Response response = qiNiuService.uploadFile(inputStream);
            if (response.isOK()) {
                QiniuPutRet ret = gson.fromJson(response.bodyString(), QiniuPutRet.class);
                return Response.ofSuccess(ret);
            } else {
                return Response.ofMessage(response.statusCode, response.getInfo());
            }

        } catch (QiniuException e) {
            com.qiniu.http.Response  response = e.response;
            try {
                return Response.ofMessage(response.statusCode, response.bodyString());
            } catch (QiniuException ee) {
                ee.printStackTrace();
                return Response.ofStatus(Response.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return Response.ofStatus(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}