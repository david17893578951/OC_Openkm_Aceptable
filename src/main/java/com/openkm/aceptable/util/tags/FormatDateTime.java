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

import com.openkm.aceptable.util.FormatUtils;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by pavila on 31/03/16.
 */
public class FormatDateTime extends TagSupport {
	private Calendar value;

	@Override
	public int doStartTag() {
		try {
			if (value != null) {
				String str = FormatUtils.formatDateTime(value);
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

	public Calendar getValue() {
		return value;
	}

	public void setValue(Calendar value) {
		this.value = value;
	}
}