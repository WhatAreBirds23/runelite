/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.ui.overlay.infobox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfoBoxManager
{
	private static final Logger logger = LoggerFactory.getLogger(InfoBoxManager.class);

	private final List<InfoBox> infoBoxes = new ArrayList<>();

	public void addInfoBox(InfoBox infoBox)
	{
		logger.debug("Adding InfoBox {}", infoBox);
		infoBoxes.add(infoBox);
	}

	public void removeInfoBox(InfoBox infoBox)
	{
		logger.debug("Removing InfoBox {}", infoBox);
		infoBoxes.remove(infoBox);
	}

	public void removeIf(Predicate<InfoBox> filter)
	{
		logger.debug("Removing InfoBoxs for filter {}", filter);
		infoBoxes.removeIf(filter);
	}

	public List<InfoBox> getInfoBoxes()
	{
		return Collections.unmodifiableList(infoBoxes);
	}

	public void cull()
	{
		for (Iterator<InfoBox> it = infoBoxes.iterator(); it.hasNext();)
		{
			InfoBox box = it.next();

			if (box.cull())
			{
				logger.debug("Culling InfoBox {}", box);
				it.remove();
			}
		}
	}
}
