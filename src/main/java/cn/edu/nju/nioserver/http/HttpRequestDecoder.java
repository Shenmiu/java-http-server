package cn.edu.nju.nioserver.http;

import cn.edu.nju.nioserver.util.BytesUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestDecoder {

    /**
     * 初始状态
     */
    private State currentState = State.READ_INITIAL;

    /**
     * 当前HttpRequest
     */
    private HttpRequest curRequest = new HttpRequest();

    /**
     * 当前的chunk长度
     */
    private int curChunkLength = -1;

    /**
     * 当前的chunk内容
     */
    private List<Byte> curChunk = new ArrayList<>();


    /**
     * 解析请求，将固定格式包装进request中(调用者还是需要根据requests的长度是否变化来判定是否有解析好的request)
     *
     * @param in       字节流缓冲区
     * @param requests 请求列表
     */
    public boolean decode(List<Byte> in, List<HttpRequest> requests) {
        int limit = in.size();
        int start = 0;
        while (start != limit) {
            switch (currentState) {
                case READ_INITIAL: {
                    int result = decodeRequestLine(start, in, curRequest);
                    if (result == -1) {
                        removeUsedElement(start, in);
                        return false;
                    }
                    start = result;
                    currentState = State.READ_HEADER;
                    break;
                }
                case READ_HEADER: {
                    int result = decodeRequestHeaders(start, in, curRequest);
                    if (result == -1) {
                        removeUsedElement(start, in);
                        return false;
                    }
                    start = result;

                    if (HttpHeaders.hasContentLength(curRequest)) {
                        //表示接下来的content为空，无需解析可以直接跳转READ_INITIAL状态
                        if (HttpHeaders.getContentLength(curRequest) == 0) {
                            requests.add(curRequest);
                            //reset
                            reset();
                        } else {
                            currentState = State.READ_FIXED_LENGTH_CONTENT;
                        }
                    } else if (HttpHeaders.isChunkTransfer(curRequest)) {
                        currentState = State.READ_VARIABLE_LENGTH_CONTENT;
                    } else {
                        //Content-Length || Transfer-encoding
                        requests.add(curRequest);
                        //reset
                        reset();
                    }
                    break;
                }
                case READ_FIXED_LENGTH_CONTENT: {
                    int result = decodeRequestContent(start, HttpHeaders.getContentLength(curRequest), in, curRequest);
                    if (result == -1) {
                        removeUsedElement(start, in);
                        return false;
                    }

                    //当前请求编码结束
                    start = result;
                    requests.add(curRequest);
                    //reset
                    reset();
                    break;
                }
                case READ_VARIABLE_LENGTH_CONTENT: {
                    //跳转到开始读取块长度状态
                    currentState = State.READ_CHUNK_SIZE;
                    break;
                }
                case READ_CHUNK_SIZE: {
                    int result = decodeRequestChunkSize(start, in);
                    if (result == -1) {
                        removeUsedElement(start, in);
                        return false;
                    }

                    start = result;
                    if (this.curChunkLength != 0) {
                        currentState = State.READ_CHUNKED_CONTENT;
                    } else {
                        currentState = State.READ_CHUNK_FOOTER;
                    }
                    break;
                }
                case READ_CHUNKED_CONTENT: {
                    int result = decodeRequestChunkContent(start, in);
                    if (result == -1) {
                        removeUsedElement(start, in);
                        return false;
                    }

                    start = result;
                    currentState = State.READ_CHUNK_SIZE;
                    break;
                }
                case READ_CHUNK_FOOTER: {
                    int result = decodeRequestChunkFooter(start, in, curRequest);
                    if (result == -1) {
                        removeUsedElement(start, in);
                        return false;
                    }

                    //当前请求分块传输结束
                    start = result;
                    requests.add(curRequest);
                    //reset
                    reset();
                    break;
                }
            }
        }

        //当前in的内容已完全解析，跳出了循环，需要去除in的全部内容
        removeUsedElement(start, in);
        return true;
    }

    /**
     * 解析请求行[method uri version]
     *
     * @param start   开始读取请求行的第一个字符下标
     * @param in      缓冲
     * @param request HttpRequest
     * @return int 下一部分的开始下标，解析失败为-1
     */
    private int decodeRequestLine(int start, List<Byte> in, HttpRequest request) {
        List<Byte> contentList = new ArrayList<>();
        int index = start;
        int limit = in.size();
        byte cur = in.get(start);
        while (cur != '\r') {
            contentList.add(cur);
            index++;

            //buffer已经读到末尾，但是没有找到请求行的结束符(\r\n)
            if (index == limit) {
                return -1;
            }

            cur = in.get(index);
        }

        index++;
        //read the '\n'
        cur = in.get(index);


        String[] info = byte2String(contentList).split("\\s");
        // method
        request.setMethod(HttpMethod.valueOf(info[0]));
        //uri
        request.setUri(info[1]);
        //version
        request.setVersion(HttpVersion.valueOf(info[2]));

        return index + 1;
    }

    /**
     * 解析请求首部
     *
     * @param start   开始读取请求行的第一个字符下标
     * @param in      缓冲
     * @param request HttpRequest
     * @return int 下一部分的开始下标，解析失败为-1
     */
    private int decodeRequestHeaders(int start, List<Byte> in, HttpRequest request) {
        //解析时创建，不能调用给上层提供的接口
        HttpHeaders httpHeaders = new HttpHeaders();

        int limit = in.size();
        int index = start;
        int countEnd = 0;
        while (countEnd != 2) {
            if (index >= limit) {
                return -1;
            }

            List<Byte> contentList = new ArrayList<>();
            byte cur = in.get(index);
            while (cur != '\r') {
                //恢复
                countEnd = 0;

                contentList.add(cur);
                index++;
                //buffer已经读到末尾，但是没有找到请求行的结束符(\r\n)
                if (index == limit) {
                    return -1;
                }
                cur = in.get(index);
            }

            //指向'\n'
            index++;
            //read the '\n'
            cur = in.get(index);
            //指向下一行的起始字符
            index++;

            if (countEnd == 1) {
                //读到了连续的两个\r\n
                break;
            }
            String[] header = byte2String(contentList).split(":");
            httpHeaders.set(header[0].trim(), header[1].trim());
            countEnd++;
        }

        request.setHeaders(httpHeaders);
        return index;
    }

    /**
     * 解析请求内容
     *
     * @param start         开始读取请求内容的第一个字符下标
     * @param contentLength content的长度
     * @param in            缓冲
     * @param request       HttpRequest
     * @return int 下一部分的开始下标，解析失败为-1
     */
    private int decodeRequestContent(int start, int contentLength, List<Byte> in, HttpRequest request) {
        //HeapByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(contentLength);
        int limit = in.size();
        for (int i = 0; i < contentLength; i++) {
            if (start + i < limit) {
                byteBuffer.put(in.get(start + i));
            } else {
                return -1;
            }
        }
        request.setContent(new HttpContent(byteBuffer));
        return start + contentLength;
    }

    /**
     * 读取chunk的内容
     *
     * @param start 开始读取chunk的第一个字符下标
     * @param in    缓冲
     * @return int 下一部分的开始下标，解析失败为-1
     */
    private int decodeRequestChunkSize(int start, List<Byte> in) {
        List<Byte> size = new ArrayList<>();
        int limit = in.size();
        int index = start;
        byte cur = in.get(index);
        while (cur != '\r') {
            size.add(cur);
            index++;

            if (index == limit) {
                return -1;
            }

            cur = in.get(index);
        }

        index++;
        //当前为'\n'
        cur = in.get(index);


        this.curChunkLength = BytesUtil.bytes2Int(BytesUtil.list2ByteArray(size));
        return index + 1;
    }

    /**
     * 读取chunk的内容
     *
     * @param start 开始读取chunk的第一个字符下标
     * @param in    缓冲
     * @return int 下一部分的开始下标，解析失败为-1
     */
    private int decodeRequestChunkContent(int start, List<Byte> in) {
        //先用list保存所有的chunk,当读取到最后一个chunk的时候，将list全部写入request
        List<Byte> content = new ArrayList<>();
        int limit = in.size();
        for (int i = 0; i < this.curChunkLength; i++) {
            if (start + i < limit) {
                content.add(in.get(start + i));
            } else {
                return -1;
            }
        }

        int index = start + this.curChunkLength;
        if (limit - index < 2) {
            //最后的"\r\n"不完整
            return -1;
        }

        byte cur_r = in.get(index);
        index++;
        byte cur_n = in.get(index);
        if (cur_r != '\r' || cur_n != '\n') {
            //消息是错误的
            return -1;
        }
        //当前块成功读完
        this.curChunk.addAll(content);
        return index + 1;
    }

    /**
     * 读取chunk的footer，并将块缓存encode入request中(结束块也必须读完整，才可以encode)
     *
     * @param start   开始读取chunk的第一个字符下标
     * @param in      缓冲
     * @param request HttpRequest
     * @return int 下一部分的开始下标，解析失败为-1
     */
    private int decodeRequestChunkFooter(int start, List<Byte> in, HttpRequest request) {

        int limit = in.size();
        if (limit - start < 2) {
            // "\r\n"不完整
            return -1;
        }

        int index = start;
        byte cur_r = in.get(index);
        index++;
        byte cur_n = in.get(index);
        if (cur_r != '\r' || cur_n != '\n') {
            //消息是错误的
            return -1;
        }


        //结束块成功读完
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.curChunk.size());
        for (int i = 0; i < this.curChunk.size(); i++) {
            byteBuffer.put(this.curChunk.get(i));
        }
        request.setContent(new HttpContent(byteBuffer));
        return index + 1;
    }

    /**
     * 重置当前对象为初始状态
     */
    private void reset() {
        currentState = State.READ_INITIAL;
        curRequest = new HttpRequest();
        curChunk = new ArrayList<>();
        curChunkLength = -1;
    }

    /**
     * 去除list中下标start之前的元素，不包括start
     *
     * @param start 需要开始保留元素的起始下标
     * @param in    缓冲
     */
    private void removeUsedElement(int start, List<Byte> in) {
        for (int i = 0; i < start; i++) {
            in.remove(0);
        }
    }

    /**
     * 根据字节list获取UTF-8编码的String
     *
     * @param list 字节list
     * @return UTF-8编码的String
     */
    private String byte2String(List<Byte> list) {
        byte[] info = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            info[i] = list.get(i);
        }
        return new String(info, StandardCharsets.UTF_8);
    }

    /**
     * 当前HttpRequest的解析状态
     */
    private enum State {
        SKIP_CONTROL_CHARS,
        READ_INITIAL,
        READ_HEADER,
        READ_VARIABLE_LENGTH_CONTENT,
        READ_FIXED_LENGTH_CONTENT,
        READ_CHUNK_SIZE,
        READ_CHUNKED_CONTENT,
        READ_CHUNK_DELIMITER,
        READ_CHUNK_FOOTER,
        BAD_MESSAGE,
        UPGRADED
    }
}
