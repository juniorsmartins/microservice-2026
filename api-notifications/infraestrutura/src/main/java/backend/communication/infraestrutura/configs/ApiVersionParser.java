package backend.communication.infraestrutura.configs;

public class ApiVersionParser implements org.springframework.web.accept.ApiVersionParser {

    @Override
    public Comparable parseVersion(String version) {

        // Allow "v1" or "v2" instead of "1.0" or "2.0"
        if (version.startsWith("v") || version.startsWith("V")) {
            version = version.substring(1);
        }

        // Auto-append ".0" for major versions
        if (!version.contains(".")) {
            version = version + ".0";
        }

        return version;
    }
}
