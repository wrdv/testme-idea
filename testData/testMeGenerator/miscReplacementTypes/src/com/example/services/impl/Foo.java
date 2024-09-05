package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import java.io.File;

public class Foo{

    private FooFighter fooFighter;

    Class clazz;

    public File fight(Fire withFire, File xFiles) {
        System.out.println(xFiles.getAbsoluteFile());
        return xFiles;
    }

    public Class study(Class inClazz) {
        System.out.println(inClazz);
        System.out.println(clazz);
        return clazz;
    }
}
<caret>