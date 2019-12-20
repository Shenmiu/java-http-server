package cn.edu.nju.example.demo.exception;

public class NotSupportedHttpMethodException extends Exception {

    @Override
    public String getMessage() {
        return "Not supported http method";
    }
}
