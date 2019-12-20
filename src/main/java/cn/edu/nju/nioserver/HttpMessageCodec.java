package cn.edu.nju.nioserver;

import cn.edu.nju.nioserver.http.*;

import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-19 11:10
 * @description
 **/
public class HttpMessageCodec extends ByteToMessageCodec<HttpRequest> {

    /**
     * 与当前连接保持相同生命周期的request解码器
     */
    private HttpRequestDecoder decoder;

    /**
     * 与当前连接保持相同生命周期的response编码器
     */
    private HttpResponseEncoder encoder;

    //TODO 待商定
    public HttpMessageCodec() {
    }

    public HttpMessageCodec(HttpRequestDecoder decoder, HttpResponseEncoder encoder) {
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    protected boolean decode(List<Byte> in, List<HttpRequest> out) {
        int preSize = out.size();
        boolean result = decoder.decode(in, out);

        //根据requests的长度是否变化来判定是否有解析好的request
        if (preSize < out.size()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean encode(Object in, List<Byte> out) {
        if (in instanceof HttpResponse || in instanceof HttpContent) {
            int prevSize = out.size();
            encoder.encode(in, out);
            if (prevSize < out.size()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
