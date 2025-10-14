package cn.snowsoft.iot.module.cps.initServer.emqx.handler;


import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;

public abstract class BaseHandler  {
    protected BaseHandler nextHandler;

    public void setNextHandler(BaseHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleRequest(EmqxProperties emqxProperties);

}
