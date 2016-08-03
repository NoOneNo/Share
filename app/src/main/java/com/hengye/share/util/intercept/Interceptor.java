package com.hengye.share.util.intercept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yuhy on 16/7/20.
 */
public class Interceptor {

    protected Interceptor() {
        interceptions = new ArrayList<>();
    }

    public static Interceptor create() {
        return new Interceptor();
    }

    public static Interceptor create(Interception interception) {
        return create(null, interception);
    }

    public static Interceptor create(Interception... interceptions) {
        return create(null, interceptions);
    }

    public static Interceptor create(Action action, Interception interception) {
        Interceptor interceptor = create();
        interceptor.add(interception);
        interceptor.setAction(action);
        return interceptor;
    }

    public static Interceptor create(Action action, Interception... interceptions) {
        Interceptor interceptor = create();
        interceptor.addAll(interceptions);
        interceptor.setAction(action);
        return interceptor;
    }

    List<Interception> interceptions;
    Action action;
    int index = 0;
    boolean isIntercept = false;

    public List<Interception> getInterceptions(){
        return interceptions;
    }

    public Action getAction(){
        return action;
    }

    public Interceptor add(Interception interception) {
        interceptions.add(interception);
        return this;
    }

    public Interceptor addAll(Interception... interceptions) {
        this.interceptions.addAll(Arrays.asList(interceptions));
        return this;
    }

    public Interceptor setAction(Action action){
        this.action = action;
        return this;
    }

    public void destroy(){
        interceptions.clear();
        action = null;
    }

    public void start(){
        index = 0;
        isIntercept = false;
        resume();
    }

    /**
     * 开启拦截
     */
    public void resume() {
        while (!interceptions.isEmpty() && index < interceptions.size()) {
            Interception interception = interceptions.get(index);

            if (interception.isIntercept()) {
                isIntercept = true;
                return;
            }
            index++;
//            interceptions.remove(0);
        }
        tryRunAction();
    }

    /**
     * 表示当前拦截已经通过,可以进行下一个拦截的操作
     */
    public void next() {
        index++;
        resume();
    }

    /**
     * 如果发生过拦截, 则返回true
     * @return
     */
    public boolean isIntercept(){
        return isIntercept;
    }

    protected void tryRunAction(){
        if(action != null){
            runAction();
        }
    }

    protected void runAction(){
        action.run();
    }

    public void removeCurrentInterception(){
        if(!interceptions.isEmpty()){
            interceptions.remove(index);
        }
    }

}
