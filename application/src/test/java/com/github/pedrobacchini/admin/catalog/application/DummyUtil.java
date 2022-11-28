package com.github.pedrobacchini.admin.catalog.application;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.util.List;

public class DummyUtil {

    private DummyUtil() {
        throw new IllegalStateException("Utility class");
    }

    private final static EasyRandomParameters parameters = new EasyRandomParameters();
    private final static EasyRandom easyRandom = new EasyRandom(parameters);

    public static <T> T dummyObject(Class<T> clazz) {
        return easyRandom.nextObject(clazz);
    }

    public static <T> List<T> dummyObjects(Class<T> clazz, int streamSize) {
        return easyRandom.objects(clazz, streamSize).toList();
    }

}
