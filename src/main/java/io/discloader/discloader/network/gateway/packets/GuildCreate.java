package io.discloader.discloader.network.gateway.packets;

import com.google.gson.Gson;

import io.discloader.discloader.common.event.guild.GuildCreateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Events;

public class GuildCreate extends AbstractHandler {

	public GuildCreate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		Gson gson = new Gson();
		String d = gson.toJson(packet.d);
		GuildJSON data = gson.fromJson(d, GuildJSON.class);
		IGuild guild = null;
		if (EntityRegistry.guildExists(data.id)) guild = EntityRegistry.getGuildByID(data.id);
		if (guild != null) {
			try {
				if (!guild.isAvailable() && !data.unavailable) {
					guild.setup(data);
					loader.checkReady();
					if (socket.status == DLUtil.Status.READY && loader.ready) {
						GuildCreateEvent event = new GuildCreateEvent(guild);
						loader.emit(Events.GUILD_CREATE, event);
						loader.emit(event);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// a brand new guild
			guild = EntityRegistry.addGuild(data);
			if (this.shouldEmit()) {
				GuildCreateEvent event = new GuildCreateEvent(guild);
				loader.emit(Events.GUILD_CREATE, event);
				loader.emit(event);
			}
		}
	}

}
