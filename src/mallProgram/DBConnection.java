package mallProgram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DBConnection {
	private Connection connection = null;

	/** connection */
	public void connect() {
		Properties properties = new Properties();

		try {
			FileInputStream fis = new FileInputStream("C:/java_test/mallProgram/src/mallProgram/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Fproperties.load error" + e.getMessage());
		}

		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("userid"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("connerction error" + e.getMessage());
		}
		
	}

	/** insert */ //매장 및 상품 등록
	public int insert(Product product,int type) {
		PreparedStatement ps = null;
		String insertQuery = null;
		int insertReturnValue = -1;
		try {
			switch (type) {
			case 1 : // 매장 등록
				insertQuery = "insert into store values(?,?,?,?)";
				ps = connection.prepareStatement(insertQuery);
				ps.setString(1, product.getStoreName());
				ps.setString(2, product.getStoreKind());
				ps.setString(3, product.getStorePhone());
				ps.setInt(4, product.getFloor());
				insertReturnValue = ps.executeUpdate();
				break;
			case 2 : // 상품 등록
				insertQuery = "insert into product values(?,?,?,?,?,?)";
				ps = connection.prepareStatement(insertQuery);
				ps.setString(1, product.getStoreName());
				ps.setString(2, product.getName());
				ps.setString(3, product.getKind());
				ps.setInt(4, product.getPrice());
				ps.setInt(5, product.getStock());
				ps.setString(6, product.getDate());
				insertReturnValue = ps.executeUpdate();
				break;
			}
		} catch (SQLException e) {
			if(e.getMessage().equals("Duplicate entry '"+product.getStoreName()+"' for key 'PRIMARY'")) {
				System.out.println("동일한 이름의 매장이 존재합니다.");
			} else {
				System.out.println("SQLException error"+e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
				try {
					if (ps != null) ps.close();
				} catch (SQLException e) {
					System.out.println("PreparedStatement close error" + e.getMessage());
				}
			}
		return insertReturnValue;
	}
	
	/** update */ // 매장 및 상품 수정
	public int update(Product product,int type) {
		PreparedStatement ps = null;
		String insertQuery = null;
		int updateReturnValue = -1;
		
		try {
			switch (type) {
			case 1:
				insertQuery = "update store set storeKind = ?, storePhone = ?, floor = ? where storeName = ?";
				ps = connection.prepareStatement(insertQuery);
				ps.setString(1, product.getStoreKind());
				ps.setString(2, product.getStorePhone());
				ps.setInt(3, product.getFloor());
				ps.setString(4, product.getStoreName());
				updateReturnValue = ps.executeUpdate();
				break;
			case 2:
				insertQuery = "update product set price = ?, stock = ? where storeName = ? and name = ?";
				ps = connection.prepareStatement(insertQuery);
				ps.setInt(1, product.getPrice());
				ps.setInt(2, product.getStock());
				ps.setString(3, product.getStoreName());
				ps.setString(4, product.getName());
				updateReturnValue = ps.executeUpdate();
				break;
			}
			
		} catch (SQLException e) {
			System.out.println("update error" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}
		
		
		return updateReturnValue;
	}
	
	/** searchStore statement */ //매장 검색
	public List<Product> selectSearchStore(String data) {
		Statement st = null;
		String selectSearchQuery = "select * from store where storeName = '"+data+"'";
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();
	
		try {
			st = connection.createStatement();

			rs = st.executeQuery(selectSearchQuery);

			if (!(rs != null || rs.isBeforeFirst())) return list;

			while (rs.next()) {
				String storeName = rs.getString("storeName");
				String storeKind = rs.getString("storeKind");
				String storePhone = rs.getString("storePhone");
				int floor = rs.getInt("floor");
				list.add(new Product(storeName, storeKind, storePhone, floor));
			}

		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				System.out.println("Statement close error" + e.getMessage());
			}
		}

		return list;
	}
	
	/** searchProduct statement */ //상품 검색
	public List<Product> selectSearchProduct(String data, int type, String searchName) {
		PreparedStatement ps = null;

		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();
		String selectSearchQuery = "select * from product where storename = ";

		try {
			switch (type) {
			case 1:
				selectSearchQuery += "? and kind = ?";
				break;
			case 2:
				selectSearchQuery += "? and name = ?";
				break;
			default:
				System.out.println("잘못 입력하였습니다.");
				return list;
			}

			ps = connection.prepareStatement(selectSearchQuery);
			ps.setString(1, searchName);
			ps.setString(2, data);
			rs = ps.executeQuery();

			if (!(rs != null || rs.isBeforeFirst())) return list;

			while (rs.next()) {
				String storeName = rs.getString("storeName");
				String name = rs.getString("name");
				String kind = rs.getString("kind");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");
				String date = rs.getString("date");
				list.add(new Product(storeName, name, kind, price, stock, date));
			}

		} catch (Exception e) {
			System.out.println("search error" + e.getMessage());
		} finally {
			try {
				if (ps != null) ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}
	
    /** delete */
    public int delete(String searchName) {
          Statement st = null;
          String dleleteQuery = "call procedure_delete_store_product('"+ searchName +"')";
          int deleteReturnValue = -1;

          try {
                st = connection.createStatement();
                deleteReturnValue = st.executeUpdate(dleleteQuery);
                
          } catch (SQLException e) {
                System.out.println("SQLException error" + e.getMessage());
          } catch (Exception e) {
                System.out.println("Exception error" + e.getMessage());
          } finally {
                try {
                      if (st != null) st.close();
                } catch (SQLException e) {
                      System.out.println("PreparedStatement close error" + e.getMessage());
                }
          }

          return deleteReturnValue;
    }
	
	/** select */
	public List<Product> select(int type) {
		Statement st = null;
		String selectQuery = "select * from ";
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();

		try {
			switch (type) {
				case 1:
					selectQuery += "store";
					st = connection.prepareStatement(selectQuery);
					rs = st.executeQuery(selectQuery);

					if (!(rs != null || rs.isBeforeFirst())) return list;

					while (rs.next()) {
						String storeName = rs.getString("storeName");
						String storeKind = rs.getString("storeKind");
						String storePhone = rs.getString("storePhone");
						int floor = rs.getInt("floor");
						list.add(new Product(storeName, storeKind, storePhone, floor));
					}
					
					break;
				case 2:
					selectQuery += "product";
					st = connection.prepareStatement(selectQuery);
					rs = st.executeQuery(selectQuery);

					if (!(rs != null || rs.isBeforeFirst())) return list;

					while (rs.next()) {
						String storeName = rs.getString("storeName");
						String name = rs.getString("name");
						String kind = rs.getString("kind");
						int price = rs.getInt("price");
						int stock = rs.getInt("stock");
						String date = rs.getString("date");
						list.add(new Product(storeName, name, kind, price, stock, date));
					}
					break;
			}

		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}

	/** selectDeleteData */
	public List<Product> selectDeleteData(int type) {
		Statement st = null;
		String selectQuery = "select * from ";
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();

		try {
			switch (type) {
				case 1:
					selectQuery += "deletestore";
					st = connection.createStatement();
					rs = st.executeQuery(selectQuery);

					if (!(rs != null || rs.isBeforeFirst())) return list;

					while (rs.next()) {
						String storeName = rs.getString("storeName");
						String storeKind = rs.getString("storeKind");
						String storePhone = rs.getString("storePhone");
						int floor = rs.getInt("floor");
						list.add(new Product(storeName, storeKind, storePhone, floor));
					}
					
					break;
				case 2:
					selectQuery += "deleteproduct";
					st = connection.prepareStatement(selectQuery);
					rs = st.executeQuery(selectQuery);

					if (!(rs != null || rs.isBeforeFirst())) return list;

					while (rs.next()) {
						String storeName = rs.getString("storeName");
						String name = rs.getString("name");
						String kind = rs.getString("kind");
						int price = rs.getInt("price");
						int stock = rs.getInt("stock");
						String date = rs.getString("deletedate");
						list.add(new Product(storeName, name, kind, price, stock, date));
					}
					break;
			}

		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}
	
	
	/** selectOrderBy */
	public List<Product> selectOrderBy(int type) {
		PreparedStatement ps = null;
		String selectOrderByQuery = "select * from product order by ";
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();

		try {
			switch (type) {
				case 1: selectOrderByQuery += "kind asc"; break;
				case 2: selectOrderByQuery += "price desc"; break;
				case 3: selectOrderByQuery += "stock desc"; break;
				default : System.out.println("다시 입력하세요"); return list;
			}
			
			ps = connection.prepareStatement(selectOrderByQuery);

			rs = ps.executeQuery(selectOrderByQuery);

			if (!(rs != null || rs.isBeforeFirst())) return list;

			
			while (rs.next()) {
				String storeName = rs.getString("storeName");
				String name = rs.getString("name");
				String kind = rs.getString("kind");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");
				String date = rs.getString("date");
				list.add(new Product(storeName, name, kind, price, stock, date));
			}

		} catch (Exception e) {
			System.out.println("select error" + e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				System.out.println("PreparedStatement close error" + e.getMessage());
			}
		}

		return list;
	}
	
	/** selectMaxMin */
	public List<Product> selectMaxMin(int type) {
		Statement st = null;
		String selectMaxMinQuery = "select * from product where price = ";
		ResultSet rs = null;
		List<Product> list = new ArrayList<Product>();

		try {
			st = connection.createStatement();
			
			switch (type) {
				case 1: selectMaxMinQuery += "(select MAX(price) from product);"; break;
				case 2: selectMaxMinQuery += "(select MIN(price) from product);"; break;
				default : System.out.println("다시 입력하세요"); return list;
			}

			rs = st.executeQuery(selectMaxMinQuery);

			if (!(rs != null || rs.isBeforeFirst())) return list;

			while (rs.next()) {
				String storeName = rs.getString("storeName");
				String name = rs.getString("name");
				String kind = rs.getString("kind");
				int price = rs.getInt("price");
				int stock = rs.getInt("stock");
				String date = rs.getString("date");
				list.add(new Product(storeName, name, kind, price, stock, date));
			}

		} catch (Exception e) {
			System.out.println("Exception error" + e.getMessage());
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				System.out.println("Statement close error" + e.getMessage());
			}
		}

		return list;
	}
	
	/** Connection close */
	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Connection close error" + e.getMessage());
		}
	}
}
