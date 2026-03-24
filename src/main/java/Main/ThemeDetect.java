package Main;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ThemeDetect {
    public static boolean isDarkMode() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                return isDarkModeWindows();
            } else if (os.contains("mac")) {
                return isDarkModeMac();
            } else {
                return isDarkModeLinux();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // fallback
    }

    // ---------------- WINDOWS ----------------
    private static boolean isDarkModeWindows() {
        try {
            Process process = Runtime.getRuntime().exec(
                    "reg query HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize /v AppsUseLightTheme"
            );

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("AppsUseLightTheme")) {
                    return line.trim().endsWith("0"); // 0 = dark
                }
            }
        } catch (Exception ignored) {}

        return false;
    }

    // ---------------- MAC ----------------
    private static boolean isDarkModeMac() {
        try {
            Process process = Runtime.getRuntime().exec(
                    "defaults read -g AppleInterfaceStyle"
            );

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line = reader.readLine();
            return line != null && line.equalsIgnoreCase("Dark");

        } catch (Exception e) {
            return false; // se falhar, assume light
        }
    }

    // ---------------- LINUX ----------------
    private static boolean isDarkModeLinux() {
        try {
            // GNOME (mais comum)
            Process process = Runtime.getRuntime().exec(
                    "gsettings get org.gnome.desktop.interface gtk-theme"
            );

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String theme = reader.readLine();

            if (theme != null) {
                theme = theme.toLowerCase();
                return theme.contains("dark");
            }

        } catch (Exception ignored) {}

        return false;
    }
}
