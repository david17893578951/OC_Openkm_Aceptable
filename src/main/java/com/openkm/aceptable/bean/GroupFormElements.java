package com.openkm.aceptable.bean;

import com.openkm.sdk4j.bean.form.FormElement;

import java.util.List;

public class GroupFormElements {

	private List<FormElement> elements;

	private DocumentType documentType;

	public GroupFormElements(DocumentType documentType, List<FormElement> elements) {
		this.elements = elements;
		this.documentType = documentType;
	}

	public List<FormElement> getElements() {
		return elements;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

}
