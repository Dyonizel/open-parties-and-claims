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

package xaero.pac.common.server.player.permission.impl;

import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import dev.ftb.mods.ftbranks.api.OptionalBoolean;
import dev.ftb.mods.ftbranks.api.PermissionValue;
import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.server.player.permission.api.IPermissionNodeAPI;
import xaero.pac.common.server.player.permission.api.IPlayerPermissionSystemAPI;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.OptionalInt;

public class PlayerFTBPermissionSystem implements IPlayerPermissionSystemAPI {

	@Nonnull
	@Override
	public OptionalInt getIntPermission(@Nonnull ServerPlayer player, @Nonnull IPermissionNodeAPI<Integer> node) {
		String nodeString = node.getNodeString();
		if(nodeString.isEmpty())
			return OptionalInt.empty();
		return FTBRanksAPI.getPermissionValue(player, nodeString).asInteger();
	}

	@Override
	public boolean getPermission(@Nonnull ServerPlayer player, @Nonnull IPermissionNodeAPI<Boolean> node) {
		return FTBRanksAPI.getPermissionValue(player, node.getNodeString()).asBooleanOrFalse();
	}

	@Nonnull
	@Override
	public <T> Optional<T> getPermissionTyped(@Nonnull ServerPlayer player, @Nonnull IPermissionNodeAPI<T> node) {
		String nodeString = node.getNodeString();
		if(nodeString.isEmpty())
			return Optional.empty();
		PermissionValue permissionValue = FTBRanksAPI.getPermissionValue(player, nodeString);
		//going through value types manually (string must be last because a lot of types can have asString implemented)
		Optional<?> optional = permissionValue.asNumber();
		if(optional.isEmpty()) {
			OptionalBoolean optionalBoolean = permissionValue.asBoolean();
			if(optionalBoolean.isPresent())
				optional = Optional.of(optionalBoolean.get());
			else {
				optional = permissionValue.asString();
				if (optional.isEmpty())
					return Optional.empty();
			}
		}
		Object value = optional.get();
		if(value.getClass() != node.getType())
			return Optional.empty();
		@SuppressWarnings("unchecked")
		T valueCast = (T) value;
		return Optional.of(valueCast);
	}

}
