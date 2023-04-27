package com.example.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

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
	@Column(nullable = false, length = 500)
	private String itemDesc;
	@Column(nullable = false, length = 100)
	private String category;
	@Column(nullable = true, length = 100)
	private String image = "";
	@Column(nullable = true, length = 100)
	private String image1;
	@Column(nullable = true, length = 100)
	private String image2;

	@Column(nullable = true)
	private Timestamp timestamp;
	@OneToMany(mappedBy="item", cascade = CascadeType.ALL)
	private List<Bid> bids;


	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
	@JsonBackReference
    private User user;

	@Column(columnDefinition="tinyint(1) default 1")
	private boolean isAvailable;

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean available) {
		isAvailable = available;
	}

//	@Transient
//	public String getPhotosImagePath() {
//
//		return "/product-photos/" + "0" + "/" + image;
//	}

	@Transient
	public String[] getPhotosImagePaths() {
		String[] imagePaths = new String[3];
		imagePaths[0] = "/product-photos/" + itemId + "/" + image;
		imagePaths[1] = "/product-photos/" + itemId + "/" + image1;
		imagePaths[2] = "/product-photos/" + itemId + "/" + image2;
		return imagePaths;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
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

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}
	//	public User user() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
