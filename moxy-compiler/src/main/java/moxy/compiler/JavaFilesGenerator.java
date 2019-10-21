package moxy.compiler;

import com.squareup.javapoet.JavaFile;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface JavaFilesGenerator<T> {

    @NotNull
    List<JavaFile> generate(T input);
}

