package agent;

public abstract class EnvVar {

    public static String getEnvVar(String varName, String defaultValue) {
        String value = System.getenv(varName);
        return (value != null) ? value : defaultValue;
    }

    public static String getEnvVar(String varName) {
        return System.getenv(varName);
    }
}
