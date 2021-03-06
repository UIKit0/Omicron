package com.lyndir.omicron.api;

import com.lyndir.lhunath.opal.system.util.ConversionUtils;


/**
 * @author lhunath, 2013-08-13
 */
public class ChangeInt {

    private final int from;
    private final int to;

    ChangeInt(final int from, final int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int delta() {
        return getTo() - getFrom();
    }

    public static From from(final Integer from) {
        return from( ConversionUtils.toIntegerNN( from ) );
    }

    public static From from(final int from) {
        return new From( from );
    }

    public static class From {

        private final int from;

        From(final int from) {
            this.from = from;
        }

        public ChangeInt to(final Integer to) {
            return to( ConversionUtils.toIntegerNN( to ) );
        }

        public ChangeInt to(final int to) {
            return new ChangeInt( from, to );
        }
    }
}
