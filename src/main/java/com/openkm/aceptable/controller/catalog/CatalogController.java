/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2015 Paco Avila & Josep Llort
 * <p>
 * No bytes were intentionally harmed during the development of this application.
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.openkm.aceptable.controller.catalog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.openkm.aceptable.bean.GroupFormElements;
import com.openkm.aceptable.config.Config;
import com.openkm.aceptable.config.auth.CustomUser;
import com.openkm.aceptable.constants.MimeTypeConstants;
import com.openkm.sdk4j.bean.Document;
import com.openkm.sdk4j.exception.*;
import com.openkm.sdk4j.util.ISO8601;
import com.openkm.aceptable.service.CatalogService;
import com.openkm.aceptable.util.PrincipalUtils;

/**
 * Catalog controller.
 */
@Controller
@RequestMapping("/catalog")
public class CatalogController {

	private static Logger logger = LoggerFactory.getLogger(CatalogController.class);

	private static final String URL_NEXT = "/next";
	private static final String URL_CATALOG = "/catalog";
	private static final String URL_TYPE_SELECT = "/typeSelect/{docId}";
	private static final String URL_ASSIGN_TYPE = "/assignType";
	private static final String URL_REMOVE_TYPE = "/removeType";
	private static final String MARKETING_AGENCY_PROPERTY = "okp:marketing.agency";

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private Config configService;

	/**
	 * Date formatter.
	 */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

	/**
	 * Sort documents by creation date ascending
	 */
	private Comparator<Document> documentsComparator = new Comparator<Document>() {

		@Override
		public int compare(Document o1, Document o2) {
			return o1.getCreated().compareTo(o2.getCreated());
		}
	};

	/**
	 * next
	 */
	@RequestMapping(value = URL_NEXT)
	public ModelAndView next(Authentication auth, HttpServletRequest request, String elementId) {
		logger.debug("next ()");
		ModelAndView view;
		try {
			view = new ModelAndView("catalog/next");
			List<Document> documents = catalogService.getChildren();
			view.addObject("total", documents.size());
			if (!documents.isEmpty()) {
				Collections.sort(documents, documentsComparator);
				Document nextDocument = getNextDocument(documents, request, elementId);
				GroupFormElements elements = catalogService.getDocumentType(nextDocument.getUuid());
				if (elements == null || elements.getDocumentType() == null || "".equals(elements.getDocumentType())) {
					return new ModelAndView("redirect:/catalog/typeSelect/" + nextDocument.getUuid());
				}
				view.addObject("document", nextDocument);
				view.addObject("documents", documents);
				view.addObject("elements", elements.getElements());
				view.addObject("type", elements.getDocumentType());
				view.addObject("appUrl", configService.OPENKM_URL);
				view.addObject("previewUrl", generatePreviewUrl(nextDocument, request));
				fillModel(view);
			}
			return view;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			view = new ModelAndView("error");
			view.addObject("message", e.getMessage());
			view.addObject("exception", e);
			return view;
		}
	}

	private Document getNextDocument(List<Document> documents, HttpServletRequest request, String elementId) {
		String next = request.getParameter("element");
		Document doc = null;
		if ((next != null && !"".equals(next)) || (elementId != null && !"".equals(elementId))) {
			for (Document d : documents) {
				if ((next != null && next.equals(d.getUuid())) || (elementId != null && elementId.equals(d.getUuid()))) {
					doc = d;
					break;
				}
			}
		} else {
			doc = documents.iterator().next();
		}
		return doc;
	}

	@RequestMapping(value = URL_CATALOG, method = RequestMethod.POST)
	public String catalog(Authentication auth, HttpServletRequest request) {
		logger.debug("catalog ()");
		try {
			Enumeration<String> parameters = request.getParameterNames();
			Map<String, String> properties = new HashMap<>();
			String parameter = null;
			String uuid = null;
			String groupName = null;
			while (parameters.hasMoreElements()) {
				parameter = parameters.nextElement();
				String parameterValue = request.getParameter(parameter);
				if ("document_type".equals(parameter)) {
					groupName = parameterValue;
				} else if ("document_uuid".equals(parameter)) {
					uuid = parameterValue;
				} else {
					if (isDate(parameterValue)) {
						Date date = sdf.parse(parameterValue);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						parameterValue = ISO8601.formatBasic(calendar);
					} else if (MARKETING_AGENCY_PROPERTY.equals(parameter)) {
						// Must convert value to array and then to json format
						List<String> list = Arrays.asList(parameterValue);
						parameterValue = new Gson().toJson(list);

					}
					properties.put(parameter, parameterValue);
				}
			}
			catalogService.setPropertiesToNode(uuid, groupName, properties);
			return "redirect:/catalog/next";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "error";
		}
	}

	private boolean isDate(String parameter) {
		try {
			sdf.parse(parameter);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	@RequestMapping(value = URL_TYPE_SELECT, method = RequestMethod.GET)
	public ModelAndView typeSelect(@PathVariable String docId, Authentication auth, HttpServletRequest request) {
		ModelAndView view;
		try {
			view = new ModelAndView("catalog/typeSelect");
			List<Document> documents = catalogService.getChildren();
			view.addObject("total", documents.size());
			view.addObject("documents", documents);
			Document doc = catalogService.getDocumentById(docId);
			view.addObject("doc", doc);
			view.addObject("appUrl", configService.OPENKM_URL);
			String previewUrl = generatePreviewUrl(doc, request);
			view.addObject("types", catalogService.getDocumentTypes());
			view.addObject("previewUrl", previewUrl);
			fillModel(view);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			view = new ModelAndView("error");
			view.addObject("message", e.getMessage());
			view.addObject("exception", e);
		}
		return view;
	}

	@RequestMapping(value = URL_ASSIGN_TYPE, method = RequestMethod.POST)
	public ModelAndView assignType(Authentication auth, HttpServletRequest request) {
		logger.debug("assignType ()");
		ModelAndView view;
		try {
			String type = request.getParameter("type_select");
			String uuid = request.getParameter("document_uuid");
			catalogService.assignType(uuid, type);
			return next(auth, request, uuid);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			view = new ModelAndView("error");
			view.addObject("message", e.getMessage());
			view.addObject("exception", e);
			return view;
		}
	}

	@RequestMapping(value = URL_REMOVE_TYPE, method = RequestMethod.POST)
	public ModelAndView removeType(Authentication auth, HttpServletRequest request) {
		logger.debug("removeType ()");
		ModelAndView view;
		try {
			String uuid = request.getParameter("document_uuid");
			catalogService.removeType(uuid);
			return next(auth, request, uuid);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			view = new ModelAndView("error");
			view.addObject("message", e.getMessage());
			view.addObject("exception", e);
			return view;
		}
	}

	private String generatePreviewUrl(Document doc, HttpServletRequest request) throws RepositoryException,
			AccessDeniedException, PathNotFoundException, DatabaseException, UnknowException, WebserviceException {
		StringBuffer previewUrl = new StringBuffer();
		previewUrl.append(configService.getPreviewKcenterToOpenKMUrl());
		previewUrl.append("?");

		boolean convertToPdf = false;
		if (MimeTypeConstants.validImageMagick.contains(doc.getMimeType())
				|| MimeTypeConstants.validOpenOffice.contains(doc.getMimeType())) {
			convertToPdf = true;
		}

		if (convertToPdf) {
			previewUrl.append("pdfUrl=");
		} else {
			previewUrl.append("mimeType=").append(doc.getMimeType());
			previewUrl.append("&docUrl=");
		}
		previewUrl.append(configService.PREVIEW_DOWNLOAD_URL);
		previewUrl.append("/downloadForPreview");
		previewUrl.append(";jsessionid=");
		previewUrl.append(request.getSession().getId());
		previewUrl.append("?node=");
		previewUrl.append(doc.getUuid());
		previewUrl.append("%26attachment=true");
		if (convertToPdf) {
			previewUrl.append("%26conversion=true");
		}
		return previewUrl.toString();
	}
	
	private void fillModel(ModelAndView model) {
		CustomUser user = PrincipalUtils.getUser();
		model.addObject("user", user);
	}

}