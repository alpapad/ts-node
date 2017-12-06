/*
 * Copyright respective authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.eclipse.nodejs.core;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.eclipse.nodejs.core.preferences.IPreferenceConstants;
import com.google.common.base.Splitter;
import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.Lists;

/**
 * 
 * @author alpapad
 *
 */
public class TypeScriptCoreSettingsInitializer extends AbstractPreferenceInitializer {

    private static final String OS_NAME = StandardSystemProperty.OS_NAME.value();
    private static final Splitter PATH_SPLITTER = Splitter.on(File.pathSeparatorChar);

    public TypeScriptCoreSettingsInitializer() {
        super();
    }

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = NodeJsCorePlugin.getDefault().getPreferenceStore();
        store.setDefault(IPreferenceConstants.GENERAL_NODE_PATH, findNodejs());
        store.setDefault(IPreferenceConstants.GENERAL_NPM_PATH, findNpm() );
    }

    public static String findNodejs() {
        String nodeFileName = getNodeFileName();
        String path = System.getenv("PATH");
        List<String> directories = Lists.newArrayList(PATH_SPLITTER.split(path));

        // ensure /usr/local/bin is included for OS X
        if (OS_NAME.startsWith("Mac OS X")) {
            directories.add("/usr/local/bin");
        }

        // search for Node.js in the PATH directories
        for (String directory : directories) {
            File nodeFile = new File(directory, nodeFileName);

            if (nodeFile.exists()) {
                return nodeFile.getAbsolutePath();
            }
        }

        return "";
    }

    public static String findNpm() {
        String nodeFileName = getNpmFileName();
        String path = System.getenv("PATH");
        List<String> directories = Lists.newArrayList(PATH_SPLITTER.split(path));

        // ensure /usr/local/bin is included for OS X
        if (OS_NAME.startsWith("Mac OS X")) {
            directories.add("/usr/local/bin");
        }

        // search for Node.js in the PATH directories
        for (String directory : directories) {
            File nodeFile = new File(directory, nodeFileName);

            if (nodeFile.exists()) {
                return nodeFile.getAbsolutePath();
            }
        }

        return "";
    }
    private static String getNodeFileName() {
        if (OS_NAME.startsWith("Windows")) {
            return "node.exe";
        }

        return "node";
    }

    private static String getNpmFileName() {
        if (OS_NAME.startsWith("Windows")) {
            return "npm.cmd";
        }

        return "npm";
    }
}
