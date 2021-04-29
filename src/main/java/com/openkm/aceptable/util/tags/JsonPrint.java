/**
 * OpenKM, Open Document Management System (http://www.openkm.com)
 * Copyright (c) 2006-2016  Paco Avila & Josep Llort
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

package com.openkm.aceptable.util.tags;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Created by pavila on 1/03/16.
 */
public class JsonPrint extends TagSupport {
	private String value;
	private String property;

	@Override
	public int doStartTag() {
		try {
			if (value != null) {
				JsonFactory jfactory = new JsonFactory();
				String str = "";

				for (JsonParser jParser = jfactory.createParser(value); jParser.nextToken() != JsonToken.END_OBJECT; ) {
					if (property.equals(jParser.getCurrentName())) {
						jParser.nextToken();
						str = jParser.getText();
						break;
					}
				}

				pageContext.getOut().write(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Tag.SKIP_BODY;
	}

	@Override
	public void release() {
		super.release();
		value = null;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
