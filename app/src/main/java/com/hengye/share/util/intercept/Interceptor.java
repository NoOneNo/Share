package com.hengye.share.util.intercept;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yuhy on 16/7/20.
 */
public class Interceptor {

    private Interceptor() {
        interceptions = new ArrayList<>();
    }

    public static Interceptor create() {
        return new Interceptor();
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

    public void detroy(){
        interceptions.clear();
        action = null;
    }

    /**
     * 开启拦截
     */
    public void start() {
        while (!interceptions.isEmpty()) {
            Interception interception = interceptions.get(0);

            if (interception.isIntercept()) {
                return;
            }
            interceptions.remove(0);
        }
        runAction();
    }

    /**
     * 表示当前拦截已经通过,可以进行下一个拦截的操作
     */
    public void next() {
        if (interceptions.isEmpty()) {
            runAction();
            return;
        }
        interceptions.remove(0);
        start();
    }

    public void runAction(){
        if(action != null){
            action.run();
        }
    }

    public void removeCurrentInterception(){
        if(!interceptions.isEmpty()){
            interceptions.remove(0);
        }
    }
}
