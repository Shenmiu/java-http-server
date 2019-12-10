package cn.edu.nju.nioserver.core.message;

/**
 * Message Reader 工厂
 *
 * @author jjenkov
 * @date 16-10-2015
 */
@FunctionalInterface
public interface IMessageReaderFactory {

    /**
     * 创建 Message Reader
     *
     * @return Message Reader
     */
    IMessageReader createMessageReader();

}
