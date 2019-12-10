package cn.edu.nju.nioserver.http;

import cn.edu.nju.nioserver.core.message.IMessageReader;
import cn.edu.nju.nioserver.core.message.IMessageReaderFactory;

/**
 * @author jjenkov
 * @date 18-10-2015
 */
public class HttpMessageReaderFactory implements IMessageReaderFactory {

    public HttpMessageReaderFactory() {
    }

    @Override
    public IMessageReader createMessageReader() {
        return new HttpMessageReader();
    }
}
