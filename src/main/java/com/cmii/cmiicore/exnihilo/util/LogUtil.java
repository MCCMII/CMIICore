package com.cmii.cmiicore.exnihilo.util;

import com.cmii.cmiicore.Reference;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class LogUtil {
    private static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    private static PrintWriter logWriter;

    private LogUtil() {
    }

    public static void log(Level level, Object object) {
        String message = object == null ? "null" : object.toString();
        String preLine = new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + " [" + level.name() + "] ";

        for (String line : message.split("\\n")) {
            LOGGER.log(level, line);
            if (logWriter != null) {
                logWriter.println(preLine + line);
            }
        }

        if (logWriter != null) {
            logWriter.flush();
        }
    }

    public static <T extends Throwable> T throwing(T thrown) {
        return throwing(Level.ERROR, thrown);
    }

    public static <T extends Throwable> T throwing(Level level, T thrown) {
        log(level, ExceptionUtils.getStackTrace(thrown));
        return thrown;
    }

    public static void fatal(Object object) {
        log(Level.FATAL, object);
    }

    public static void error(Object object) {
        log(Level.ERROR, object);
    }

    public static void error(Object object, Throwable throwable) {
        log(Level.ERROR, object);
        LOGGER.error(throwable);
        if (logWriter != null) {
            throwable.printStackTrace(logWriter);
            logWriter.flush();
        }
    }

    public static void warn(Object object) {
        log(Level.WARN, object);
    }

    public static void info(Object object) {
        log(Level.INFO, object);
    }

    public static void debug(Object object) {
        log(Level.DEBUG, object);
    }

    public static void trace(Object object) {
        log(Level.TRACE, object);
    }

    public static void setup() {
        File logDirectory = new File("./logs/cmiicore/exnihilo/");
        if (!logDirectory.mkdirs() && !logDirectory.isDirectory()) {
            LOGGER.warn("Unable to create Ex Nihilo log directory: {}", logDirectory.getAbsolutePath());
            return;
        }

        String baseName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File logFile;
        int i = 0;
        while ((logFile = new File(logDirectory, baseName + "-" + i + ".log")).exists()) {
            i++;
        }

        try {
            if (!logFile.createNewFile()) {
                LOGGER.warn("Unable to create Ex Nihilo log file: {}", logFile.getAbsolutePath());
                return;
            }
            logWriter = new PrintWriter(new FileWriter(logFile));
        } catch (IOException e) {
            LOGGER.error("Unable to initialize Ex Nihilo log file", e);
        }
    }
}
