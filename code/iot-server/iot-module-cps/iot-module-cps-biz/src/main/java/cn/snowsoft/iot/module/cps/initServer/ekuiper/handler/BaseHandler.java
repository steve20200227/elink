package cn.snowsoft.iot.module.cps.initServer.ekuiper.handler;


import cn.snowsoft.iot.module.cps.initServer.ekuiper.EkuiperProperties;

public abstract class BaseHandler  {
    protected BaseHandler nextHandler;

    public void setNextHandler(BaseHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handleRequest(EkuiperProperties ekuiperProperties);

}
