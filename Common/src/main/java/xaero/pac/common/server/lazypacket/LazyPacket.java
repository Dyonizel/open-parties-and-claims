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

package xaero.pac.common.server.lazypacket;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;

public abstract class LazyPacket<E extends LazyPacket.Encoder<P>, P extends LazyPacket<E, P>> {
	
	private final FriendlyByteBuf data;
	private boolean prepared;
	
	public LazyPacket() {
		data = new FriendlyByteBuf(Unpooled.buffer());
	}
	
	protected abstract E getEncoder();
	
	public int getPreparedSize() {
		if(!prepared)
			throw new IllegalStateException("Lazy packet has not been prepared!");
		return data.readableBytes();
	}
	
	public int prepare() {
		if(prepared)
			return data.readableBytes();
		data.clear();
		writeOnPrepare(getEncoder(), data);
		prepared = true;
		return data.readableBytes();
	}
	
	protected abstract void writeOnPrepare(E encoder, FriendlyByteBuf dest);
	
	public static class Encoder<P extends LazyPacket<?, P>> implements BiConsumer<P, FriendlyByteBuf> {

		@Override
		public void accept(P t, FriendlyByteBuf u) {
			LazyPacket<?,P> lazyPacket = t;
			if(!lazyPacket.prepared)
				throw new IllegalStateException("Lazy packet has not been prepared!");
			u.writeBytes(lazyPacket.data, 0, lazyPacket.data.readableBytes());
		}
		
	}

}
