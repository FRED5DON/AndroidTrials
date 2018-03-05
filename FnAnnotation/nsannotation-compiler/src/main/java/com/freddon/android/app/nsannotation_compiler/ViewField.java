package com.freddon.android.app.nsannotation_compiler;

import com.freddon.android.app.nsannotations.ViewById;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.type.TypeMirror;

/**
 * Created by fred on 2018/2/27.
 */

public class ViewField {

    private TypeMirror fieldType;
    private Name fieldName;
    private int resId;

    public ViewField(Element element) {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException("Only fields can be annotated");
        }
        ViewById annotation= element.getAnnotation(ViewById.class);
        resId=annotation.value();
        fieldName=element.getSimpleName();
        fieldType=element.asType();
    }

    public TypeMirror getFieldType() {
        return fieldType;
    }

    public Name getFieldName() {
        return fieldName;
    }


    public int getResId() {
        return resId;
    }

}
