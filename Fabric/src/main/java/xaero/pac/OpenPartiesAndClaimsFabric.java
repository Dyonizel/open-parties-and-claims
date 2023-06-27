/*
 * Open Parties and Claims - adds chunk claims and player parties to Minecraft
 * Copyright (C) 2022-2023, Xaero <xaero1996@gmail.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of version 3 of the GNU Lesser General Public License
 * (LGPL-3.0-only) as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received copies of the GNU Lesser General Public License
 * and the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package xaero.pac;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import xaero.pac.client.LoadClientFabric;
import xaero.pac.client.event.ClientEventsFabric;
import xaero.pac.common.capability.CapabilityHelperFabric;
import xaero.pac.common.config.ForgeConfigHelperFabric;
import xaero.pac.common.event.CommonEventsFabric;
import xaero.pac.common.mods.ModSupportFabric;
import xaero.pac.common.packet.PacketHandlerFabric;
import xaero.pac.common.server.data.ServerDataReloadListenerFabric;
import xaero.pac.server.LoadDedicatedServerFabric;

public class OpenPartiesAndClaimsFabric extends OpenPartiesAndClaims implements ClientModInitializer, DedicatedServerModInitializer {

	private ClientEventsFabric clientEvents;
	private CommonEventsFabric commonEvents;

	public OpenPartiesAndClaimsFabric() {
		super(new CapabilityHelperFabric(), PacketHandlerFabric.Builder.begin().build(), new ForgeConfigHelperFabric(), new ModSupportFabric());
		CapabilityHelperFabric.createCapabilities();
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ServerDataReloadListenerFabric());
	}

	@Override
	public void onInitializeClient() {
		LoadClientFabric loader = new LoadClientFabric(this);
		loader.loadCommon();
		loader.loadClient();
	}

	@Override
	public void onInitializeServer() {
		LoadDedicatedServerFabric loader = new LoadDedicatedServerFabric(this);
		loader.loadCommon();
		loader.loadServer();
	}

	@Override
	public ClientEventsFabric getClientEvents() {
		return clientEvents;
	}

	@Override
	public CommonEventsFabric getCommonEvents() {
		return commonEvents;
	}

	public void setClientEvents(ClientEventsFabric clientEvents) {
		this.clientEvents = clientEvents;
	}

	public void setCommonEvents(CommonEventsFabric commonEvents) {
		this.commonEvents = commonEvents;
	}

}
