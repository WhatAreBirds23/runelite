/*
 * Copyright (c) 2017, Seth <Sethtroll3@gmail.com>
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
package net.runelite.client.plugins.fishing;

import com.google.common.primitives.Ints;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.queries.NPCQuery;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FishingSpotOverlay extends Overlay
{
	private static final Logger logger = LoggerFactory.getLogger(FishingSpotOverlay.class);

	private final BufferedImage[] imgCache = new BufferedImage[FishingSpot.values().length];

	private final List<Integer> ids = new ArrayList<>();

	private final FishingConfig config;
	private final static Client client = RuneLite.getClient();

	public FishingSpotOverlay(FishingPlugin plugin)
	{
		super(OverlayPosition.DYNAMIC);
		this.config = plugin.getConfig();
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (client.getGameState() != GameState.LOGGED_IN || !config.enabled())
		{
			return null;
		}

		NPCQuery query = new NPCQuery()
			.idEquals(Ints.toArray(ids));
		NPC[] npcs = client.runQuery(query);

		for (NPC npc : npcs)
		{
			FishingSpot spot = FishingSpot.getSpot(npc.getId());

			if (spot == null)
			{
				continue;
			}
			Color color = npc.getId() == FishingSpot.FLYING_FISH ? Color.RED : Color.CYAN;
			if (config.showIcons())
			{
				BufferedImage fishImage = getFishImage(spot);
				if (fishImage != null)
				{
					OverlayUtil.renderActorOverlayImage(graphics, npc, fishImage, color.darker());
				}
			}
			else
			{
				String text = spot.getName();
				OverlayUtil.renderActorOverlay(graphics, npc, text, color.darker());
			}
		}

		return null;
	}

	private BufferedImage getFishImage(FishingSpot spot)
	{
		int fishIdx = spot.ordinal();
		BufferedImage fishImage = null;

		if (imgCache[fishIdx] != null)
		{
			return imgCache[fishIdx];
		}

		try
		{
			InputStream in = FishingSpotOverlay.class.getResourceAsStream(spot.getImage() + ".png");
			fishImage = ImageIO.read(in);
			imgCache[fishIdx] = fishImage;
		}
		catch (IOException e)
		{
			logger.warn("Error Loading fish icon", e);
		}

		return fishImage;
	}

	public void updateConfig()
	{
		ids.clear();
		if (config.showShrimp())
		{
			ids.addAll(Ints.asList(FishingSpot.SHRIMP.getIds()));
		}
		if (config.showLobster())
		{
			ids.addAll(Ints.asList(FishingSpot.LOBSTER.getIds()));
		}
		if (config.showShark())
		{
			ids.addAll(Ints.asList(FishingSpot.SHARK.getIds()));
		}
		if (config.showMonkfish())
		{
			ids.addAll(Ints.asList(FishingSpot.MONKFISH.getIds()));
		}
		if (config.showSalmon())
		{
			ids.addAll(Ints.asList(FishingSpot.SALMON.getIds()));
		}
		if (config.showBarb())
		{
			ids.addAll(Ints.asList(FishingSpot.BARB_FISH.getIds()));
		}
		if (config.showAngler())
		{
			ids.addAll(Ints.asList(FishingSpot.ANGLERFISH.getIds()));
		}
		if (config.showMinnow())
		{
			ids.addAll(Ints.asList(FishingSpot.MINNOW.getIds()));
		}
	}
}
