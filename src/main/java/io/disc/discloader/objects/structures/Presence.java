package io.disc.discloader.objects.structures;

import io.disc.discloader.objects.gateway.GameJSON;
import io.disc.discloader.objects.gateway.PresenceJSON;

public class Presence {
	/**
	 * The {@link Game} the user is currently playing, or {@link null} if user isn't playing a game
	 * @author Perry Berman
	 * @see Game
	 * @see User
	 * @see GuildMember
	 */
	public Game game;
	/**
	 * The status of the current user. can be {@literal online}, {@literal offline}, {@literal idle}, or {@literal invisible}
	 * @author Perry Berman
	 */
	public String status;

	public Presence(PresenceJSON presence) {
		this.game = presence.game != null ? new Game(presence.game) : null;
		this.status = presence.status;
	}

	public Presence(String status, GameJSON game) {
		this.game = new Game(game);
		this.status = status;
	}

	public Presence() {
		this.game = null;
		this.status = "offline";
	}

	public Presence(Presence data) {
		this.game = data.game != null ? new Game(data.game) : null;
		this.status = data.status;
	}

	public void update(PresenceJSON presence) {
		this.update(presence.status, presence.game != null ? new Game(presence.game) : null);
	}

	public void update(String status, Game game) {
		this.status = status != null ? status : this.status;
		this.game = game;
	}
	
	public void update(String status) {
		this.update(status, this.game);
	}
	
	public void update(Game game) {
		this.update(null, game);
	}
	
	
}
