package io.discloader.discloader.network.rest.actions.channel;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class SendMessage extends RESTAction<Message> {

	private SendableMessage sendable;
	private ITextChannel channel;

	public SendMessage(ITextChannel channel, String content, RichEmbed embed, Attachment attachment, File file) {
		super(channel.getLoader());
		sendable = new SendableMessage(content, embed, attachment, file);
		this.channel = channel;
	}

	public CompletableFuture<Message> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.messages(channel.getID()), Methods.POST, true, sendable));
	}

	public void complete(String r, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		future.complete(new Message(channel, gson.fromJson(r, MessageJSON.class)));
	}

}