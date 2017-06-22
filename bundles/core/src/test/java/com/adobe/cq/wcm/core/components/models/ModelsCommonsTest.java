/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.adobe.cq.wcm.core.components.models;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.adobe.cq.wcm.core.components.models.form.Button;
import com.adobe.cq.wcm.core.components.models.form.Container;
import com.adobe.cq.wcm.core.components.models.form.Field;
import com.adobe.cq.wcm.core.components.models.form.OptionItem;
import com.adobe.cq.wcm.core.components.models.form.Options;

import static org.junit.Assert.fail;

public class ModelsCommonsTest {

    private static final Map<String, Object> MODELS = new HashMap<String, Object>() {{
        put(Breadcrumb.class.getName(), new Breadcrumb() {});
        put(Image.class.getName(), new Image() {});
        put(com.adobe.cq.wcm.core.components.models.List.class.getName(), new com.adobe.cq.wcm.core.components.models.List() {});
        put(NavigationItem.class.getName(), new NavigationItem() {});
        put(Page.class.getName(), new Page() {});
        put(SocialMediaHelper.class.getName(), new SocialMediaHelper() {});
        put(Text.class.getName(), new Text() {});
        put(Title.class.getName(), new Title() {});
        put(Button.class.getName(), new Button() {});
        put(Container.class.getName(), new Container() {});
        put(Field.class.getName(), new Field() {});
        put(OptionItem.class.getName(), new OptionItem() {});
        put(Options.class.getName(), new Options() {});
        put(com.adobe.cq.wcm.core.components.models.form.Text.class.getName(), new com.adobe.cq.wcm.core.components.models.form.Text() {});
    }};

    @Test
    public void testDefaultBehaviour() throws Exception {
        Class[] models = getClasses("com.adobe.cq.wcm.core.components.models");
        StringBuilder errors = new StringBuilder();
        for (Class clazz : models) {
            if (clazz.isInterface() && !clazz.getName().contains("package-info")) {
                Object instance = MODELS.get(clazz.getName());
                if (instance == null) {
                    fail("Expected to have an instance for " + clazz.getName() + " in the MODELS map.");
                }
                Method[] methods = clazz.getMethods();
                for (Method m : methods) {
                    if (!m.isDefault()) {
                        errors.append("Method ").append(m.toString()).append(" was not marked as default.\n");
                    }
                    Throwable t = null;
                    try {
                        m.invoke(instance);
                    } catch (InvocationTargetException e) {
                        t = e.getCause();
                    }
                    if (t == null || !(t instanceof UnsupportedOperationException)) {
                        errors.append("Expected method ").append(m.toString()).append(" to throw an ").append(UnsupportedOperationException
                                .class.getName()).append(".\n");
                    }
                }
            }
        }
        if (errors.length() > 0) {
            errors.insert(0, "\n");
            fail(errors.toString());
        }
    }

    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (!file.getName().contains(".")) {
                        classes.addAll(findClasses(file, packageName + "." + file.getName()));
                    }
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

}
