package mallProgram;

public class Store{
		
	private String storeName; 	 //매장명
	private String storeKind; 	 //의류매장 , 식품매장 ~
	private String storePhone;	 //전화번호
	private int floor;   		 //매창 위치
	
	public Store() {}
	
	public Store(String storeName, String storeKind, String storePhone, int floor) {
		this.storeName = storeName;
		this.storeKind = storeKind;
		this.storePhone = storePhone;
		this.floor = floor;
	}

	public String getStoreName() {return storeName;}
	public void setStoreName(String storeName) {this.storeName = storeName;}

	public String getStoreKind() {return storeKind;}
	public void setStoreKind(String storeKind) {this.storeKind = storeKind;}

	public String getStorePhone() {return storePhone;}
	public void setStorePhone(String storePhone) {this.storePhone = storePhone;}

	public int getFloor() {return floor;}
	public void setFloor(int floor) {this.floor = floor;}

	@Override
	public String toString() {
		System.out.println("매장이름\t카테고리\t매장전화\t\t위치");
		return storeName + "\t" + storeKind + "\t" + storePhone + "\t" + floor + "층";
	}
}
