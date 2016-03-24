package com.model.backstage;

public class Back_pay_message {
	private int id;
	private int hid;
	private String account;
	private long time;
	private int productId;
	private String terrace;
	private int zoneid;
//	String original_transaction_id = (String) receipt.get("original_transaction_id");
	private String original_transaction_id;
//	String bvrs = (String) receipt.get("bvrs");
	private String bvrs;
//	String transaction_id = (String) receipt.get("transaction_id");
	private String transaction_id;
//	String quantity = (String) receipt.get("quantity");
	private String quantity;
//	String purchase_date = (String) receipt.get("purchase_date");
	private String purchase_date;
//	String product_id = (String) receipt.get("product_id");
	private String product_id;
//	String original_purchase_date = (String) receipt.get("original_purchase_date");
	private String original_purchase_date;
//	String bid = (String) receipt.get("bid");
	private String bid;
	//	String purchase_date_pst = (String) receipt.get("purchase_date_pst");
	private String purchase_date_pst;
//	String original_purchase_date_ms = (String) receipt.get("original_purchase_date_ms");
	private String original_purchase_date_ms;
//	String original_purchase_date_pst = (String) receipt.get("original_purchase_date_pst");
	private String original_purchase_date_pst;
//	String purchase_date_ms = (String) receipt.get("purchase_date_ms");
	private String purchase_date_ms;
//	String unique_identifier = (String) receipt.get("unique_identifier");
	private String unique_identifier;
//	String unique_vendor_identifier = (String) receipt.get("unique_vendor_identifier");
	private String unique_vendor_identifier;
//	String item_id = (String) receipt.get("item_id");
	private String item_id;
//	String orderId = map.get("orderId");
//	String order = map.get("order");
	private String order;
	private String orderId;
	private int isdone;//交易完成
	private int giveTime;//发货时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHid() {
		return hid;
	}
	public void setHid(int hid) {
		this.hid = hid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getOriginal_transaction_id() {
		return original_transaction_id;
	}
	public void setOriginal_transaction_id(String original_transaction_id) {
		this.original_transaction_id = original_transaction_id;
	}
	public String getBvrs() {
		return bvrs;
	}
	public void setBvrs(String bvrs) {
		this.bvrs = bvrs;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getPurchase_date() {
		return purchase_date;
	}
	public void setPurchase_date(String purchase_date) {
		this.purchase_date = purchase_date;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getOriginal_purchase_date() {
		return original_purchase_date;
	}
	public void setOriginal_purchase_date(String original_purchase_date) {
		this.original_purchase_date = original_purchase_date;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getPurchase_date_pst() {
		return purchase_date_pst;
	}
	public void setPurchase_date_pst(String purchase_date_pst) {
		this.purchase_date_pst = purchase_date_pst;
	}
	public String getOriginal_purchase_date_ms() {
		return original_purchase_date_ms;
	}
	public void setOriginal_purchase_date_ms(String original_purchase_date_ms) {
		this.original_purchase_date_ms = original_purchase_date_ms;
	}
	public String getOriginal_purchase_date_pst() {
		return original_purchase_date_pst;
	}
	public void setOriginal_purchase_date_pst(String original_purchase_date_pst) {
		this.original_purchase_date_pst = original_purchase_date_pst;
	}
	public String getPurchase_date_ms() {
		return purchase_date_ms;
	}
	public void setPurchase_date_ms(String purchase_date_ms) {
		this.purchase_date_ms = purchase_date_ms;
	}
	public String getUnique_identifier() {
		return unique_identifier;
	}
	public void setUnique_identifier(String unique_identifier) {
		this.unique_identifier = unique_identifier;
	}
	public String getUnique_vendor_identifier() {
		return unique_vendor_identifier;
	}
	public void setUnique_vendor_identifier(String unique_vendor_identifier) {
		this.unique_vendor_identifier = unique_vendor_identifier;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public void setTerrace(String terrace) {
		this.terrace = terrace;
	}
	public String getTerrace() {
		return terrace;
	}
	public void setZoneid(int zoneid) {
		this.zoneid = zoneid;
	}
	public int getZoneid() {
		return zoneid;
	}
	
	public void setGiveTime(int giveTime) {
		this.giveTime = giveTime;
	}
	public int getGiveTime() {
		return giveTime;
	}
	public void setIsdone(int isdone) {
		this.isdone = isdone;
	}
	public int getIsdone() {
		return isdone;
	}
}
