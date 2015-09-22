package com.github.kraii.intellij.textreplacer;


import com.google.common.io.Resources;
import com.intellij.openapi.editor.Document;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

public class TextReplaceActionTest extends LightCodeInsightFixtureTestCase {

    public static final String FIVE_SPACES = "     ";

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "src/test/resources/test_data";
    }

    public void testNothingHappensIfNoTextIsSelected() throws IOException {
        myFixture.configureByFiles("sometext.txt");
        myFixture.performEditorAction("ReplaceTextWithSpaces");

        assertThat(myFixture.getEditor().getDocument().getText())
                .isEqualTo(originalFileContents());
    }

    public void testReplaceSelectedTextWithSpaces() throws IOException {
        myFixture.configureByFiles("sometext.txt");

        Document document = myFixture.getEditor().getDocument();
        selectText(1, 5);

        myFixture.performEditorAction("ReplaceTextWithSpaces");

        assertThat(document.getText())
                .isNotEqualTo(originalFileContents())
                .contains(FIVE_SPACES);
    }

    @NotNull
    private Document selectText(int selectionStart, int selectionEnd) {
        Document document = myFixture.getEditor().getDocument();
        myFixture.getEditor().getSelectionModel().setSelection(selectionStart, selectionEnd);
        return document;
    }

    private String originalFileContents() throws IOException {
        return Resources.toString(getClass().getResource("/test_data/sometext.txt"), Charset.defaultCharset());
    }
}