package de.fhe.ai;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.fhe.ai.model.ModelBase;

/**
 * Class with static helper methods.
 */
public final class Utils {
    private Utils() {
    }

    /**
     * Returns the name of a class without it's package names.
     * 
     * @param classType the underlying class of the object
     * @return a string representing only the class itself
     */
    public static String getShortClassName(Class<?> classType) {
        String fullClassName = classType.toString();
        return fullClassName.contains(".") && fullClassName.lastIndexOf('.') + 1 < fullClassName.length()
                ? fullClassName.substring(fullClassName.lastIndexOf('.') + 1, fullClassName.length())
                : fullClassName;
    }

    /**
     * Indents each line in a string by the given depth
     * 
     * @param stringToIndent the string to indent each line for
     * @param depth          the depth of indentation where one indentation is four
     *                       spaces
     * @return a new string with the specified indentation
     */
    public static String indentEachLine(String stringToIndent, int depth) {
        // java internal String.replace does not work for whatever reason
        // return stringToIndent.replace("\n", "\n" + indentation);

        String output = "";
        var subStr = stringToIndent.split("\n");
        for (int ii = 0; ii < depth; ii++) {
            for (int jj = 0; jj < depth; jj++) {
                output += "    ";
            }
            output += subStr[ii];
        }

        return output;
    }

    /**
     * Returns a string that represents the given model, this method will show
     * private fields, their types and some additional information if necessary.
     * This method should ideally only used for debugging as it can expose sensitive
     * information and lead to massive outputs with the correct arguments
     * 
     * @param modelBase       the model to get a verbsoe representation of
     * @param depth           the maximum depth at which to verbosely display
     * @param showCollections indicates whether collections should be completely
     *                        displayed non-primitive fields
     * @return a verbose json-like representation of the model
     */
    public static String getVerboseModelRepresentation(ModelBase modelBase, int depth) {
        return getVerboseModelRepresentation(modelBase, depth, 0);
    }

    private static String getVerboseModelRepresentation(ModelBase modelBase, int targetDepth, int currentDepth) {
        // get underlying type of the deriving model
        Class<?> modelType = modelBase.getClass();
        List<Field> allFields = new ArrayList<>();

        // get all fields by moving up the class hierarchy
        Class<?> superType = modelType;
        while (superType != null) {
            Collections.addAll(allFields, superType.getDeclaredFields());
            superType = superType.getSuperclass();
        }

        // prepare output
        StringBuilder outputBuilder = new StringBuilder(
                Utils.getShortClassName(modelType) + "(" + modelBase.getId() + ") {\n");

        // appaned all fields with name and value and one indentation
        for (Field field : allFields) {
            StringBuilder fieldLineBuilder = new StringBuilder(field.getName() + ": ");
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(modelBase);

                if (fieldValue != null) {
                    // append field name and value
                    // view count on collections
                    if (fieldValue instanceof Collection<?>) {
                        fieldLineBuilder.append("{ Count: " + ((Collection<?>) fieldValue).size() + " }");
                    } else if (fieldValue instanceof Map<?, ?>) {
                        fieldLineBuilder.append("{ Count: " + ((Map<?, ?>) fieldValue).size() + " }");
                    } else if (fieldValue instanceof ModelBase) {
                        if (targetDepth > 0) {
                            fieldLineBuilder.append(Utils.getVerboseModelRepresentation((ModelBase) fieldValue,
                                    targetDepth - currentDepth));
                        } else {
                            fieldLineBuilder.append(Utils.getShortClassName(fieldValue.getClass()) + "("
                                    + ((ModelBase) fieldValue).getId() + ") { ... }");
                        }
                    } else if (fieldValue instanceof String) {
                        fieldLineBuilder.append("'" + fieldValue + "'");
                    } else {
                        fieldLineBuilder.append(fieldValue.toString());
                    }
                } else {
                    fieldLineBuilder.append("null");
                }
            } catch (Exception ex) {
                fieldLineBuilder.append("UnknownType = UnknownValue");
            }
            // add indentation for fieldLine according to targetDepth
            outputBuilder.append(Utils.indentEachLine(fieldLineBuilder.toString(), currentDepth + 1) + "\n");
        }

        return outputBuilder.append('}').toString();
    }
}