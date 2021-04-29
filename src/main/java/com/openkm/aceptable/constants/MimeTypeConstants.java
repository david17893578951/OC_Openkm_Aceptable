/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2015  Paco Avila & Josep Llort
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.openkm.aceptable.constants;

import java.util.ArrayList;

public class MimeTypeConstants {
	public static ArrayList<String> validOpenOffice = new ArrayList<>();
	public static ArrayList<String> validImageMagick = new ArrayList<>();

	static {
		// Basic
		validOpenOffice.add(MimeTypeConstants.MIME_TEXT);
		validOpenOffice.add(MimeTypeConstants.MIME_CSV);
		validOpenOffice.add(MimeTypeConstants.MIME_RTF);

		// OpenOffice.org OpenDocument
		validOpenOffice.add(MimeTypeConstants.MIME_OO_TEXT);
		validOpenOffice.add(MimeTypeConstants.MIME_OO_PRESENTATION);
		validOpenOffice.add(MimeTypeConstants.MIME_OO_SPREADSHEET);
		validOpenOffice.add(MimeTypeConstants.MIME_OO_GRAPHICS);
		validOpenOffice.add(MimeTypeConstants.MIME_OO_DATABASE);

		// Microsoft Office
		validOpenOffice.add(MimeTypeConstants.MIME_MS_WORD);
		validOpenOffice.add(MimeTypeConstants.MIME_MS_EXCEL);
		validOpenOffice.add(MimeTypeConstants.MIME_MS_POWERPOINT);

		// Microsoft Office 2007
		validOpenOffice.add(MimeTypeConstants.MIME_MS_WORD_2007);
		validOpenOffice.add(MimeTypeConstants.MIME_MS_WORD_2007_TEMPLATE);
		validOpenOffice.add(MimeTypeConstants.MIME_MS_EXCEL_2007);
		validOpenOffice.add(MimeTypeConstants.MIME_MS_EXCEL_2007_TEMPLATE);
		validOpenOffice.add(MimeTypeConstants.MIME_MS_POWERPOINT_2007);
		validOpenOffice.add(MimeTypeConstants.MIME_MS_POWERPOINT_2007_TEMPLATE);
		validOpenOffice.add(MimeTypeConstants.MIME_MS_POWERPOINT_2007_SLIDESHOW);

		// Images
		validImageMagick.add(MimeTypeConstants.MIME_JPEG);
		validImageMagick.add(MimeTypeConstants.MIME_PNG);
		validImageMagick.add(MimeTypeConstants.MIME_GIF);
		validImageMagick.add(MimeTypeConstants.MIME_TIFF);
		validImageMagick.add(MimeTypeConstants.MIME_BMP);
		validImageMagick.add(MimeTypeConstants.MIME_SVG);
		validImageMagick.add(MimeTypeConstants.MIME_PSD);
	}

	// MIME types => NOTE Keep on sync with default.sql
	public final static String MIME_UNDEFINED = "application/octet-stream";

	// Application
	public final static String MIME_RTF = "application/rtf";
	public final static String MIME_PDF = "application/pdf";
	public final static String MIME_ZIP = "application/zip";
	public final static String MIME_POSTSCRIPT = "application/postscript";
	public final static String MIME_MS_WORD = "application/msword";
	public final static String MIME_MS_EXCEL = "application/vnd.ms-excel";
	public final static String MIME_MS_POWERPOINT = "application/vnd.ms-powerpoint";
	public final static String MIME_MS_WORD_2007 = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public final static String MIME_MS_WORD_2007_TEMPLATE = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
	public final static String MIME_MS_EXCEL_2007 = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public final static String MIME_MS_EXCEL_2007_TEMPLATE = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
	public final static String MIME_MS_POWERPOINT_2007 = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	public final static String MIME_MS_POWERPOINT_2007_TEMPLATE = "application/vnd.openxmlformats-officedocument.presentationml.template";
	public final static String MIME_MS_POWERPOINT_2007_SLIDESHOW = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
	public final static String MIME_OO_TEXT = "application/vnd.oasis.opendocument.text";
	public final static String MIME_OO_SPREADSHEET = "application/vnd.oasis.opendocument.spreadsheet";
	public final static String MIME_OO_PRESENTATION = "application/vnd.oasis.opendocument.presentation";
	public final static String MIME_OO_GRAPHICS = "application/vnd.oasis.opendocument.graphics";
	public final static String MIME_OO_DATABASE = "application/vnd.oasis.opendocument.database";
	public final static String MIME_SWF = "application/x-shockwave-flash";
	public final static String MIME_JAR = "application/x-java-archive";
	public final static String MIME_EPUB = "application/epub+zip";

	// Image
	public final static String MIME_DXF = "image/vnd.dxf";
	public final static String MIME_DWG = "image/vnd.dwg";
	public final static String MIME_DWF = "image/vnd.dwf";
	public final static String MIME_TIFF = "image/tiff";
	public final static String MIME_JPEG = "image/jpeg";
	public final static String MIME_GIF = "image/gif";
	public final static String MIME_PNG = "image/png";
	public final static String MIME_BMP = "image/bmp";
	public final static String MIME_PSD = "image/x-psd";
	public final static String MIME_ICO = "image/x-ico";
	public final static String MIME_PBM = "image/pbm";
	public final static String MIME_SVG = "image/svg+xml";

	// Video
	public final static String MIME_MP3 = "audio/mpeg";
	public final static String MIME_MP4 = "video/mp4";
	public final static String MIME_MPEG = "video/mpeg";
	public final static String MIME_FLV = "video/x-flv";
	public final static String MIME_WMV = "video/x-ms-wmv";
	public final static String MIME_AVI = "video/x-msvideo";

	// Text
	public final static String MIME_HTML = "text/html";
	public final static String MIME_TEXT = "text/plain";
	public final static String MIME_XML = "text/xml";
	public final static String MIME_CSV = "text/csv";
	public final static String MIME_CSS = "text/css";

	// Language
	public final static String MIME_SQL = "text/x-sql";
	public final static String MIME_JAVA = "text/x-java";
	public final static String MIME_SCALA = "text/x-scala";
	public final static String MIME_PYTHON = "text/x-python";
	public final static String MIME_GROOVY = "text/x-groovy";
	public final static String MIME_DIFF = "text/x-diff";
	public final static String MIME_PASCAL = "text/x-pascal";
	public final static String MIME_CSHARP = "text/x-csharp";
	public final static String MIME_CPP = "text/x-c++";
	public final static String MIME_APPLESCRIPT = "text/applescript";
	public final static String MIME_SH = "application/x-shellscript";
	public final static String MIME_BSH = "application/x-bsh";
	public final static String MIME_PHP = "application/x-php";
	public final static String MIME_PERL = "application/x-perl";
	public final static String MIME_JAVASCRIPT = "application/javascript";
	public final static String MIME_AS3 = "application/x-font-truetype";

	// Mail
	public final static String MIME_OUTLOOK = "application/vnd.ms-outlook";
	public final static String MIME_EML = "message/rfc822";
}