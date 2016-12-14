package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import java.io.File;

public class Foo{

    private FooFighter fooFighter;

    public File fight(Fire withFire, File xFiles) {
        System.out.println(xFiles.getAbsoluteFile());
        return xFiles;
    }
}
<caret>