package com.github.abcaeffchen.smartyblockfolding

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.elementType
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.smarty.SmartyFile
import com.jetbrains.smarty.SmartyFileType
import com.jetbrains.smarty.lang.SmartyTokenTypes
import com.jetbrains.smarty.lang.psi.SmartyTag

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class MyPluginTest : BasePlatformTestCase() {

    fun testSmartyFile() {
        val psiFile = myFixture.configureByText(SmartyFileType.INSTANCE, "{foo}bar{baz}bla1{bla2 name='woot'}{/baz}{/foo}{test}{best}{/best}")
        assertInstanceOf(psiFile, SmartyFile::class.java)

        val elements = PsiTreeUtil.findChildrenOfType(psiFile, SmartyTag::class.java)
            // find block commands
            .filter {
                it.childrenOfType<PsiElement>().filter { a -> a.elementType == SmartyTokenTypes.END_TAG_START }.size == 1
            }
            .map {
                val children = it.childrenOfType<PsiElement>()
                val openingEnd = children.find { b -> b.elementType == SmartyTokenTypes.TAG_END }?.textRange?.endOffset ?: 0
                val closingBegin = children.find { b -> b.elementType == SmartyTokenTypes.END_TAG_START }?.textRange?.endOffset ?: 0
                print(openingEnd)
                print(", $closingBegin")
                println(", ${it.textRange.endOffset}")
                FoldingDescriptor(
                    it.node,
                    TextRange(openingEnd - 1, it.textRange.endOffset - 1)
                )
            }
    }

    override fun getTestDataPath() = "src/test/testData"
}
