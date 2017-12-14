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

import java.io.File;

import com.github.eclipse.nodejs.core.NodeJsCorePlugin;
import com.github.eclipse.nodejs.core.preferences.IPreferenceConstants;



/**
 * @author alpapad
 */
public class DefNodeJs extends NodeJs {

    protected DefNodeJs() {
        super(NodeJsCorePlugin.TsCorePluginId, "PREFERENCES", null, null, null, 8192);
    }

    @Override
    public File getNodeJs() {
        return new File(NodeJsCorePlugin.getDefault().getPreferenceStore().getString(IPreferenceConstants.GENERAL_NODE_PATH));
    }

    @Override
    public File getNpm() {
        return new File(NodeJsCorePlugin.getDefault().getPreferenceStore().getString(IPreferenceConstants.GENERAL_NPM_PATH));
    }
}
