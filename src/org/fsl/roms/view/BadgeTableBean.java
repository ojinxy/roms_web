package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BadgeTableBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2986297779194435737L;

	private List<BadgeNameSearchView> badgeList;
	
	private BadgeNameSearchView selectedBadge;

	
	
	public BadgeTableBean() {
		badgeList = new ArrayList<BadgeNameSearchView>();
		selectedBadge = new BadgeNameSearchView();
	}
	
	public BadgeTableBean(List<BadgeNameSearchView> var) {
		badgeList = var;
		selectedBadge = new BadgeNameSearchView();
	}

	public List<BadgeNameSearchView> getBadgeList() {
		return badgeList;
	}

	public void setBadgeList(List<BadgeNameSearchView> badgeList) {
		this.badgeList = badgeList;
	}

	public BadgeNameSearchView getSelectedBadge() {
		return selectedBadge;
	}

	public void setSelectedBadge(BadgeNameSearchView selectedBadge) {
		this.selectedBadge = selectedBadge;
	}

		
	

}
