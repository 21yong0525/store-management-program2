package mallProgram;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

//쇼핑몰 상품 관리 프로그램
public class Product extends Store implements Comparable<Product>, Serializable, Runnable {
	
	//상품 정보
	private String name; 	// 이름
	private String kind; 	// 종류
	private int price;		// 가격
	private int stock;		// 재고
	private String date;	// 상품등록날짜

	public Product(String storeName, String storeKind, String storePhone, int floor) {
		super(storeName, storeKind, storePhone, floor);
	}

	public Product(String storeName, String name, String kind, int price, int stock, String date) {
		this(name,kind,price,stock);
		super.setStoreName(storeName);
		this.date = date;
	}
	
	public Product(String name, String kind ,int price, int stock) {
		this.name = name;
		this.kind = kind;
		this.price = price;
		this.stock = stock;
	}

	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	public String getKind() {return kind;}
	public void setKind(String kind) {this.kind = kind;}

	public int getPrice() {return price;}
	public void setPrice(int price) {this.price = price;}

	public int getStock() {return stock;}
	public void setStock(int stock) {this.stock = stock;}

	public String getDate() {return date;}
	public void setDate(String date) {this.date = date;}
	
	public void calDate() {
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		this.date = sdformat.format(new Date());
	}
	
	@Override
	public int hashCode() {
		return this.kind.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Product)) return false;
		return this.kind.equals(((Product)obj).kind);
	}
	
	@Override
	public int compareTo(Product product) {
		return this.kind.compareToIgnoreCase(product.kind);
	}
	
	@Override
	public String toString() {
		System.out.println("〓".repeat(70));
		System.out.println("상품 정보\n판매매장\t품명\t종류\t가격\t재고\t등록일");
		return this.getStoreName() + "\t" + name + "\t" + kind + "\t" + price +"\t"+ stock + "\t" + date;
	}

	@Override
	public void run() {
		System.out.println("〓".repeat(70));
		System.out.println(super.toString());
	}
}
