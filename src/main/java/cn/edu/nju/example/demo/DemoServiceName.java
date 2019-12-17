package cn.edu.nju.example.demo;

/**
 * 枚举 demo 中需要的 service
 */
public enum DemoServiceName {
    NOT_SUPPORTED("not supported"),
    METHOD("method"),
    STATUS("status"),
    MIME("mime");

    private String value;

    DemoServiceName(String value) {
        this.value = value;
    }

    public static DemoServiceName getServiceName(String name) {
        for (DemoServiceName cur : DemoServiceName.values()) {
            if (cur.value.equals(name)) {
                return cur;
            }
        }
        return NOT_SUPPORTED;
    }
}
