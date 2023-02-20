package com.example.Model;

import javax.persistence.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Entity
@Table(name = "msItem")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long itemId;
	@Column(nullable = false, length = 45)
	private String itemName;
	@Column(nullable = false)
	private int itemPrice;
	@Column(nullable = false, length = 100)
	private String itemDesc;
	@Column(nullable = false, length = 100)
	private String category;
	@Column(nullable = false, length = 100)
	private String image;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private User user;

	@Transient
	public String getPhotosImagePath() {

		return "/user-photos/" + "0" + "/" + image;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(int itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public User user() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
