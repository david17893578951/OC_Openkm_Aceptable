package com.openkm.aceptable.bean;

public class DocumentType {
	private String key;
	private String metadataGroup;
	private String name;

	public DocumentType(String key, String metadataGroup, String name) {
		this.key = key;
		this.metadataGroup = metadataGroup;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMetadataGroup() {
		return metadataGroup;
	}

	public void setMetadataGroup(String metadataGroup) {
		this.metadataGroup = metadataGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
