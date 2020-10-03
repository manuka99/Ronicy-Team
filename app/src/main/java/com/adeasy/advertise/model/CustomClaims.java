package com.adeasy.advertise.model;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class CustomClaims{
	
	private boolean admin;
	private boolean advertisement_manager;
	private boolean favourite_manager;
	private boolean chat_manager;
	private boolean contact_manager;
	private boolean order_manager;
	private boolean user_manager;
	private boolean guest_admin;
	
	public CustomClaims() {
		this.advertisement_manager = false;
		this.favourite_manager = false;
		this.chat_manager = false;
		this.contact_manager = false;
		this.order_manager = false;
		this.admin = false;
		this.user_manager = false;
		this.guest_admin = false;
	}
	
	public boolean isAdvertisement_manager() {
		return advertisement_manager;
	}
	
	public void setAdvertisement_manager(boolean advertisement_manager) {
		this.advertisement_manager = advertisement_manager;
	}
	
	public boolean isFavourite_manager() {
		return favourite_manager;
	}
	
	public void setFavourite_manager(boolean favourite_manager) {
		this.favourite_manager = favourite_manager;
	}
	public boolean isChat_manager() {
		return chat_manager;
	}
	
	public void setChat_manager(boolean chat_manager) {
		this.chat_manager = chat_manager;
	}
	
	public boolean isContact_manager() {
		return contact_manager;
	}
	
	public void setContact_manager(boolean contact_manager) {
		this.contact_manager = contact_manager;
	}
	
	public boolean isOrder_manager() {
		return order_manager;
	}
	
	public void setOrder_manager(boolean order_manager) {
		this.order_manager = order_manager;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isUser_manager() {
		return user_manager;
	}

	public void setUser_manager(boolean user_manager) {
		this.user_manager = user_manager;
	}

	public boolean isGuest_admin() {
		return guest_admin;
	}

	public void setGuest_admin(boolean guest_admin) {
		this.guest_admin = guest_admin;
	}

}
