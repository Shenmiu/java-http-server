package cn.edu.nju.example.demo.service.method;

import cn.edu.nju.example.demo.service.method.util.FileUtil;
import cn.edu.nju.nioserver.http.HttpRequest;
import cn.edu.nju.nioserver.http.HttpResponse;
import cn.edu.nju.nioserver.http.HttpResponseStatus;

public class HttpMethodServiceDelete implements HttpMethodServiceInt {
    @Override
    public boolean process(HttpRequest request, HttpResponse response) {
        String curFileName = FileUtil.realFileName(request.uri());
        String wantedFilePreContent = FileUtil.read(curFileName);
        if (wantedFilePreContent == null) {
            // 目标资源不存在
            response.setStatus(HttpResponseStatus.NOT_FOUND);
        } else {
            // DELETE 方法成功执行，资源已被删除
            response.setStatus(HttpResponseStatus.NO_CONTENT);
            boolean a = FileUtil.delete(curFileName);
            System.out.println(a);
        }
        return false;
    }
}
