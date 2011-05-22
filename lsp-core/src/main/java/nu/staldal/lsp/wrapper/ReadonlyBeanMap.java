/*
 * Copyright (c) 2008, Mikael Ståldal
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the author nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * Note: This is known as "the modified BSD license". It's an approved
 * Open Source and Free Software license, see
 * http://www.opensource.org/licenses/
 * and
 * http://www.gnu.org/philosophy/license-list.html
 */

package nu.staldal.lsp.wrapper;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

/**
 * Implementation of read only Map for a Java bean. This class is <em>not</em>
 * thread-safe
 *
 * @author Mikael Ståldal
 */
public class ReadonlyBeanMap implements Map<String, Object> {
    private final Object bean;

    private final Map<String, Member> cache;

    /**
     * Constructor.
     *
     * @param bean the Java bean to wrap
     */
    public ReadonlyBeanMap(Object bean) {
        this.bean = bean;
        this.cache = new HashMap<String, Member>();
    }

    private Member lookupMember(String property) {
        if (property.isEmpty()) {
            return null;
        }
        return tryToLookupMember(property, "get", "is");
    }

    private Member tryToLookupMember(final String property, final String... prefixes) {
        final Class<? extends Object> beanClass = bean.getClass();

        for (final String prefix : prefixes) {
            try {
                final String methodName = prefix + StringUtils.capitalize(property);
                return beanClass.getMethod(methodName);
            } catch (NoSuchMethodException ignored) {
            }
        }

        try {
            return beanClass.getField(property);
        } catch (NoSuchFieldException ignored) {
        }

        try {
            return beanClass.getMethod(property);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Member getMember(String property) {
        Member member = cache.get(property);
        if (member == null) {
            member = lookupMember(property);
            if (member != null) {
                cache.put(property, member);
            }
        }
        return member;
    }

    public Object get(Object key) {
        Member member = getMember((String) key);
        if (member == null) {
            return null;
        }

        try {
            if (member instanceof Method) {
                return ((Method) member).invoke(bean);
            } else if (member instanceof Field) {
                return ((Field) member).get(bean);
            } else {
                throw new Error("Unknown Member: " + member.getClass().getName());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Throwable ee = e.getCause();
            if (ee instanceof RuntimeException) {
                throw (RuntimeException) ee;
            } else {
                throw new RuntimeException(ee);
            }
        }
    }

    public boolean containsKey(Object key) {
        return getMember((String) key) != null;
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Object put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends String, ? extends Object> t) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public java.util.Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    public java.util.Collection<Object> values() {
        throw new UnsupportedOperationException();
    }

    public java.util.Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
