package agent;

/**
 * Abstract class EnvVarUtils that provides utility methods for working with
 * environment variables.
 */
public abstract class EnvVarUtils {

    /**
     * Retrieves the value of the specified environment variable.
     *
     * @param varName The name of the environment variable to retrieve.
     * @return The value of the environment variable as a String, or null if
     *         the variable does not exist.
     */
    public static String getEnvVar(String varName) {
        return System.getenv(varName);
    }
}
