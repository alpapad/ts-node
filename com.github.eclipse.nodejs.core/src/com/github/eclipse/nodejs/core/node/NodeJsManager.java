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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;

import com.github.eclipse.nodejs.core.NodeJsCorePlugin;

/**
 * 
 * @author alpapad
 */
public class NodeJsManager implements IRegistryChangeListener {

    private static final String EXTENSION_NODEJS_INSTALLS = "nodeJs";

    private static final NodeJsManager INSTANCE = new NodeJsManager();

    private Set<NodeJs> nodeJSInstalls;

    private final DefNodeJs def = new DefNodeJs();

    private boolean registryListenerIntialized;

    public static NodeJsManager getManager() {
        return INSTANCE;
    }


    public NodeJsManager() {
        this.registryListenerIntialized = false;
    }

    @Override
    public void registryChanged(final IRegistryChangeEvent event) {
        IExtensionDelta[] deltas = event.getExtensionDeltas(NodeJsCorePlugin.PluginId, EXTENSION_NODEJS_INSTALLS);
        if (deltas != null) {
            for (IExtensionDelta delta : deltas) {
                handleNodejsInstallDelta(delta);
            }
        }
    }

    /**
     * FIXME: This should take an argument with the ID of the calling plugin. Currently we hard coded the TsCorePluginId!
     * @return
     */
    public NodeJs[] getNodejsInstalls() {
        if (nodeJSInstalls == null) {
            load();
        }

        NodeJs[] st = new NodeJs[nodeJSInstalls.size()];
        nodeJSInstalls.toArray(st);
        return st;
    }

    /**
     * FIXME: This should take an extra argument with the ID of the calling plugin. Currently we hard coded the TsCorePluginId!
     * @return
     */
    public NodeJs findNodejsInstall(String id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        if (nodeJSInstalls == null) {
            load();
        }
        Iterator<NodeJs> iterator = nodeJSInstalls.iterator();
        while (iterator.hasNext()) {
            NodeJs nodeJSInstall = iterator.next();
            if (id.equals(nodeJSInstall.getId())) {
                return nodeJSInstall;
            }
        }
        return null;
    }

    /**
     * FIXME: This should take an argument with the ID of the calling plugin. Currently we hard coded the TsCorePluginId!
     * @return
     */
    public NodeJs getConfiguration() {
        NodeJs[] instances = NodeJsManager.getManager().getNodejsInstalls();
        if(instances != null && instances.length > 0) {
            for(NodeJs n: instances) {
                if(n.getNodeJs().exists() && n.getNpm().exists()) {
                  return n;
                } else {
                   throw new RuntimeException("Seems like nodejs or npm executables do not exist for:" + n);
                }
            }
        }
        return def;
    }

    /**
     * Load the Nodejs installs.
     */
    private synchronized void load() {
        if (nodeJSInstalls != null) {
            return;
        }

        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] cf = registry.getConfigurationElementsFor(NodeJsCorePlugin.PluginId, EXTENSION_NODEJS_INSTALLS);
        Set<NodeJs> set = new LinkedHashSet<NodeJs>(cf.length);
        addNodejs(cf, set);
        addRegistryListenerIfNeeded();
        nodeJSInstalls = set;

    }

    /**
     * Load the Nodejs installs.
     */
    private synchronized void addNodejs(IConfigurationElement[] cf, Set<NodeJs> nodes) {
        for (IConfigurationElement ce : cf) {
            try {
                nodes.add(new NodeJs(ce));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void handleNodejsInstallDelta(IExtensionDelta delta) {
        if (nodeJSInstalls == null) {
            return;
        }

        IConfigurationElement[] cf = delta.getExtension().getConfigurationElements();

        Set<NodeJs> set = new LinkedHashSet<NodeJs>(nodeJSInstalls);
        if (delta.getKind() == IExtensionDelta.ADDED) {
            addNodejs(cf, set);
        } else {
            int size = set.size();
            NodeJs[] st = set.toArray(new NodeJs[size]);
            
            int size2 = cf.length;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size2; j++) {
                    if (st[i].getId().equals(cf[j].getAttribute("id"))) {
                        set.remove(st[i]);
                    }
                }
            }
        }
        nodeJSInstalls = set;
    }

    private void addRegistryListenerIfNeeded() {
        if (registryListenerIntialized) {
            return;
        }

        IExtensionRegistry registry = Platform.getExtensionRegistry();
        registry.addRegistryChangeListener(this, NodeJsCorePlugin.PluginId);
        registryListenerIntialized = true;
    }

    public void initialize() {

    }

    public void destroy() {
        Platform.getExtensionRegistry().removeRegistryChangeListener(this);
    }
}
