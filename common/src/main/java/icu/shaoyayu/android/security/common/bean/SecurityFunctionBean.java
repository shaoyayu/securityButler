package icu.shaoyayu.android.security.common.bean;

/**
 * @author shaoyayu
 * 封装功能模块
 */
public class SecurityFunctionBean {
    //功能的图标
    private int icoAddress;
    //功能是不是含有警告
    private int icoWarning;
    //功能的名字
    private String functionName;
    //功能的介绍
    private String functionInfo;
    //需要转至的SecurityFunctionActivity的子类
    private Class<?> securityFunctionActivityClass;

    public int getIcoAddress() {
        return icoAddress;
    }

    public void setIcoAddress(int icoAddress) {
        this.icoAddress = icoAddress;
    }

    public int getIcoWarning() {
        return icoWarning;
    }

    public void setIcoWarning(int icoWarning) {
        this.icoWarning = icoWarning;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionInfo() {
        return functionInfo;
    }

    public void setFunctionInfo(String functionInfo) {
        this.functionInfo = functionInfo;
    }

    public Class<?> getSecurityFunctionActivityClass() {
        return securityFunctionActivityClass;
    }

    public void setSecurityFunctionActivityClass(Class<?> securityFunctionActivityClass) {
        this.securityFunctionActivityClass = securityFunctionActivityClass;
    }

    public SecurityFunctionBean(int icoAddress, int icoWarning, String functionName, String functionInfo, Class<?> securityFunctionActivityClass) {
        this.icoAddress = icoAddress;
        this.icoWarning = icoWarning;
        this.functionName = functionName;
        this.functionInfo = functionInfo;
        this.securityFunctionActivityClass = securityFunctionActivityClass;
    }

    public SecurityFunctionBean(int icoAddress, String functionName, String functionInfo, Class<?> securityFunctionActivityClass) {
        this.icoAddress = icoAddress;
        this.functionName = functionName;
        this.functionInfo = functionInfo;
        this.securityFunctionActivityClass = securityFunctionActivityClass;
    }

    public SecurityFunctionBean() {
    }
}
