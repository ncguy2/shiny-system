package net.ncguy.foundation.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logger {

    static ConcurrentLinkedQueue<LogItem> queue = new ConcurrentLinkedQueue<>();

    public static LogLevel outputLevel = LogLevel.WARN;

    public static LogItem log(LogLevel level, String message) {
        LogItem e = new LogItem(level, message);
        queue.add(e);
        if(level.ordinal() >= outputLevel.ordinal()) {
            System.out.println(e.toString());
        }
        return e;
    }

    public static void log(Throwable e) {
        log(LogLevel.ERROR, e);
    }
    public static void log(LogLevel level, Throwable e) {
        LogItem log = log(level, e.getLocalizedMessage());
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            log.add(stackTraceElement.toString());
        }
    }

    public static class LogItem {
        public final LogLevel level;
        public final long time;
        public final String message;
        public final List<String> substrings;

        public LogItem(LogLevel level, String message) {
            this.level = level;
            this.time = System.currentTimeMillis();
            this.message = message;
            substrings = new ArrayList<>();
        }

        public void add(String substring) {
            substrings.add(substring);
        }

        @Override
        public String toString() {
            DateFormat format = new SimpleDateFormat("HH:mm:ss_SSS");
            StringBuilder str = new StringBuilder(String.format("%s [%s] >> %s", format.format(new Date(time)), level.paddedName(), message));
            if(!substrings.isEmpty()) {
                for (String substring : substrings) {
                    str.append("\t").append(substring);
                }
            }
            return str.toString();
        }
    }

    public static enum LogLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL,
        ;

        private static int longestNameLength = 0;

        LogLevel() {
            init();
        }

        void init() {
            int len = name().length();
            if(len > longestNameLength) {
                longestNameLength = len;
            }
        }

        public String paddedName() {
            StringBuilder s = new StringBuilder(name());
            while(s.length() < longestNameLength) {
                s.append(" ");
            }
            return s.toString();
        }

        public void log(String message) {
            Logger.log(this, message);
        }
    }

}
