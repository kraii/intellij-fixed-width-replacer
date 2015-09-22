package com.github.kraii.intellij.textreplacer;

import com.google.common.base.Strings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextReplaceAction extends EditorAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextReplaceAction.class);
    private static final int START_OF_STRING = 0;

    public TextReplaceAction() {
        super(new Handler());
    }

    private static class Handler extends EditorActionHandler {
        @Override
        protected void doExecute(Editor editor, Caret caret, DataContext dataContext) {
            super.doExecute(editor, caret, dataContext);
            SelectionModel selectionModel = editor.getSelectionModel();
            String selectedText = selectionModel.getSelectedText();

            if (selectedText != null && !selectedText.isEmpty()) {
                LOGGER.debug("Woop gonna do it");
                replaceSelectedTextWithSpaces(editor.getDocument(), selectionModel, selectedText);
            } else {
                LOGGER.debug("No selected text - ignoring");
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


}
