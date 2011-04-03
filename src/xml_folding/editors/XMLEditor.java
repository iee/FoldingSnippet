/*******************************************************************************
 * Copyright (c) 2005 Prashant Deva.
 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License - v 1.0
 * which is available at http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/

package xml_folding.editors;


import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

public class XMLEditor extends TextEditor {

    private ProjectionSupport projectionSupport;
    
	private ColorManager colorManager;

	public XMLEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new XMLConfiguration(colorManager,this));
		setDocumentProvider(new XMLDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     * Override createPartControl method of TextEditor to configure and install ProjectionSupport
     */
    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);
        ProjectionViewer viewer =(ProjectionViewer)getSourceViewer();
        
        projectionSupport = new ProjectionSupport(viewer,getAnnotationAccess(),getSharedColors());
		projectionSupport.install();
		
		//turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);
		
		annotationModel = viewer.getProjectionAnnotationModel();
		
    }
	private Annotation[] oldAnnotations;
	private ProjectionAnnotationModel annotationModel;
	
	/*
	 * The parser runs as a reconciling strategy and parses the entire document every time it is modified. 
	 * The parser then passes the range of every XML element to the editor, which then in turn adds 
	 * Projection Annotations to define collapsible regions.
	 */
	public void updateFoldingStructure(ArrayList positions)
	{
		Annotation[] annotations = new Annotation[positions.size()];
		
		//this will hold the new annotations along
		//with their corresponding positions
		HashMap newAnnotations = new HashMap();
		
		for(int i =0;i<positions.size();i++)
		{
			XMLProjectionAnnotation annotation = new XMLProjectionAnnotation();
			annotation.setRangeIndication(false);
			newAnnotations.put(annotation,positions.get(i));
			
			annotations[i]=annotation;
		}
		
		annotationModel.modifyAnnotations(oldAnnotations,newAnnotations,null);
		/*for (int i = 0; i < 10; i++) {
			((ProjectionViewer)viewer).doOperation(ProjectionViewer.COLLAPSE_ALL);
			((ProjectionViewer)viewer).doOperation(ProjectionViewer.EXPAND_ALL);
		}*/
		annotationModel.collapse(annotations[1]);
		((XMLProjectionAnnotation)annotations[1]).markCollapsedReal();
		annotationModel.expand(annotations[1]);
		oldAnnotations=annotations;
	}
	private ISourceViewer viewer;
    /* (non-Javadoc)
     * @see org.eclipse.ui.texteditor.AbstractTextEditor#createSourceViewer(org.eclipse.swt.widgets.Composite, org.eclipse.jface.text.source.IVerticalRuler, int)
     * Override createSourceViewer method of AbstractTextEditor to return a ProjectionViewer
     */
    protected ISourceViewer createSourceViewer(Composite parent,
            IVerticalRuler ruler, int styles)
    {
        viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
        
    	// ensure decoration support has been created and configured.
    	getSourceViewerDecorationSupport(viewer);
    	return viewer;
    }
}
