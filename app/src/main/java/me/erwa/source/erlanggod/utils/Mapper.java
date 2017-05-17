package me.erwa.source.erlanggod.utils;

import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Map;

/**
 * Author: xumin
 * Date: 2016/8/10
 */
@SuppressWarnings("unused")
public class Mapper {
    private Object mObject;
    private Object mUnexpectedKey;

    private Mapper(Object object, Object unexpectedKey) {
        mObject = object;
        mUnexpectedKey = unexpectedKey;
    }

    public static Mapper from(Object map) {
        if (map != null && map instanceof Map) {
            return new Mapper(map, null);
        } else {
            throw new IllegalStateException();
        }
    }

    public Mapper to(Object... keys) {
        if (mUnexpectedKey != null) {
            return new Mapper(mObject, mUnexpectedKey);
        }

        Object object = mObject;
        Object key = null;

        try {
            for (int i = 0; i < keys.length; i++) {
                key = keys[i];
                if (key instanceof Number) {
                    List l = (List) object;
                    object = l.get(((Number) key).intValue());
                } else if (key instanceof String) {
                    Map m = (Map) object;
                    Preconditions.checkArgument(m.containsKey(key));
                    object = m.get(key);
                } else {
                    throw new IllegalStateException();
                }
            }

            key = null;
        } catch (Exception e) {
            if (key == null) {
                throw new IllegalStateException("unexpected key: null");
            } else {
                object = null;
            }
        }

        return new Mapper(object, key);
    }

    public boolean getBoolean() {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return (boolean) mObject;
        } catch (Exception e) {
            throw new IllegalStateException("unexpected key: " + mUnexpectedKey, e);
        }
    }

    public int getInt() {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return ((Number) mObject).intValue();
        } catch (Exception e) {
            throw new IllegalStateException("unexpected key: " + mUnexpectedKey, e);
        }
    }

    public long getLong() {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return ((Number) mObject).longValue();
        } catch (Exception e) {
            throw new IllegalStateException("unexpected key: " + mUnexpectedKey, e);
        }
    }

    public double getDouble() {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return ((Number) mObject).doubleValue();
        } catch (Exception e) {
            throw new IllegalStateException("unexpected key: " + mUnexpectedKey, e);
        }
    }

    public String getString() {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return (String) mObject;
        } catch (Exception e) {
            throw new IllegalStateException("unexpected key: " + mUnexpectedKey, e);
        }
    }

    public List getList() {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return (List) mObject;
        } catch (Exception e) {
            throw new IllegalStateException("unexpected key: " + mUnexpectedKey, e);
        }
    }

    public Map getMap() {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return (Map) mObject;
        } catch (Exception e) {
            throw new IllegalStateException("unexpected key: " + mUnexpectedKey, e);
        }
    }

    public boolean optBoolean() {
        return optBoolean(false);
    }

    public int optInt() {
        return optInt(0);
    }

    public long optLong() {
        return optLong(0L);
    }

    public double optDouble() {
        return optDouble(Double.NaN);
    }

    public String optString() {
        return optString(null);
    }

    public List optList() {
        return optList(null);
    }

    public boolean optBoolean(boolean defaultValue) {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return (boolean) mObject;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int optInt(int defaultValue) {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return ((Number) mObject).intValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long optLong(long defaultValue) {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return ((Number) mObject).longValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double optDouble(double defaultValue) {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return ((Number) mObject).doubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String optString(String defaultValue) {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return (String) mObject;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public List optList(List defaultValue) {
        try {
            Preconditions.checkState(mUnexpectedKey == null);
            return (List) mObject;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
