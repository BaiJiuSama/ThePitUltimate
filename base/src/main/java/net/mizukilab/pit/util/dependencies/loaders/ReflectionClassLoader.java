/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.mizukilab.pit.util.dependencies.loaders;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class ReflectionClassLoader implements PluginClassLoader {

    private static final Unsafe UNSAFE = lookupUnsafe();
    private static final long UCP_OFFSET = fieldOffset(URLClassLoader.class, "ucp");
    private static final long UCP_PATH_OFFSET;
    private static final long UCP_UNOPENED_URLS_OFFSET;

    static {
        Object probe = UNSAFE.getObject(new URLClassLoader(new URL[0]), UCP_OFFSET);
        Class<?> urlClassPathType = probe.getClass();
        UCP_PATH_OFFSET = fieldOffset(urlClassPathType, "path");
        UCP_UNOPENED_URLS_OFFSET = fieldOffset(urlClassPathType, "unopenedUrls");
    }

    private final URLClassLoader classLoader;

    public ReflectionClassLoader(Object plugin) throws IllegalStateException {
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            this.classLoader = (URLClassLoader) classLoader;
        } else {
            throw new IllegalStateException("ClassLoader is not instance of URLClassLoader");
        }
    }

    @Override
    public void loadJar(Path file) {
        try {
            appendUrl(file.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void appendUrl(URL url) {
        Object urlClassPath = UNSAFE.getObject(this.classLoader, UCP_OFFSET);

        @SuppressWarnings("unchecked")
        ArrayList<URL> path = (ArrayList<URL>) UNSAFE.getObject(urlClassPath, UCP_PATH_OFFSET);

        @SuppressWarnings("unchecked")
        Collection<URL> unopenedUrls = (Collection<URL>) UNSAFE.getObject(urlClassPath, UCP_UNOPENED_URLS_OFFSET);

        synchronized (unopenedUrls) {
            if (path.contains(url)) {
                return;
            }
            unopenedUrls.add(url);
            path.add(url);
        }
    }

    private static Unsafe lookupUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static long fieldOffset(Class<?> type, String name) {
        try {
            return UNSAFE.objectFieldOffset(type.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
