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

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.openkm.sdk4j.bean.Document;
import com.openkm.sdk4j.exception.AccessDeniedException;
import com.openkm.sdk4j.exception.AuthenticationException;
import com.openkm.sdk4j.exception.ConversionException;
import com.openkm.sdk4j.exception.DatabaseException;
import com.openkm.sdk4j.exception.PathNotFoundException;
import com.openkm.sdk4j.exception.RepositoryException;
import com.openkm.sdk4j.exception.UnknowException;
import com.openkm.sdk4j.exception.WebserviceException;
import com.openkm.aceptable.service.CatalogService;
import com.openkm.aceptable.util.FileUtils;
import com.openkm.aceptable.util.PathUtils;
import com.openkm.aceptable.util.WebUtils;

//@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@Controller
@RequestMapping("/Download")
public class DownloadController {
	private static Logger log = LoggerFactory.getLogger(DownloadController.class);

	@Autowired
	private CatalogService catalogService;

	/**
	 * download
	 * @throws AuthenticationException 
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException, RepositoryException,
			PathNotFoundException, AccessDeniedException, DatabaseException, UnknowException, WebserviceException, AuthenticationException {
		log.debug("download({}, {})", request, response);
		String node = request.getParameter("node");
		boolean inline = true;

		if (request.getParameter("attachment") != null && !request.getParameter("attachment").isEmpty()) {
			inline = false;
		}

		InputStream is = catalogService.getContent(node);
		Document doc = catalogService.getProperties(node);
		WebUtils.prepareSendFile(request, response, PathUtils.getName(doc.getPath()), doc.getMimeType(), inline);

		// Set length
		//response.setContentLength(is.available()); // Cause a bug, because at this point InputStream still has not its real size.
		response.setContentLength(new Long(doc.getActualVersion().getSize()).intValue());

		ServletOutputStream sos = response.getOutputStream();
		IOUtils.copy(is, sos);
		sos.flush();
		sos.close();
	}

	/**
	 * downloadForPreview
	 * @throws AuthenticationException 
	 */
	@RequestMapping(value = "/downloadForPreview", method = RequestMethod.GET)
	public void downloadForPreview(HttpServletRequest request, HttpServletResponse response) throws IOException, RepositoryException,
			PathNotFoundException, AccessDeniedException, DatabaseException, UnknowException, WebserviceException, NotImplementedException, ConversionException, AuthenticationException {
		log.debug("downloadForPreview({}, {})", request, response);
		String node = request.getParameter("node");
		String jSessionId = WebUtils.getJSessionIdFromUrl(request.getRequestURI());

		if (jSessionId != null && jSessionId.equalsIgnoreCase(request.getSession().getId())) {
			boolean inline = true;
			if (request.getParameter("attachment") != null && !request.getParameter("attachment").isEmpty()) {
				inline = false;
			}
			boolean conversion = false;
			if (request.getParameter("conversion") != null && !request.getParameter("conversion").isEmpty()) {
				conversion = true;
			}

			Document doc = catalogService.getProperties(node);
			String fileName = PathUtils.getName(doc.getPath());

			InputStream is;
			if (conversion) {
				is = catalogService.getContentInPdfFormat(node);
				fileName = FileUtils.getFileName(fileName) + ".pdf";
			} else {
				is = catalogService.getContent(node);
			}

			WebUtils.prepareSendFile(request, response, fileName, doc.getMimeType(), inline);

			// Set length
			// response.setContentLength(is.available()); // Cause a bug, because at this point InputStream still has
			// not its real size.
			if (!conversion) {
				response.setContentLength(new Long(doc.getActualVersion().getSize()).intValue());
			}

			ServletOutputStream sos = response.getOutputStream();
			IOUtils.copy(is, sos);
			sos.flush();
			sos.close();
		}
	}
}