package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.GameJSON;

public class Game {
	public String name;
	public int type;
	public String url;
	public boolean streaming;
	
	public Game(GameJSON game) {
		this.name = game.name;
		this.type = game.type;
		this.url = game.url;
		this.streaming = this.type != 0 || this.type != 1 || this.type != 2 ? false : true;
	}
}
