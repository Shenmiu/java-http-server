package cn.edu.nju.nioserver.core;

import java.nio.channels.Channel;

/**
 * @author Aneureka
 * @createdAt 2019-12-17 16:51
 * @description
 **/
public interface ChannelPipelineFactory {
    ChannelPipeline getPipeline();
}
