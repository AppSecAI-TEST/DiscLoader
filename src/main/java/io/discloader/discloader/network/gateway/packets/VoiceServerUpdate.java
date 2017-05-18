/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.entity.voice.VoiceConnect;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.VoiceServerUpdateJSON;

/**
 * @author Perry Berman
 */
public class VoiceServerUpdate extends AbstractHandler {

	public VoiceServerUpdate(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		try {
			VoiceServerUpdateJSON data = gson.fromJson(d, VoiceServerUpdateJSON.class);
			VoiceConnect connection = EntityRegistry.getVoiceConnectionByID(SnowflakeUtil.parse(data.guild_id));
			connection.endpointReceived(data.endpoint, data.token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
