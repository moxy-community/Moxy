package io.moxy.compiler;

import com.squareup.javapoet.JavaFile;

import java.util.List;

public abstract class JavaFilesGenerator<T> {

    public abstract List<JavaFile> generate(T input);
}

