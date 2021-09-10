package com.five.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cart {
	
	@Id
	@Column(nullable = false)
	String buyerid;
	//@Id
	@Column(nullable = false)
	String prodid;
	@Column(nullable = false)
	Integer quantity;
	
	public Cart() {
		super();
	}


	public String getBuyerid() {
		return buyerid;
	}



	public void setBuyerid(String buyerid) {
		this.buyerid = buyerid;
	}



	public String getProdid() {
		return prodid;
	}



	public void setProdid(String prodid) {
		this.prodid = prodid;
	}



	public Integer getQuantity() {
		return quantity;
	}



	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}



	public Cart(String buyerid, String prodid, Integer quantity) {
		super();
		this.buyerid = buyerid;
		this.prodid = prodid;
		this.quantity = quantity;
	}


	@Override
	public String toString() {
		return "Cart [buyerid=" + buyerid + ", prodid=" + prodid + ", quantity=" + quantity + "]";
	}

	
}
