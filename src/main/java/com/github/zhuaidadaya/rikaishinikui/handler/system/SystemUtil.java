package com.github.zhuaidadaya.rikaishinikui.handler.system;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import org.apache.commons.io.*;
import org.apache.logging.log4j.*;
import oshi.software.os.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.security.*;
import java.util.*;

public class SystemUtil {
    static final Logger LOGGER = LogManager.getLogger();

    public static OperatingSystem getOperatingSystem() {
        String string = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (string.contains("win")) {
            return OperatingSystem.WINDOWS;
        }
        if (string.contains("mac")) {
            return OperatingSystem.OSX;
        }
        if (string.contains("solaris")) {
            return OperatingSystem.SOLARIS;
        }
        if (string.contains("sunos")) {
            return OperatingSystem.SOLARIS;
        }
        if (string.contains("linux")) {
            return OperatingSystem.LINUX;
        }
        if (string.contains("unix")) {
            return OperatingSystem.LINUX;
        }
        return OperatingSystem.UNKNOWN;
    }

    public enum OperatingSystem {
        LINUX("linux"), SOLARIS("solaris"), WINDOWS("windows") {
            @Override
            protected String[] getURLOpenCommand(URL url) {
                return new String[]{"rundll32", "url.dll,FileProtocolHandler", url.toString()};
            }
        }, OSX("mac") {
            @Override
            protected String[] getURLOpenCommand(URL url) {
                return new String[]{"open", url.toString()};
            }
        }, UNKNOWN("unknown");

        private final String name;

        OperatingSystem(String name) {
            this.name = name;
        }

        public void open(URI uri) {
            try {
                this.open(uri.toURL());
            } catch (MalformedURLException malformedURLException) {
                LOGGER.error("Couldn't open uri '{}'", uri, malformedURLException);
            }
        }

        public void open(URL url) {
            try {
                Process process = AccessController.doPrivileged((PrivilegedAction<Process>) () -> {
                    try {
                        return Runtime.getRuntime().exec(this.getURLOpenCommand(url));
                    } catch (IOException e) {
                        return null;
                    }
                });
                for (String string : IOUtils.readLines(process.getErrorStream(), StandardCharsets.UTF_8)) {
                    LOGGER.error(string);
                }
                process.getInputStream().close();
                process.getErrorStream().close();
                process.getOutputStream().close();
            } catch (Exception process) {
                LOGGER.error("Couldn't open url '{}'", url, process);
            }
        }

        protected String[] getURLOpenCommand(URL url) {
            String string = url.toString();
            if ("file".equals(url.getProtocol())) {
                string = string.replace("file:", "file://");
            }
            return new String[]{"xdg-open", string};
        }

        public void open(File file) {
            try {
                this.open(file.toURI().toURL());
            } catch (MalformedURLException malformedURLException) {
                LOGGER.error("Couldn't open file '{}'", file, malformedURLException);
            }
        }

        public void open(String uri) {
            try {
                this.open(new URI(uri).toURL());
            } catch (IllegalArgumentException | MalformedURLException | URISyntaxException exception) {
                LOGGER.error("Couldn't open uri '{}'", uri, exception);
            }
        }

        public String getName() {
            return this.name;
        }
    }

}
