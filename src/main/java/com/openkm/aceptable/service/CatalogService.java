package com.openkm.aceptable.service;

import com.openkm.aceptable.bean.DocumentType;
import com.openkm.aceptable.bean.GroupFormElements;
import com.openkm.sdk4j.bean.Document;
import com.openkm.sdk4j.exception.*;
import org.apache.commons.lang.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface CatalogService {

	List<Document> getChildren() throws RepositoryException, AccessDeniedException, PathNotFoundException, DatabaseException,
			UnknowException, WebserviceException, IOException, ParseException, NoSuchPropertyException, NoSuchGroupException,
			LockException, ExtensionException, AutomationException, AuthenticationException;

	GroupFormElements getDocumentType(String uuid)
			throws AccessDeniedException, IOException, ParseException, NoSuchPropertyException, NoSuchGroupException,
			LockException, PathNotFoundException, RepositoryException, DatabaseException, ExtensionException,
			AutomationException, UnknowException, WebserviceException, PluginNotFoundException, ValidationFormException, AuthenticationException;

	void setPropertiesToNode(String uuid, String groupName, Map<String, String> properties)
			throws AccessDeniedException, IOException, ParseException, NoSuchPropertyException, NoSuchGroupException,
			LockException, PathNotFoundException, RepositoryException, DatabaseException, ExtensionException,
			AutomationException, UnknowException, WebserviceException, AuthenticationException, PluginNotFoundException, ValidationFormException;

	Document getDocumentById(String docId) throws RepositoryException, AccessDeniedException, PathNotFoundException,
			DatabaseException, UnknowException, WebserviceException, AuthenticationException;

	void assignType(String uuid, String type) throws AccessDeniedException, IOException, ParseException,
			NoSuchPropertyException, NoSuchGroupException, LockException, PathNotFoundException, RepositoryException,
			DatabaseException, ExtensionException, AutomationException, UnknowException, WebserviceException, PluginNotFoundException, ValidationFormException, AuthenticationException;

	List<DocumentType> getDocumentTypes() throws DatabaseException, UnknowException, WebserviceException, AuthenticationException;

	void removeType(String uuid) throws AccessDeniedException, NoSuchGroupException, LockException, PathNotFoundException,
			RepositoryException, DatabaseException, ExtensionException, AutomationException, UnknowException,
			WebserviceException, IOException, ParseException, NoSuchPropertyException, AuthenticationException;

	InputStream getContent(String node) throws RepositoryException, IOException, PathNotFoundException, AccessDeniedException,
			DatabaseException, UnknowException, WebserviceException, AuthenticationException;

	InputStream getContentInPdfFormat(String node)
			throws RepositoryException, PathNotFoundException, DatabaseException, UnknowException, WebserviceException,
			AccessDeniedException, IOException, NotImplementedException, ConversionException, AuthenticationException;

	Document getProperties(String node) throws RepositoryException, PathNotFoundException, DatabaseException, UnknowException,
			WebserviceException, AccessDeniedException, AuthenticationException;

}
