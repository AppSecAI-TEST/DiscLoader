package io.discloader.discloader.client.render.texture.icon;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.util.DLUtil.Endpoints;

/**
 * @author Perry Berman
 */
public class UserIcon extends AbstractTexture {

	public final User user;
	private String avatarURL;
	private String hash;

	public UserIcon(User user, String hash) {
		super();
		this.user = user;
		this.hash = hash;
		this.setIconHeight(128);
		this.setIconWidth(128);
		this.setIconName(hash != null ? hash : Long.toUnsignedString(user.getID()));
		this.avatarURL = hash != null ? Endpoints.avatar(user.getID(), hash) : null;

	}

	@Override
	public ImageIcon getImageIcon() {
		return this.createImageIcon(toString());
	}

	@Override
	public Image getImage() {
		return this.getImageIcon().getImage();
	}

	public Image getImage(int width, int height) {
		return this.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	public ImageIcon getImageIcon(int width, int height) {
		return new ImageIcon(this.getImage(width, height));
	}

	protected ImageIcon createImageIcon(String url) {
		URL imgURL = null;
		try {
			imgURL = new URL(url);
		} catch (MalformedURLException e) {
			imgURL = ClassLoader.getSystemResource("assets/discloader/texture/gui/icons/missing-icon.png");
		}
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.print("Couldn't find file: " + url);
			return null;
		}
	}

	public String toHash() {
		return hash;
	}

	/**
	 * @return the avatarURL
	 */
	public String toString() {
		return this.avatarURL;
	}

	/**
	 * @param avatarURL the avatarURL to set
	 */
	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}
}
