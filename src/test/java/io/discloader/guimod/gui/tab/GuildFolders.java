/**
 * 
 */
package io.discloader.guimod.gui.tab;

import javax.swing.event.ListSelectionEvent;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Guild;
import io.discloader.guimod.gui.info.AbstractInfo;
import io.discloader.guimod.gui.info.GuildInfo;
import io.discloader.guimod.gui.list.AbstractList;
import io.discloader.guimod.gui.list.GuildList;

/**
 * @author Perry Berman
 *
 */
public class GuildFolders<T extends Guild> extends AbstractFolder<T, GuildList<T>, GuildInfo<T>> {

	private static final long serialVersionUID = -1589736360078814907L;

	public GuildFolders(DiscLoader loader) {
		super(loader);
	}
	
	@Override
	public GuildList<T> createList() {
		return new GuildList<T>(this.loader);
	}
	
	@Override
	public GuildInfo<T> createInfo() {
		return new GuildInfo<T>();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (list.folders.getSelectedIndex() != -1) {
			info.update(list.items.get(list.folders.getSelectedIndex()));
		}
	}
}
