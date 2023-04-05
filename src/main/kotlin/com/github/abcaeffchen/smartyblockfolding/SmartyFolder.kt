package com.github.abcaeffchen.smartyblockfolding


import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.util.TextRange

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.xml.XmlTokenImpl
import com.intellij.psi.search.*;
import com.intellij.psi.util.*

import com.jetbrains.smarty.lang.SmartyElementType
import com.jetbrains.smarty.lang.psi.SmartyTag
import com.jetbrains.smarty.lang.SmartyTokenTypes

class SmartyFolder : FoldingBuilderEx() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean) = PsiTreeUtil
        .findChildrenOfType(root, SmartyTag::class.java)
        // find block commands
        .filter {
            it.childrenOfType<PsiElement>().filter { a -> a.elementType == SmartyTokenTypes.END_TAG_START }.size == 1
        }
        // make all remaining blocks to FoldingDescriptors
        .map {
            FoldingDescriptor(
                it.node,
                TextRange(
                    (it.childrenOfType<PsiElement>().find { b -> b.elementType == SmartyTokenTypes.TAG_END }?.textRange?.endOffset ?: 0) - 1,
                    it.textRange.endOffset - 1)
            )
        }
        .toTypedArray()

    /**
     * All placeholder texts are '/' to display '</>'
     */
    override fun getPlaceholderText(node: ASTNode) = "..."

    /**
     * All collapsed by default, of course
     */
    override fun isCollapsedByDefault(node: ASTNode) = false

}