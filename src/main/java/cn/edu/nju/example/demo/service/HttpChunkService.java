package cn.edu.nju.example.demo.service;

import cn.edu.nju.nioserver.core.ChannelHandlerContext;
import cn.edu.nju.nioserver.http.*;
import lombok.extern.log4j.Log4j2;

import java.nio.ByteBuffer;

/**
 * 块传输场景
 * <p>
 *
 * @author Shenmiu
 * @date 2019/12/20
 */
@Log4j2
public class HttpChunkService {
    public void service(ChannelHandlerContext ctx, HttpRequest request) {
        log.info("========模拟 chunk 分块开始========");
        HttpResponse response = new HttpResponse();
        response.setStatus(HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
        ctx.sendDownStream(response);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("sleep error");
        }
        for (int i = 0; i < 5; i++) {
            log.info("模拟耗时业务处理过程");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("sleep error");
            }
            ctx.sendDownStream(new HttpContent(ByteBuffer.wrap(("Chunk " + i + "\n").getBytes())));
        }
        ctx.sendDownStream(new HttpContent(ByteBuffer.allocate(0)));
        log.info("========模拟 chunk 分块结束========");
    }
}
