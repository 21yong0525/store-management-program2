package mallProgram;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

	public static final int INPUT = 1, UPDATE = 2, SEARHCH = 3, DELETE = 4, OUTPUT = 5, SORT = 6, STATS = 7, EXIT = 8;
	public static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		
		
		DBConnection dbConn = new DBConnection();

		dbConn.connect();

		boolean mainFlag = false;
		int select = 0;
		
		while (!mainFlag) {
			switch (displayMenu(select)) {
			case INPUT :
				insert();
				break;
			case UPDATE :
				update();
				break;
			case SEARHCH :
				search();
				break;
			case DELETE :
				delete();
				break;
			case OUTPUT :
				select();
				break;
			case SORT :
				sort();
				break;
			case STATS :
				stats();
				break;
			case EXIT :
				System.out.println("종료합니다.");
				mainFlag = true;
				break;
			}
		}
	}


	public static void insert() {

		try {
			boolean value = false;
			DBConnection dbc = new DBConnection();
			int insertReturnValue = -1;
			Product product = null;
			dbc.connect();
			
			System.out.println("1.매장 || 2.상품");
			int type = sc.nextInt();
			sc.nextLine();
			value = checkInputPattern(String.valueOf(type),8);
			if (!value) return;
			
			
			switch (type) {
				case 1:// 매장 등록
					System.out.print("매장 이름을 입력하세요 : ");
					String storeName = sc.nextLine();
					value = checkInputPattern(storeName,1);
					if (!value) return;

					System.out.print("매장 종류를 입력하세요 (의류,잡화,기타) : ");
					String storeKind = sc.nextLine();
					value = checkInputPattern(storeKind,2);
					if (!value) return;
					
					System.out.print("매장 전화번호를 입력하세요 (02-0000-0000) : ");
					String storePhone = sc.nextLine();
					value = checkInputPattern(storePhone,3);
					if (!value) return;
					
					System.out.print("매장이 위치한 층을 입력하세요 (1~5층) : ");
					int floor = sc.nextInt();
					value = checkInputPattern(String.valueOf(floor),4);
					if (!value) return;
					
					product = new Product(storeName, storeKind, storePhone, floor);

					break;
				case 2:// 매장을 검색하여 해당 매장 상품 등록
					List<Product> list = new ArrayList<Product>();
					
					System.out.println("상품 등록할 매장을 입력하세요");
					String searchName = sc.nextLine();
					value = checkInputPattern(searchName,1);
					if (!value) return;
					
					list = dbc.selectSearchStore(searchName);
					
					if (list.size() <= 0) {
						System.out.println("해당 매장이 없습니다.");
						return;
					}
					
					for (Product data : list) {
						data.run();
						System.out.println("〓".repeat(70));
						System.out.println("매장의 상품을 등록합니다.");
					}
					
					
					System.out.print("품명을 입력하세요 : ");
					String name = sc.nextLine();
					value = checkInputPattern(name,1);
					if (!value) return;
					
					System.out.print("상품 종류를 입력하세요 : ");
					String kind = sc.nextLine();
					value = checkInputPattern(kind,1);
					if (!value) return;
					
					
					System.out.print("가격을 입력하세요 (0~9999999) : ");
					int price = sc.nextInt();
					value = checkInputPattern(String.valueOf(price),5);
					if (!value) return;
					
					System.out.print("재고를 입력하세요 (0~99999) : ");
					int stock = sc.nextInt();
					value = checkInputPattern(String.valueOf(stock),6);
					if (!value) return;
				

					product = new Product(name, kind, price, stock);
					
					product.setStoreName(searchName);
					product.calDate();
				
					break;
			}

			insertReturnValue = dbc.insert(product,type);

			if (insertReturnValue == -1 || insertReturnValue == 0) {
				System.out.println("등록 실패입니다.");
			} else {
				System.out.println("등록 성공입니다.");
			}
	
			dbc.close();
	
		} catch (InputMismatchException e) {
			System.out.println("잘못 입력하였습니다." + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("잘못 입력하였습니다." + e.getMessage());
			return;
		}
		
	}
	

	public static void update() {
		DBConnection dbc = new DBConnection();
		dbc.connect();
		int type = -1;
		boolean value = false;
		List<Product> list = new ArrayList<Product>();
		
		System.out.println("매장 이름을 입력하세요");
		String searchName = sc.nextLine();
		value = checkInputPattern(searchName,1);
		if (!value) return;
		
		list = dbc.selectSearchStore(searchName);
		
		if (list.size() <= 0) {
			System.out.println("해당 매장이 없습니다.");
			return;
		}
		
		for (Product data : list) {
			data.run();
		}
		Product update = list.get(0);
		
		try {
			System.out.println("1.매장 || 2.상품");
			type = sc.nextInt();
			value = checkInputPattern(String.valueOf(type),8);
			sc.nextLine();
			if (!value) return;
			
			switch (type) {
				case 1:// 매장 정보 수정
					System.out.println("해당 매장의 정보를 수정합니다.");
					
					System.out.println("매장 전화번호를 입력하세요 (02-0000-0000) ");
					String storePhone = sc.nextLine();
					value = checkInputPattern(storePhone,3);
					if (!value) return;
					update.setStorePhone(storePhone);
					
					System.out.println("매장이 위치한 층을 입력하세요 (1~5층) ");
					int floor = sc.nextInt();
					value = checkInputPattern(String.valueOf(floor),4);
					if (!value) return;
					update.setFloor(floor);
					
					break;
				case 2:// 상품 정보 수정
					System.out.println("해당 매장의 상품 정보를 수정합니다.");
					
					System.out.print("수정할 품명을 입력하세요 : ");
					String searchProductName = sc.nextLine();
					value = checkInputPattern(searchProductName,1);
					if (!value) return;
					
					list = dbc.selectSearchProduct(searchProductName,2,searchName);
					
					if (list.size() <= 0) {
						System.out.println("입력된 정보가 없습니다.");
					}
					for (Product product : list) {
						System.out.println(product);
					}
					
					update = list.get(0);
					update.setStoreName(searchName);

					
					System.out.print("가격을 입력하세요 : (0~9999999)");
					int price = sc.nextInt();
					value = checkInputPattern(String.valueOf(price),5);
					if (!value) return;
					update.setPrice(price);
					
					System.out.print("재고를 입력하세요 : (0~99999)");
					int stock = sc.nextInt();
					value = checkInputPattern(String.valueOf(stock),6);
					if (!value) return;
					update.setStock(stock);
					
					
				break;
			}
		} catch (InputMismatchException e) {
			System.out.println("잘못 입력하였습니다." + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("잘못 입력하였습니다." + e.getMessage());
			return;
		}
		
		
		int returnUpdateValue = dbc.update(update,type);
		if (returnUpdateValue == -1) {
			System.out.println("상품 수정 정보 없음");
			return;
		}
		System.out.println("상품 수정 완료하였습니다.");
		
	}

	
	public static void search() {
		DBConnection dbc = new DBConnection();
		dbc.connect();
		int type = -1;
		String searchData = null;
		boolean value = false;
		List<Product> list = new ArrayList<Product>();
		
		System.out.println("매장 이름을 입력하세요");
		String searchName = sc.nextLine();
		value = checkInputPattern(searchName,1);
		if (!value) return;
		
		list = dbc.selectSearchStore(searchName);
		
		if (list.size() <= 0) {
			System.out.println("해당 매장이 없습니다.");
			return;
		}
		
		for (Product data : list) {
			data.run();
		}
		
		try {
			System.out.println("해당 매장 상품검색");
			System.out.println("1.종류 || 2.이름 || 3.나가기");
			type = sc.nextInt();
			sc.nextLine();
			
			switch (type) {
				case 1:
					System.out.print("검색할 상품 종류를 입력하세요 : ");
					searchData = sc.nextLine();
					value = checkInputPattern(searchData,1);
					if (!value) return;
					break;
				case 2:
					System.out.print("검색할 품명을 입력하세요 : ");
					searchData = sc.nextLine();
					value = checkInputPattern(searchData,1);
					if (!value) return;
					break;
				default:
					return;
			}
			
			list = dbc.selectSearchProduct(searchData,type,searchName);
			
			if (list.size() <= 0) {
				System.out.println("정보가 없습니다.");
			}
			for (Product product : list) {
				System.out.println(product);
			}
		
			dbc.close();
			
		} catch (InputMismatchException e) {
			System.out.println("잘못 입력하였습니다." + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("잘못 입력하였습니다." + e.getMessage());
			return;
		}
		
	}


	public static void delete() {
		DBConnection dbc = new DBConnection();
		dbc.connect();
		boolean value = false;
		List<Product> list = new ArrayList<Product>();
		
		try {
			System.out.println("매장 이름을 입력하세요");
			String searchName = sc.nextLine();
			value = checkInputPattern(searchName,1);
			if (!value) return;
			
			list = dbc.selectSearchStore(searchName);
			
			if (list.size() <= 0) {
				System.out.println("해당 매장이 없습니다.");
				return;
			}
			
			for (Product data : list) {
				data.run();
			}
			
			System.out.println("해당 매장을 삭제하시겠습니까? \n1.삭제 || 2.취소");
			int select = sc.nextInt();
			value = checkInputPattern(String.valueOf(select),8);
			if (!value) return;
			
			switch (select) {
				case 1:
					int deleteReturnValue = dbc.delete(searchName);
					if (deleteReturnValue == -1) {
						System.out.println("삭제 실패입니다.");
					} else {
						System.out.println("해당 매장의 정보와 상품을 삭제하였습니다.");
					}
					break;
				case 2:
					System.out.println("취소하였습니다.");
					break;
			}
			
		} catch (InputMismatchException e) {
			System.out.println("잘못 입력하였습니다." + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("데이터베이스 삭제 에러입니다. " + e.getMessage());
			return;
		} finally {
			dbc.close();
		}
		
	}


	public static void select() {
		boolean value = false;
		List<Product> list = new ArrayList<Product>();
		
		try {
			DBConnection dbc = new DBConnection();
			dbc.connect();
			
			System.out.println("1.현재정보 || 2.삭제정보");
			int saveSelect = sc.nextInt();
			
			switch (saveSelect) {
			case 1:
				list = dbc.select(1);
				
				if (list.size() <= 0) {
					System.out.println("매장 정보가 없습니다.");
					return;
				}
	
				for (Product product : list) {
					product.run();
				}
				System.out.println("〓".repeat(70));
	
				System.out.println("상품을 출력하시겠습니까? \n1.출력 || 2.취소");
				int select = sc.nextInt();
				value = checkInputPattern(String.valueOf(select),8);
				if (!value) return;
				
				switch (select) {
					case 1:
						list = dbc.select(2);
						
						if (list.size() <= 0) {
							System.out.println("상품 정보가 없습니다.");
						}
	
						for (Product product : list) {
							System.out.println(product);
						}
						break;
					case 2:
						System.out.println("취소합니다");
						break;
				}
				break;
			case 2:
				list = dbc.selectDeleteData(1);
				
				if (list.size() <= 0) {
					System.out.println("매장 정보가 없습니다.");
					return;
				}
	
				for (Product product : list) {
					product.run();
				}
				System.out.println("〓".repeat(70));
	
				System.out.println("상품을 출력하시겠습니까? \n1.출력 || 2.취소");
				select = sc.nextInt();
				value = checkInputPattern(String.valueOf(select),8);
				if (!value) return;
				
				switch (select) {
					case 1:
						list = dbc.selectDeleteData(2);
						
						if (list.size() <= 0) {
							System.out.println("상품 정보가 없습니다.");
						}
	
						for (Product product : list) {
							System.out.println(product);
						}
						break;
					case 2:
						System.out.println("취소합니다");
						break;
				}
				break;
			}
			dbc.close();

		} catch (Exception e) {
			System.out.println("데이터베이스 출력 에러입니다." + e.getMessage());
			return;
		} 
	}

	
	public static void sort() {
		List<Product> list = new ArrayList<Product>();

		try {
			DBConnection dbc = new DBConnection();
			dbc.connect();
			
			System.out.println("등록된 상품을 정렬하여 출력합니다.");
			System.out.print("정렬 방식을 선택하세요 (1.종류 || 2.가격 || 3.재고 >> ");
			int type = sc.nextInt();

			boolean value = checkInputPattern(String.valueOf(type), 9);
			if (!value) return;
	

			list = dbc.selectOrderBy(type);
			if (list.size() <= 0) {
				System.out.println("상품 정보가 없습니다.");
			}

			for (Product product : list) {
				System.out.println(product);
			}

			dbc.close();
		} catch (InputMismatchException e) {
			System.out.println("잘못 입력하였습니다. (1~3)" + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("데이터베이스 출력 에러입니다. " + e.getMessage());
			return;
		}
	}
	
	
	public static void stats() {
		List<Product> list = new ArrayList<Product>();

		try {
			System.out.println("상품 통계 메뉴입니다.");
			System.out.println("1.최고가 || 2.최저가");
			int type = sc.nextInt();
			
			boolean value = checkInputPattern(String.valueOf(type),8);
			if (!value) return;

			DBConnection dbc = new DBConnection();
			dbc.connect();
			list = dbc.selectMaxMin(type);
			
			if (list.size() <= 0) {
				System.out.println("상품의 정보가 없습니다.");
			}
			
			for (Product product : list) {
				System.out.println(product);
			}

			dbc.close();
			
		} catch (InputMismatchException e) {
			System.out.println("잘못 입력하였습니다. (1~2)" + e.getMessage());
			sc.nextLine();
			return;
		} catch (Exception e) {
			System.out.println("데이터베이스 에러입니다. " + e.getMessage());
			return;
		}
	}
	
	
	public static boolean checkInputPattern(String data, int patternType) {
		String pattern = null;
		boolean regex = false;
		String message = null;

		switch (patternType) {
		case 1:
			pattern = "^[가-힝]{1,9}$";
			message = "잘못 입력하였습니다. 다시 입력하세요";
			break;
		case 2:
			pattern = "^[가-힝]{1,3}$";
			message = "종류를 다시 입력하세요";
			break;
		case 3:
			pattern = "^02-[0-9]{3,4}-[0-9]{3,4}$";
			message = "전화번호를 다시 입력하세요 (02-0000-0000)";
			break;
		case 4:
			pattern = "^[1-5]$";
			message = "위치를 다시 입력하세요 (1~5)";
			break;
		case 5:
			pattern = "^[0-9]{1,7}$";
			message = "가격을 다시 입력하세요 (0~9999999)";
			break;
		case 6:
			pattern = "^[0-9]{1,5}$";
			message = "재고를 다시 입력하세요 (0~99999)";
			break;
		case 7:
			pattern = "^[1-8]*$";
			message = "다시 입력하세요 (1~8)";
			break;
		case 8:
			pattern = "^[1-2]*$";
			message = "다시 입력하세요 (1~2)";
			break;
		case 9:
			pattern = "^[1-3]*$";
			message = "다시 입력하세요 (1~2)";
			break;
		}

		regex = Pattern.matches(pattern, data);

		if (patternType == 6) {
			if (!regex || Integer.parseInt(data) < 0) {
				System.out.println(message);
				return false;
			}
		} else {
			if (!regex) {
				System.out.println(message);
				return false;
			}
		}
		return regex;
	}
	
	/** 메뉴 선택 */
	public static int displayMenu(int select) {
		try {
			System.out.println("〓".repeat(70));
			System.out.println("1.입력 || 2.수정 || 3.검색 || 4.삭제 || 5.출력 || 6.정렬 || 7.통계 || 8.종료");
			System.out.println("〓".repeat(70));
			select = sc.nextInt();

			checkInputPattern(String.valueOf(select),7);
			
		} catch (InputMismatchException e) {
			System.out.println("다시 입력하세요 (1~8)");
		} finally {
			sc.nextLine();
		}
		return select;
	}
}
