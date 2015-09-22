package com.github.kraii.intellij.textreplacer;

import com.google.common.base.Strings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.jetbrains.annotations.NotNull;

public class TextReplaceAction extends AnAction {

    private static final int START_OF_STRING = 0;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Editor editor = DataKeys.EDITOR.getData(anActionEvent.getDataContext());

        if (editor != null) {
            SelectionModel selectionModel = editor.getSelectionModel();
            String selectedText = selectionModel.getSelectedText();

            if (selectedText != null && !selectedText.isEmpty()) {
                replaceSelectedTextWithSpaces(editor.getDocument(), selectionModel, selectedText);
            }
        }
    }

    private void replaceSelectedTextWithSpaces(Document document, SelectionModel selectionModel, String selectedText) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            String documentText = document.getText();
            String textBeforeSelection = documentText.substring(START_OF_STRING, selectionModel.getSelectionStart());
            String textAfterSelection = documentText.substring(selectionModel.getSelectionEnd());

            document.setText(textBeforeSelection + spacesWithTheSameLengthAs(selectedText) + textAfterSelection);
        });
    }

    @NotNull
    private String spacesWithTheSameLengthAs(String selectedText) {
        return Strings.padEnd(" ", selectedText.length(), ' ');
    }
}
