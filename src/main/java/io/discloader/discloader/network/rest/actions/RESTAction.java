package io.discloader.discloader.network.rest.actions;

import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import io.discloader.discloader.common.DiscLoader;

public abstract class RESTAction<T> {

	protected CompletableFuture<String> request;
	protected CompletableFuture<T> future;
	protected DiscLoader loader;
	protected Gson gson;

	public RESTAction(DiscLoader loader) {
		this.loader = loader;
		request = new CompletableFuture<>();
		future = new CompletableFuture<>();
		gson = new Gson();
	}

	public abstract CompletableFuture<T> execute();

	public CompletableFuture<T> execute(CompletableFuture<String> r) {
		request = r;
		request.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return "";
		});
		request.whenCompleteAsync(this::complete);
		return future;
	}

	public void complete(String data, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		future.complete((T) null);
	}

}
