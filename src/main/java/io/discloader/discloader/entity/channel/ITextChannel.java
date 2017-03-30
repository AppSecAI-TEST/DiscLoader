package io.discloader.discloader.entity.channel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.core.entity.message.MessageFetchOptions;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author perryberman
 * @since 0.0.3
 */
public interface ITextChannel<T extends ITextChannel<T>> extends IChannel {

	/**
	 * Deletes multiple messages from the channel.
	 * 
	 * @param messages The messages to delete
	 * @return A HashMap of the deleted messages if successful.
	 */
	CompletableFuture<Map<String, IMessage<T>>> deleteMessages(@SuppressWarnings("unchecked") IMessage<T>... messages);

	/**
	 * Deletes multiple messages from the channel.
	 * 
	 * @param messages A HashMap of messages to delete
	 * @return A HashMap of the deleted messages if successful.
	 */
	CompletableFuture<Map<String, IMessage<T>>> deleteMessages(Map<String, IMessage<T>> messages);

	/**
	 * @param id The id of the message to fetch
	 * @return a new message object
	 */
	CompletableFuture<IMessage<T>> fetchMessage(String id);

	/**
	 * @return A HashMap of fetched messages
	 */
	CompletableFuture<Map<String, IMessage<T>>> fetchMessages();

	/**
	 * @param options The fetch options to use
	 * @return A HashMap of fetched messages
	 */
	CompletableFuture<Map<String, IMessage<T>>> fetchMessages(MessageFetchOptions options);

	/**
	 * @param id The Snowflake ID of the message
	 * @return The cached message, or null if no message was found
	 */
	IMessage<T> getMessage(String id);

	/**
	 * Gets the channels cached messages
	 * 
	 * @return A HashMap of cached messages
	 */
	Map<String, IMessage<T>> getMessages();

	/**
	 * Downloads and caches a channel's pinned messages
	 * 
	 * @return A Future that completes with a HashMap of the channel's pinned
	 *         messages if successful.
	 */
	CompletableFuture<Map<String, IMessage<T>>> fetchPinnedMessages();

	/**
	 * Checks if any of the channel's cached messages are pinned to the channel,
	 * and returns all cached messages that are pinned.
	 * 
	 * @return A HashMap of pinned messages. Indexed by {@link Message#id
	 *         message.id}.
	 */
	Map<String, IMessage<T>> getPinnedMessages();

	/**
	 * @return A HashMap of typing users. Indexed by {@link User#id}.
	 */
	Map<String, IUser> getTyping();

	/**
	 * Checks if a certain user is typing in this channel
	 * 
	 * @param author The user to check.
	 * @return {@code true} if the user is typing, false otherwise.
	 */
	boolean isTyping(IUser author);

	/**
	 * Pins a message to the channel if not already pinned, and the client has
	 * sufficient permissions
	 * 
	 * @param message The message to pin
	 * @return A Future that completes with the pinned message if successful.
	 */
	CompletableFuture<IMessage<T>> pinMessage(IMessage<T> message);

	/**
	 * Sends a {@link Message} to the channel.
	 * 
	 * @param embed The embed to send
	 * @return A Future that completes with a {@link Message} if successful,
	 */
	CompletableFuture<IMessage<T>> sendEmbed(RichEmbed embed);

	/**
	 * Sends a {@link Message} to the channel.
	 * 
	 * @param content The message's content
	 * @return A Future that completes with a {@link Message} if successful,
	 */
	CompletableFuture<IMessage<T>> sendMessage(String content);

	/**
	 * Sends a {@link Message} to the channel.
	 * 
	 * @param content The message's content
	 * @param embed The RichEmbed to send with the message
	 * @return A Future that completes with the pinned {@link Message} if
	 *         successful.
	 */
	CompletableFuture<IMessage<T>> sendMessage(String content, RichEmbed embed);

	/**
	 * Sets the client as typing in the channel
	 * 
	 * @return A Future that completes with null if successful.
	 */
	CompletableFuture<Map<String, IUser>> startTyping();

	/**
	 * Unpins a message if it is pinned in the channel.
	 * 
	 * @param message The message to unpin.
	 * @return A Future that completes with the unpinned {@link Message} if
	 *         successful.
	 */
	CompletableFuture<IMessage<T>> unpinMessage(IMessage<T> message);

}
