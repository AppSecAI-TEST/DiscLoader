package io.discloader.discloader.network.gateway.packets;

import java.util.HashMap;
import java.util.Map;

import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildMembersChunkJSON;
import io.discloader.discloader.network.json.MemberJSON;

/**
 * @author Perry Berman
 */
public class GuildMembersChunk extends AbstractHandler {

	public GuildMembersChunk(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildMembersChunkJSON data = this.gson.fromJson(d, GuildMembersChunkJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		if (guild == null) return;
		Map<Long, IGuildMember> members = new HashMap<>();
		for (MemberJSON m : data.members) {
			IGuildMember member = EntityBuilder.getGuildFactory().buildMember(guild, EntityRegistry.addUser(m.user), new String[] {}, false, false, null);
			guild.addMember(member);
			members.put(member.getID(), member);
		}
		GuildMembersChunkEvent event = new GuildMembersChunkEvent(guild, members);
		loader.emit(event);
	}
}
