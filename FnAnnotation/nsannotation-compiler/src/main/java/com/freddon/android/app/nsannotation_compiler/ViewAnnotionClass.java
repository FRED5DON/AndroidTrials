package com.freddon.android.app.nsannotation_compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by fred on 2018/2/27.
 */

public class ViewAnnotionClass {

    static class TypeUtil {
        public final static ClassName finderType = ClassName.get("com.freddon.android.app.nsannotation_api", "IFinder");
        public final static ClassName binderType = ClassName.get("com.freddon.android.app.nsannotation_api", "IViewBinder");
    }

    private TypeElement typeElement;
    private Elements elementUtils;
    List<ViewField> fields;

    public ViewAnnotionClass(TypeElement typeElement, Elements elementUtils) {
        this.typeElement = typeElement;
        this.elementUtils = elementUtils;
        if (fields == null) {
            fields = new ArrayList<>();
        } else {
            fields.clear();
        }
    }

    public void addField(ViewField viewField) {
        fields.add(viewField);
    }

    public JavaFile generateFile() {
        //生成 IViewBinder api方法

//        public interface IViewBinder {
//
//        public void bindView(Object target, Object object, IFinder finder) ;
//
//    void unbind(Object target);}

        MethodSpec.Builder finderBuilder = MethodSpec.methodBuilder("bindView")
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(typeElement.asType()), "target")
                .addParameter(TypeName.OBJECT, "object")
                .addParameter(TypeUtil.finderType, "finder");
        //方法体
        for (ViewField field : fields){
            finderBuilder.addStatement("target.$N = ($T)(finder.findView(object, $L))",
                    field.getFieldName(), ClassName.get(field.getFieldType()),
                    field.getResId());
        }


        //方法2  void unbind(Object target);
        MethodSpec.Builder unbindbuilder = MethodSpec.methodBuilder("unbind")
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(typeElement.asType()), "target");
//                .addParameter(TypeName.OBJECT, "target")
        //方法体
        for (ViewField field : fields) {
            unbindbuilder.addStatement("target.$N = null",
                    field.getFieldName());
        }

        //生成类
        TypeSpec clazz = TypeSpec.classBuilder(typeElement.getSimpleName() + "$$IViewBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(
                        TypeUtil.binderType,
                        TypeName.get(typeElement.asType())))
                .addMethod(finderBuilder.build())
                .addMethod(unbindbuilder.build())
                .build();

        String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        return JavaFile.builder(packageName, clazz).

                build();
    }
}
