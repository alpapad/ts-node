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

package com.github.eclipse.nodejs.core.node;

import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Platform;

/**
 * @author alpapad
 */
public class Ts {


    public static String getGlobalTypingsCacheLocation(String versionMajorMinor) {

        switch (Platform.getOS()) {
            case Platform.OS_WIN32: {
                return getWindowsCacheLocation(versionMajorMinor);
            }
            case Platform.OS_AIX:
            case Platform.OS_HPUX:
            case Platform.OS_LINUX:
            case Platform.OS_MACOSX:
            case Platform.OS_QNX:
            case Platform.OS_SOLARIS: {
                String cacheLocation = getNonWindowsCacheLocation(Platform.OS_MACOSX.equals(Platform.getOS()));
                return combinePaths(combinePaths(cacheLocation, "typescript"), versionMajorMinor);
            }
            default:
                throw new RuntimeException("unsupported platform '" + Platform.getOS() + "'");

        }
    }

    static String getWindowsCacheLocation(String versionMajorMinor) {
        String basePath = System.getProperty("java.io.tmpdir");
        if(getEv("LOCALAPPDATA") != null) {
            basePath = getEv("LOCALAPPDATA");
        } else {
            if(getEv("APPDATA") != null) {
                basePath = getEv("APPDATA");
            } else {
                String homePath = System.getProperty("user.home");
                if(StringUtils.isNotEmpty(homePath)) {
                    basePath = homePath;
                } else {
                    if(getEv("USERPROFILE") != null) {
                        basePath = getEv("USERPROFILE");
                    } else {
                        if(getEv("HOMEDRIVE") != null && getEv("HOMEPATH") != null) {
                            basePath = combinePaths(getEv("HOMEDRIVE") ,getEv("HOMEPATH"));
                        }
                    }
                }
            }
        }
        return combinePaths(combinePaths(normalizeSlashes(basePath), "Microsoft/TypeScript"), versionMajorMinor);
    }

    static String getNonWindowsCacheLocation(boolean platformIsDarwin) {
        String xdg = getEv("XDG_CACHE_HOME");
        if (xdg != null) {
            return xdg;
        }

        String homePath = System.getProperty("user.home");

        String cacheFolder = platformIsDarwin ? "Library/Caches" : ".cache";
        return combinePaths(normalizeSlashes(homePath), cacheFolder);
    }

    static String combinePaths(String a, String b) {
        return Paths.get(a, b).toString();
    }

    static String normalizeSlashes(String a) {
        return Paths.get(a).normalize().toString();
    }

    private static String getEv(String key) {
        String p = System.getenv().get(key);
        if (StringUtils.isNotEmpty(p)) {
            return p;
        }
        return null;
    }
}
