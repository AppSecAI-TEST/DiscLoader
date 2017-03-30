package io.discloader.discloader.network.rest.actions.channel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

/**
 * @author Perry Berman
 */
public class BulkDelete<T extends ITextChannel<T>> extends RESTAction<Map<String, IMessage<T>>> {

	public T channel;
	public Map<String, IMessage<T>> messages;

	public BulkDelete(T channel, Map<String, IMessage<T>> messages) {
		super(channel.getLoader());
		this.channel = channel;
		this.messages = messages;
	}

	public CompletableFuture<Map<String, IMessage<T>>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.bulkDelete(channel.getID()), Methods.GET, true));
	}

	@Override
	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		future.complete(messages);
	}

}
