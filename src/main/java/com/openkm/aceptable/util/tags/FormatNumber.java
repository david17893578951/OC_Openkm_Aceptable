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
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class FormatNumber extends TagSupport {
	private BigDecimal value;
	private String type;

	@Override
	public int doStartTag() {
		try {
			if (value != null) {
				String str;

				if ("currency".equals(type)) {
					str = FormatUtils.formatCurrency(value);
				} else {
					str = FormatUtils.formatNumber(value);
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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
