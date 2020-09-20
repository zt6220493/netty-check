package com.schoolguard.messages.processors.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by Rogers on 15-5-18.
 *
 * Storm topology submitter需要bolt实例能被序列化，injector及被注入的对象不能保证能被序列化，
 * 而bolt构造方法被storm控制，因此只能在worker运行时初始化injector
 */
public class InjectorProvider {
    private static Injector injector;

    public static Injector get() {
        if (injector == null) {
            synchronized (InjectorProvider.class) {
                if (injector == null) {
                    injector = Guice.createInjector(
                            new AppModule());
                }
            }
        }
        return injector;
    }
}
