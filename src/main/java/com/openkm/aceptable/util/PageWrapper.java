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

package com.openkm.aceptable.util;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavila on 20/10/15.
 */
public class PageWrapper<T> {
	public static final int MAX_PAGE_ITEM_DISPLAY = 5;
	public static final int START_NUMBER = 1;
	public static final int PAGE_SIZE = 12;
	private Page<T> page;
	private List<PageItem> items;
	private int currentNumber;

	public PageWrapper(Page<T> page) {
		this.page = page;
		this.items = new ArrayList<>();
		this.currentNumber = page.getNumber() + START_NUMBER;

		int start, size;
		if (page.getTotalPages() <= MAX_PAGE_ITEM_DISPLAY) {
			start = START_NUMBER;
			size = page.getTotalPages();
		} else {
			if (currentNumber <= MAX_PAGE_ITEM_DISPLAY - MAX_PAGE_ITEM_DISPLAY / 2) {
				start = START_NUMBER;
				size = MAX_PAGE_ITEM_DISPLAY;
			} else if (currentNumber >= page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY / 2) {
				start = page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY + START_NUMBER;
				size = MAX_PAGE_ITEM_DISPLAY;
			} else {
				start = currentNumber - MAX_PAGE_ITEM_DISPLAY / 2;
				size = MAX_PAGE_ITEM_DISPLAY;
			}
		}

		for (int i = 0; i < size; i++) {
			items.add(new PageItem(start + i, (start + i) == currentNumber));
		}
	}

	public List<PageItem> getItems() {
		return items;
	}

	public int getNumber() {
		return currentNumber;
	}

	public List<T> getContent() {
		return page.getContent();
	}

	public int getSize() {
		return page.getSize();
	}

	public int getTotalPages() {
		return page.getTotalPages();
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public class PageItem {
		private int number;
		private boolean current;

		public PageItem(int number, boolean current) {
			this.number = number;
			this.current = current;
		}

		public int getNumber() {
			return this.number;
		}

		public boolean isCurrent() {
			return this.current;
		}
	}
}
