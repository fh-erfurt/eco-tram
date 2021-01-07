package de.fhe.ai.model;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.fhe.ai.helper.Utils;
import de.fhe.ai.manager.EventManager;

public abstract class ModelBase {

    private final int id;
    protected final EventManager eventManager;

    public ModelBase(int id, EventManager eventManager) {
        this.id = id;
        this.eventManager = eventManager;
    }

    public int getId() {
        return id;
    }

    protected String getString(ModelBase modelBase) {
        // get underlying type of the deriving model
        Class<?> modelType = modelBase.getClass();
        List<Field> allFields = new ArrayList<>();

        // get all fields dynamically by moving up the class hierarchy
        Class<?> superType = modelType;
        while (superType != null) {
            Collections.addAll(allFields, superType.getDeclaredFields());
            superType = superType.getSuperclass();
        }

        // prepare output
        String output = Utils.getShortClassName(modelType) + "\n";

        // appaned all fields with name and value and one indentation
        for (Field field : allFields) {
            output += "    " + field.getName() + ": ";
            try {
                field.setAccessible(true);
                var value = field.get(modelBase);

                // append field name and value
                if (value != null) {
                    // view count on collections and id on moduleBase (prevents nesting)
                    // else toString()
                    if (value instanceof Collection) {
                        output += "Collection (Count: " + ((Collection<?>) value).size() + ")";
                    } else if (value instanceof String) {
                        output += "'" + value + "'";
                    } else if (value instanceof ModelBase) {
                        output += Utils.getShortClassName(value.getClass()) + " (Id: " + ((ModelBase) value).getId()
                                + ")";
                    } else {
                        output += value.toString();
                    }
                } else {
                    output += "null";
                }
            } catch (Exception ex) {
                output += "Unknown";
            }
            output += "\n";
        }

        return output;
    }
}