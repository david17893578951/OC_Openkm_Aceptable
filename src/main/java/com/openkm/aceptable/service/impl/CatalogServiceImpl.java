package com.openkm.aceptable.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.openkm.aceptable.bean.DocumentType;
import com.openkm.aceptable.bean.GroupFormElements;
import com.openkm.aceptable.cache.WSCacheDAO;
import com.openkm.aceptable.constants.MimeTypeConstants;
import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.bean.Configuration;
import com.openkm.sdk4j.bean.Document;
import com.openkm.sdk4j.bean.form.FormElement;
import com.openkm.sdk4j.exception.*;
import com.openkm.aceptable.service.CatalogService;
import com.openkm.aceptable.util.PathUtils;

@Service("catalogService")
public class CatalogServiceImpl implements CatalogService {

	private static final Logger log = LoggerFactory.getLogger(CatalogServiceImpl.class);

	private static final String TYPE_METADATA_PROPERTY = "okp:type.type";

	private static final String TYPE_METADATA_GROUP = "okg:type";

	private static final String NEW_LINE_SEPARATOR_1 = "\\r\\n";

	private static final String NEW_LINE_SEPARATOR_2 = "\\n";

	private static final String CP_DOCUMENT_TYPE = "cp.document.type";

	private static final String CP_PENDING_FOLDER = "cp.pending.folder";

	@Autowired
	private WSCacheDAO wsDao;

	@Override
	public List<Document> getChildren() throws RepositoryException, AccessDeniedException, PathNotFoundException,
			DatabaseException, UnknowException, WebserviceException, IOException, ParseException, NoSuchPropertyException,
			NoSuchGroupException, LockException, ExtensionException, AutomationException, AuthenticationException {
		log.debug("getChildren()");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());
		Configuration pendingPath = getProperty(CP_PENDING_FOLDER);

		String folderUuid = ws.getNodeUuid(pendingPath.getValue());
		List<Document> documents = ws.getDocumentChildren(folderUuid);

		List<Document> documentsWithType = new ArrayList<>();
		for (Document doc : documents) {
			//if (ws.hasGroup(doc.getUuid(), TYPE_METADATA_GROUP)) {
				documentsWithType.add(doc);
			//}
		}
		return documentsWithType;
	}

	@Override
	public GroupFormElements getDocumentType(String uuid)
			throws AccessDeniedException, IOException, ParseException, NoSuchPropertyException, NoSuchGroupException,
			LockException, PathNotFoundException, RepositoryException, DatabaseException, ExtensionException,
			AutomationException, UnknowException, WebserviceException, PluginNotFoundException, ValidationFormException, AuthenticationException {
		log.debug("getDocumentType({})", uuid);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());
		List<FormElement> elements = null;
		DocumentType selectedDocumentType = null;

		try {
			Map<String, String> map = ws.getPropertyGroupProperties(uuid, TYPE_METADATA_GROUP);
			String type = map.get(TYPE_METADATA_PROPERTY);

			if (type != null && !"".equals(type)) {
				List<String> values = new Gson().fromJson(type, List.class); // Type comes as a List serialized by gson as a String like ["1"]
				type = values.get(0); // Capture the first value
				List<DocumentType> types = getDocumentTypes();
				for (DocumentType documentType : types) {
					if (type.equals(documentType.getKey())) {
						selectedDocumentType = documentType;
						break;
					}
				}
				if (selectedDocumentType != null) {
					elements = ws.getPropertyGroupForm(selectedDocumentType.getMetadataGroup());
				}
			}
		} catch (IOException | ParseException | NoSuchPropertyException | NoSuchGroupException | LockException
				| PathNotFoundException | AccessDeniedException | RepositoryException | DatabaseException | ExtensionException
				| AutomationException | UnknowException | WebserviceException e) {
			// This is not an error. The document has no type assigned yet
			log.debug("There is no type assigned yet", e);
		}

		return new GroupFormElements(selectedDocumentType, elements);
	}

	@Override
	public List<DocumentType> getDocumentTypes() throws DatabaseException, UnknowException, WebserviceException, AuthenticationException {
		log.debug("getDocumentTypes()");
		List<DocumentType> types = new ArrayList<>();
		Configuration conf = getProperty(CP_DOCUMENT_TYPE);

		String[] values = null;
		if (conf.getValue() != null && conf.getValue().contains(NEW_LINE_SEPARATOR_1)) {
			values = conf.getValue().split(NEW_LINE_SEPARATOR_1);
		} else {
			values = conf.getValue().split(NEW_LINE_SEPARATOR_2);
		}

		for (String row : values) {
			String[] parts = row.split(";");
			DocumentType type = new DocumentType(parts[0], parts[1], parts[2]);
			types.add(type);
		}

		Collections.sort(types, new Comparator<DocumentType>() {
			@Override
			public int compare(DocumentType o1, DocumentType o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		return types;
	}

	@Override
	public void setPropertiesToNode(String uuid, String groupName, Map<String, String> properties)
			throws AccessDeniedException, IOException, ParseException, NoSuchPropertyException, NoSuchGroupException,
			LockException, PathNotFoundException, RepositoryException, DatabaseException, ExtensionException,
			AutomationException, UnknowException, WebserviceException, AuthenticationException, PluginNotFoundException, ValidationFormException {
		log.debug("setPropertiesToNode({}, {}, {})", uuid, groupName, properties);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());

		// Only add group if it has not
		if (!ws.hasGroup(uuid, groupName)) {
			ws.addGroup(uuid, groupName, properties);
		} else {
			ws.setPropertyGroupProperties(uuid, groupName, properties);
		}
	}

	@Override
	public Document getDocumentById(String docId) throws RepositoryException, AccessDeniedException, PathNotFoundException,
			DatabaseException, UnknowException, WebserviceException, AuthenticationException {
		log.debug("getDocumentById({})", docId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());
		return ws.getDocumentProperties(docId);
	}

	@Override
	public void assignType(String uuid, String type) throws AccessDeniedException, IOException, ParseException,
			NoSuchPropertyException, NoSuchGroupException, LockException, PathNotFoundException, RepositoryException,
			DatabaseException, ExtensionException, AutomationException, UnknowException, WebserviceException, PluginNotFoundException, ValidationFormException, AuthenticationException {
		log.debug("assignType({}, {})", uuid, type);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());

		Map<String, String> properties = new HashMap<>();
		List<String> list = Arrays.asList(type); // Convert value to List
		properties.put(TYPE_METADATA_PROPERTY, new Gson().toJson(list)); // Values must be sent in json array format
		// Only add group if it has not
		if (!ws.hasGroup(uuid, TYPE_METADATA_GROUP)) {
			ws.addGroup(uuid, TYPE_METADATA_GROUP, properties);
		} else {
			ws.setPropertyGroupProperties(uuid, TYPE_METADATA_GROUP, properties);
		}
	}

	private Configuration getProperty(String property) throws DatabaseException, UnknowException, WebserviceException, AuthenticationException {
		log.debug("getProperty({})", property);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());
		OKMWebservices ws = wsDao.getOKMWebservices(WSCacheDAO.ADMIN_USER);
		return ws.getConfiguration(property);
	}

	@Override
	public void removeType(String uuid) throws AccessDeniedException, NoSuchGroupException, LockException,
			PathNotFoundException, RepositoryException, DatabaseException, ExtensionException, AutomationException,
			UnknowException, WebserviceException, IOException, ParseException, NoSuchPropertyException, AuthenticationException {
		log.debug("removeType({})", uuid);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());
		ws.removeGroup(uuid, TYPE_METADATA_GROUP);

		Map<String, String> properties = new HashMap<>();
		properties.put(TYPE_METADATA_PROPERTY, ""); // must add with empty value
		ws.addGroup(uuid, TYPE_METADATA_GROUP, properties);
	}

	@Override
	public InputStream getContent(String node) throws RepositoryException, IOException, PathNotFoundException,
			AccessDeniedException, DatabaseException, UnknowException, WebserviceException, AuthenticationException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());
		return ws.getContent(node);
	}

	@Override
	public InputStream getContentInPdfFormat(String node) throws RepositoryException, PathNotFoundException, DatabaseException,
			UnknowException, WebserviceException, AccessDeniedException, IOException, NotImplementedException,
			ConversionException, AuthenticationException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());
		Document doc = getProperties(node);

		if (MimeTypeConstants.validImageMagick.contains(doc.getMimeType())) {
			return ws.imageConvert(getContent(node), PathUtils.getName(doc.getPath()), "", MimeTypeConstants.MIME_PDF);
		} else if (MimeTypeConstants.validOpenOffice.contains(doc.getMimeType())) {
			return ws.doc2pdf(getContent(node), PathUtils.getName(doc.getPath()));
		}

		return ws.getContent(node);
	}

	@Override
	public Document getProperties(String node) throws RepositoryException, PathNotFoundException, DatabaseException,
			UnknowException, WebserviceException, AccessDeniedException, AuthenticationException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OKMWebservices ws = wsDao.getOKMWebservices(auth.getName());
		return ws.getDocumentProperties(node);
	}
}