package io.discloader.discloader.core.entity.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.core.entity.message.embed.MessageEmbed;
import io.discloader.discloader.entity.ISnowflake;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.actions.channel.pin.PinMessage;
import io.discloader.discloader.network.rest.actions.channel.pin.UnpinMessage;
import io.discloader.discloader.util.DLUtil;

/**
 * Represents a message object on the api <br>
 * <b>How to send messages</b>
 * 
 * <pre>
 * Message message = ITextChannel.sendMessage(content).join();
 * </pre>
 * 
 * @author Perry Berman
 * @since 0.0.1
 * @see ITextChannel#sendMessage(String, RichEmbed)
 * @see ISnowflake
 */
public class Message {

	/**
	 * The message's {@link ISnowflake Snowflake} ID.
	 */
	public final String id;

	/**
	 * The message's content
	 */
	public String content;

	/**
	 * The time at which the message has been edited. is null if the message has
	 * not been edited
	 */
	public String edited_timestamp;

	/**
	 * Used for checking if the message has been sent
	 */
	public String nonce;

	/**
	 * The id of the webhook that sent the message. {@code null} if sent by a
	 * bot/user account
	 */
	public String webhookID;

	/**
	 * Whether or not the message was sent using /tts
	 */
	private boolean tts;

	private boolean pinned;

	/**
	 * The time at which the message was sent
	 */
	public final Date timestamp;

	/**
	 * The time at which the message was lasted edited at
	 */
	public Date editedAt;

	/**
	 * An object containing the information about who was mentioned in the
	 * message
	 */
	public Mentions mentions;

	/**
	 * The current instance of DiscLoader
	 */
	public final DiscLoader loader;

	/**
	 * The channel the message was sent in
	 */
	public final ITextChannel channel;

	/**
	 * The user who authored the message
	 */
	public final IUser author;

	/**
	 * The guild the {@link #channel} is in. is {@code null} if
	 * {@link Channel#type} is "dm" or "groupDM"
	 */
	public IGuild guild;

	/**
	 * The member who sent the message if applicable
	 */
	public IGuildMember member;

	private final int type;

	/**
	 * 
	 */
	public ArrayList<MessageEmbed> embeds;

	/**
	 * Creates a new message object
	 * 
	 * @param channel The channel the message was sent in
	 * @param data The message's data
	 */
	public Message(ITextChannel channel, MessageJSON data) {
		this.id = data.id;

		this.channel = channel;

		if (this.channel.isPrivate()) {
			PrivateChannel privateChannel = (PrivateChannel) channel;
			this.loader = privateChannel.getLoader();
		} else {
			TextChannel textChannel = (TextChannel) channel;
			this.loader = textChannel.getLoader();
			this.guild = textChannel.getGuild();
		}

		if (!this.loader.users.containsKey(data.author.id)) {
			this.author = this.loader.addUser(data.author);
		} else {
			this.author = this.loader.users.get(data.author.id);
		}

		this.mentions = new Mentions(this, data.mentions, data.mention_roles, data.mention_everyone);

		this.timestamp = DLUtil.parseISO8601(data.timestamp);

		this.editedAt = data.edited_timestamp != null ? DLUtil.parseISO8601(data.edited_timestamp) : null;

		this.member = this.guild != null ? guild.getMembers().get(author.getID()) : null;

		this.tts = data.tts;

		this.content = data.content;

		this.nonce = data.nonce;

		this.type = data.type;

		this.embeds = new ArrayList<>();
	}

	/**
	 * Deletes the message if the loader has suficient permissions
	 * 
	 * @see DLUtil.PermissionFlags
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	public CompletableFuture<Message> delete() {
		return this.loader.rest.deleteMessage(this.channel, this);
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader
	 * loader} is the message's {@link #author}
	 * 
	 * @param embed The new embed for the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	public CompletableFuture<Message> edit(RichEmbed embed) {
		return this.edit(null, embed);
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader
	 * loader} is the message's {@link #author}
	 * 
	 * @param content The new content of the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	public CompletableFuture<Message> edit(String content) {
		return this.edit(content, null);
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader
	 * loader} is the message's {@link #author}
	 * 
	 * @param content The new content of the message
	 * @param embed The new embed for the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	public CompletableFuture<Message> edit(String content, RichEmbed embed) {
		return this.loader.rest.editMessage(this.channel, this, content, embed, null, null);
	}

	/**
	 * Whether or not you can edit the message.
	 * 
	 * @return {@code true} when {@link #author}.id equals {@link #loader}
	 *         .user.id, {@code false} otherwise.
	 * @since 0.1.0
	 */
	public boolean isEditable() {
		return loader.user.getID().equals(author.getID());
	}

	/**
	 * Is the messaged pinned in the {@link #channel}
	 * 
	 * @return {@code true} if {@code this} is pinned, {@code false} otherwise.
	 */
	public boolean isPinned() {
		return pinned;
	}

	/**
	 * Checks if the message was sent by a user or if it is a system message.
	 * <br>
	 * Ex: "user has pinned a message to this channel." would be a system
	 * message
	 * 
	 * @return true if the message is a system message, false otherwise.
	 * @since 0.1.0
	 */
	public boolean isSystem() {
		return this.type != 0;
	}

	/**
	 * 
	 * @return true if {@code this} was sent using /tts
	 */
	public boolean isTTS() {
		return tts;
	}

	public Message patch(MessageJSON data) {
		this.content = data.content;

		this.mentions.patch(data.mentions, data.mention_roles, data.mention_everyone);

		this.editedAt = DLUtil.parseISO8601(data.edited_timestamp);

		pinned = data.pinned;

		return this;
	}

	/**
	 * Pins {@code this} to the {@link #channel}
	 * 
	 * @return A Future that completes with {@code this} if successful.
	 */
	public CompletableFuture<Message> pin() {
		CompletableFuture<Message> future = new PinMessage(this).execute();
		future.thenAcceptAsync(action -> this.pinned = true);
		return future;
	}

	/**
	 * Unpins {@code this} from the {@link #channel}
	 * 
	 * @return A Future that completes with {@code this} if successful.
	 */
	public CompletableFuture<Message> unpin() {
		CompletableFuture<Message> future = new UnpinMessage(this).execute();
		future.thenAcceptAsync(action -> this.pinned = false);
		return future;
	}

}