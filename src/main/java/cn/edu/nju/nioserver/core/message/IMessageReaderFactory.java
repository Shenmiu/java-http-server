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
     * 创建具体的 MessageReader
     *
     * @param readBuffer MessageReader 需要的共享缓存
     * @return Message Reader
     */
    IMessageReader createMessageReader(MessageBuffer readBuffer);

}
