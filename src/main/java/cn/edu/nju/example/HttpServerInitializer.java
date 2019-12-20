package cn.edu.nju.example;

import cn.edu.nju.example.demo.HttpServiceController;
import cn.edu.nju.nioserver.HttpMessageCodec;
import cn.edu.nju.nioserver.core.ChannelPipeline;
import cn.edu.nju.nioserver.core.ChannelPipelineFactory;

/**
 * <p>
 * <p>
 *
 * @author Shenmiu
 * @date 2019/12/20
 */
public class HttpServerInitializer implements ChannelPipelineFactory {
    @Override
    public ChannelPipeline getPipeline() {
        ChannelPipeline pipeline = new ChannelPipeline();
        pipeline.add(new HttpMessageCodec());
        pipeline.add(HttpServiceController.controller);
        return pipeline;
    }
}
