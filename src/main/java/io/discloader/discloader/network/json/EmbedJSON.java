/**
 * 
 */
package io.discloader.discloader.network.json;

/**
 * @author Perry Berman
 *
 */
public class EmbedJSON {

	public String title;
	public String type;
	public String description;
	public String url;
	public String timestamp;
	public int color;
	public EmbedFooterJSON footer;
	public EmbedImageJSON image;
	public EmbedThumbnailJSON thumbnail;
	public EmbedVideoJSON video;
	public EmbedProviderJSON provider;
	public EmbedAuthorJSON author;
	public EmbedFieldJSON[] fields;

}
