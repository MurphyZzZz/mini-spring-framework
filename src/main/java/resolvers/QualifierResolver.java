package resolvers;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;

public class QualifierResolver {

    static public String getNameOnAnnotationQualifier(Class<?> clz) {
        Annotation[] declaredAnnotations = clz.getDeclaredAnnotations();
        for (Annotation annotation: declaredAnnotations) {
            Class<?> annotationClz = annotation.annotationType();
            // 以第一个实现qualifier的annotation为准
            if (annotationClz.isAnnotationPresent(Qualifier.class)){
                return annotationClz.getSimpleName();
            }
        }
        return null;
    }
}
