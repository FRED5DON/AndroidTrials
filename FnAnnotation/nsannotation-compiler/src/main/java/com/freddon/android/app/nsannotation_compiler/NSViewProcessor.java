package com.freddon.android.app.nsannotation_compiler;

import com.freddon.android.app.nsannotations.ViewById;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class NSViewProcessor extends AbstractProcessor {

    private TreeMap<String, ViewAnnotionClass> annotionClassesMap;
    private Messager $messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        annotionClassesMap = new TreeMap<>();
        $messager=processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        annotionClassesMap.clear();
        for (Element element : roundEnv.getElementsAnnotatedWith(ViewById.class)) {
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                String className = typeElement.getQualifiedName().toString();
                ViewAnnotionClass viewAnnotionClass = annotionClassesMap.get(className);
                if (viewAnnotionClass==null){
                    viewAnnotionClass=new ViewAnnotionClass(typeElement, processingEnv.getElementUtils());
                    annotionClassesMap.put(className,viewAnnotionClass);
                }
                ViewField viewField=new ViewField(element);
                //add field
                viewAnnotionClass.addField(viewField);

        }
        $messager.printMessage(Diagnostic.Kind.NOTE,">>>>>>>>>>>>>>>>>>>>>>");

        for(ViewAnnotionClass viewAnnotionClass :annotionClassesMap.values()){
            try {
                viewAnnotionClass.generateFile().writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationTypes = new HashSet<>();
        supportedAnnotationTypes.add(ViewById.class.getCanonicalName());
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
