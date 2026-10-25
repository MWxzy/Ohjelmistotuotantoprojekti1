import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public final class I18n {

    public static final String BUNDLE_NAME = "MessagesBundle";

    private static ResourceBundle bundle = load(Locale.US);

    private I18n() {
    }

    public static ResourceBundle load(Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_NAME, locale, new Utf8Control());
    }

    public static void setLocale(Locale locale) {
        bundle = load(locale);
    }

    public static Locale getLocale() {
        return bundle.getLocale();
    }

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static boolean contains(String key) {
        return bundle.containsKey(key);
    }

    public static String format(String key, Object... arguments) {
        return String.format(bundle.getLocale(), get(key), arguments);
    }

    private static final class Utf8Control extends ResourceBundle.Control {

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                        ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {

            if (!"java.properties".equals(format)) {
                return super.newBundle(baseName, locale, format, loader, reload);
            }

            String resourceName = toResourceName(toBundleName(baseName, locale), "properties");
            InputStream input = loader.getResourceAsStream(resourceName);
            if (input == null) {
                return null;
            }

            try (InputStream in = input;
                 Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                return new PropertyResourceBundle(reader);
            }
        }
    }
}
