package de.fhe.ai.helper;

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
}