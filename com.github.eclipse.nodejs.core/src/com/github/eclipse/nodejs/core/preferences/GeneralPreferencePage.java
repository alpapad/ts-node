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

package com.github.eclipse.nodejs.core.preferences;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.eclipse.nodejs.core.NodeJsCorePlugin;


/**
 * 
 * @author alpapad
 *
 */
public final class GeneralPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
    private Text nodePathText;
    private Text npmPathText;

    public GeneralPreferencePage() {
        this.setPreferenceStore(NodeJsCorePlugin.getDefault().getPreferenceStore());
    }

    @Override
    public void init(IWorkbench workbench) {
    }

    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(3, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setFont(parent.getFont());

        Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        label.setText("Node executable path");

        this.nodePathText = new Text(composite, SWT.BORDER);
        this.nodePathText.setFont(composite.getFont());
        this.nodePathText.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, false));
        this.nodePathText.setText(this.getPreferenceStore().getString(IPreferenceConstants.GENERAL_NODE_PATH));

        Button button = new Button(composite, SWT.NONE);
        button.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        button.setText("Browse...");
        button.addListener(SWT.Selection, new MyListener(this.nodePathText));

        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        label.setText("NPM executable Path");
        this.npmPathText = new Text(composite, SWT.BORDER);
        this.npmPathText.setFont(composite.getFont());
        this.npmPathText.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, false));
        this.npmPathText.setText(this.getPreferenceStore().getString(IPreferenceConstants.GENERAL_NPM_PATH));

        button = new Button(composite, SWT.NONE);
        button.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        button.setText("Browse...");
        button.addListener(SWT.Selection, new MyListener(this.npmPathText));

        return composite;
    }

    @Override
    public boolean performOk() {
        String oldNodePath = this.getPreferenceStore().getString(IPreferenceConstants.GENERAL_NODE_PATH);

        String newNodePath = this.nodePathText.getText();

        if (!oldNodePath.equals(newNodePath)) {
            String title = "Node path:";
            String message = "The Node.js path has changed. Eclipse must be rebooted for this change to take effect.";
            String[] buttonLabels = new String[] { IDialogConstants.OK_LABEL };
            MessageDialog dialog = new MessageDialog(this.getShell(), title, null, message, MessageDialog.QUESTION, buttonLabels, 2);
            dialog.open();

            this.getPreferenceStore().setValue(IPreferenceConstants.GENERAL_NODE_PATH, newNodePath);
        }

        this.getPreferenceStore().setValue(IPreferenceConstants.GENERAL_NPM_PATH, npmPathText.getText());

        return true;
    }

    @Override
    protected IPreferenceStore doGetPreferenceStore() {
        return NodeJsCorePlugin.getDefault().getPreferenceStore();
    }

    private final class MyListener implements Listener {

        private final Text field;

        MyListener(Text folderField) {
            this.field = folderField;
        }

        @Override
        public void handleEvent(Event event) {
            FileDialog dialog = new FileDialog(getShell());
            dialog.setFileName(this.field.getText());

            // open the dialog and process the result
            String nodePath = dialog.open();
            if (nodePath != null) {
                this.field.setText(nodePath);
            }
        }
    }
}
