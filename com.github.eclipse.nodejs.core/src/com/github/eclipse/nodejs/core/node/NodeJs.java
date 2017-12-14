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
import java.io.IOException;
import java.nio.file.Paths;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import com.github.eclipse.nodejs.core.NodeJsCorePlugin;

/**
 * 
 * @author alpapad
 */
public class NodeJs {

	private final String id;
	private final String name;
	private final File nodeJs;
	private final File npm;
	private final File tsLibFolder;
	private final File typingsLocation;
	private final Integer maxOldSpaceSize;

	protected NodeJs(String id, String name, File nodeJs, File npm, File tsLibFolder, Integer maxOldSpaceSize) {
		super();
		this.id = id;
		this.name = name;
		this.nodeJs = nodeJs;
		this.npm = npm;
		this.tsLibFolder = tsLibFolder;
		this.typingsLocation = new File(Ts.getGlobalTypingsCacheLocation("2.7"));
		this.maxOldSpaceSize = maxOldSpaceSize != null ? maxOldSpaceSize : Integer.valueOf(8192);
	}

	/**
	 * GeneratorType constructor comment.
	 *
	 * @param element
	 *            a configuration element
	 * @throws IOException
	 */
	public NodeJs(IConfigurationElement element) throws IOException {
		this.id = element.getAttribute("id");
		this.name = element.getAttribute("name");
		this.nodeJs = getExecutable(element, "nodeJs");
		this.npm = getExecutable(element, "npm");
		this.tsLibFolder = getRelativeToPlugin(NodeJsCorePlugin.TsCorePluginId, "node_modules/typescript/lib");
		this.typingsLocation = new File(Ts.getGlobalTypingsCacheLocation("2.7"));
		this.maxOldSpaceSize = getIntValue(element, "max-old-space-size", 8192);
	}

	private File getExecutable(IConfigurationElement element, String key) throws IOException {
		String pluginId = element.getNamespaceIdentifier();
		String lpath = element.getAttribute(key);
		if (lpath != null && lpath.length() > 0) {
			File file = getRelativeToPlugin(pluginId, lpath);
			if (file.exists()) {
				file.setExecutable(true);
				return file;
			}
		}
		return null;
	}

	private Integer getIntValue(IConfigurationElement element, String key, Integer def) {
		String attribute = element.getAttribute(key);
		if (attribute != null && attribute.length() > 0) {
			return Integer.decode(attribute);
		}
		return def;
	}

	protected static File getRelativeToPlugin(String pluginId, String relPath) throws IOException {
		File baseDir = FileLocator.getBundleFile(Platform.getBundle(pluginId));
		return Paths.get(baseDir.getAbsolutePath(), relPath).normalize().toFile();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public File getNodeJs() {
		return nodeJs;
	}

	public File getNpm() {
		return npm;
	}

	public Integer getMaxOldSpaceSize() {
		return maxOldSpaceSize;
	}

	/**
	 * TODO: Consider moving this method to TsCore... 
	 * @return
	 */
	public File getTsLibFolder() {
		try {
			return getRelativeToPlugin(NodeJsCorePlugin.TsCorePluginId, "node_modules/typescript/lib");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO: Consider moving this method to TsCore... 
	 * @return
	 */
	public File getTypingsLocation() {
		return typingsLocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeJs other = (NodeJs) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NodeJs [id=" + id + ", name=" + name + ", nodeJs=" + nodeJs + ", npm=" + npm + ", tsLibFolder="
				+ tsLibFolder + ", getId()=" + getId() + ", getName()=" + getName() + ", getNodeJs()=" + getNodeJs()
				+ ", getNpm()=" + getNpm() + ", getTsLibFolder()=" + getTsLibFolder() + "]";
	}

}
