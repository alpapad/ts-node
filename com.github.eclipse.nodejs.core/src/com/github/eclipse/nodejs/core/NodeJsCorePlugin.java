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

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

import com.github.eclipse.nodejs.core.node.NodeJs;
import com.github.eclipse.nodejs.core.node.NodeJsManager;

/**
 * 
 * @author alpapad
 */
public class NodeJsCorePlugin extends Plugin {

	public static final String PluginId = "com.github.eclipse.nodejs.core";

	public static final String TsCorePluginId = "com.github.eclipse.ts.core";
	
	private static NodeJsCorePlugin PLUGIN;

	private IPreferenceStore preferenceStore;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		PLUGIN = this;

		NodeJs[] nodes = NodeJsManager.getManager().getNodejsInstalls();
		if (nodes != null) {
			for (NodeJs node : nodes) {
				System.err.println("Node :" + node.getNodeJs());
				System.err.println("NPM  :" + node.getNpm());
				System.err.println("TSLIB:" + node.getTsLibFolder());
			}
		} else {
			System.err.println("No Node???");
		}

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		try {

			PLUGIN = null;

		} finally {
			super.stop(context);
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static NodeJsCorePlugin getDefault() {
		return PLUGIN;
	}

	public IPreferenceStore getPreferenceStore() {
		// Create the preference store lazily.
		if (preferenceStore == null) {
			preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, getBundle().getSymbolicName());

		}
		return preferenceStore;
	}

}
