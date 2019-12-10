package cn.edu.nju.nioserver.core.message;

import cn.edu.nju.nioserver.core.WriteProxy;

/**
 * @author jjenkov
 * @date 16-10-2015
 */
@FunctionalInterface
public interface IMessageProcessor {

    void process(Message message, WriteProxy writeProxy);

}
