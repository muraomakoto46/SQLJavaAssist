package insertDB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
/*半分自動でDBにinsert intoしてくれるソースコードを生成します。
 * 対話型で入力していけばソースコードが生成されます。
 * しかし一字一句間違えずに入力できるとは思えないな。どこかで間違える。
 * だからもっと良い手段を考案したいな。*/
public class DBInsertClassSourceCodeMaker {
	/* ctrl + i で、字下げインデントできるよ。
	 * クラス名 SeitoInsert.java

	パッケージ名 insertDB
	DBへのアドレス jdbc:mariadb://localhost/
	databaseName SeitoNoSeiseki
	tableName seito
	userName root
	password 1234
	 * */
	
	//記入例を示しておく。
	private String className; //= "SeitoInsert";
	private String packageName; //= "insertDB";
	private String interfaceName; //="InterfaceRegisterEmployee";
	private String jdbcAddress; //= "jdbc:mariadb://localhost/";
	private String databaseName; //= "SeitonoSeiseki";
	private String tableName; //= "seito";
	private String userName; //= "root";
	private String yourPassword; //= "1234";
	private String directory; // = "C:\\Users\\Makoto\\Documents\\"
	
	private String sengenImport ="import java.sql.Connection;\n"
			+ "import java.sql.DriverManager;\n"
			+ "import java.sql.SQLException;\n"
			+ "import java.sql.Statement;";
	
	private StringBuilder hikisuwLine;
	private StringBuilder retsumeiLine;
	private StringBuilder insertHikisuwLine;
	
	public DBInsertClassSourceCodeMaker() {
		hikisuwLine = new StringBuilder();
		retsumeiLine = new StringBuilder();
		insertHikisuwLine = new StringBuilder();
		Scanner sc = new Scanner(System.in);
		System.out.println("DBのユーザー名を入力して下さい。");
		userName=new String(sc.nextLine());
		System.out.println("DBパスワードを入力してください。");
		yourPassword=new String(sc.nextLine());
		System.out.println("JDBCのアドレスを入力して下さい。");
		jdbcAddress = new String(sc.nextLine());
		System.out.println("DB名を入力してください。");
		databaseName = new String(sc.nextLine());
		System.out.println("table名を入力してください。");
		tableName = new String(sc.nextLine());
		System.out.println("クラス名を入力してください。");
		className = new String(sc.nextLine());
		System.out.println("package名を入力してください。");
		packageName = new String(sc.nextLine());
		System.out.println("interface名を入力してください。");
		interfaceName= new String(sc.nextLine());
		System.out.println("ファイルの保存先を入力してください。");
		directory=new String(sc.nextLine());
		//sc.close();//sc.close()を書いてはいけない。エラーの原因となる。使ってはいけないメソッドだ。
	}
	public void makeCode() {
		//designateProperties();
		requireColumn();
		//上から順に作っていかなきゃ。最終の段階の出力は一番最後に作りましょう。
		try {
			FileWriter fileWriter = new FileWriter(directory+className+".java");
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));
			
			printWriter.println("package "+packageName+";\n");
			printWriter.println(sengenImport+"\n");
			printWriter.println("public class "+className+ " implements "+interfaceName+"{"+"\n");
			printWriter.println("	private String address = \""+jdbcAddress+"\";\n"
					+ "	private String databaseName=\""+databaseName+"\";\n"
					+ "	private String tableName=\""+tableName+"\";\n"
					+ "	private String userName=\""+userName+"\";\n"
					+ "	private String password=\""+yourPassword+"\";");
			printWriter.println("public "+className+"{\n"
					+ "		\n"
					+ "	}");
			printWriter.println("	@Override");
			
			printWriter.print("public void insert(");
			//String name, String birthday, String seibetsu
			printWriter.print(hikisuwLine.toString());
			printWriter.println("){\n");
			printWriter.println("		try(Connection connection = DriverManager.getConnection(");
			printWriter.println("jdbcAddress"+"+databaseName"+","+userName+","+yourPassword+")){");
			printWriter.println("			try(Statement statement = connection.createStatement()){");
			printWriter.println("			statement.executeUpdate(");
			
			printWriter.println("						\"INSERT INTO \"");
			printWriter.println(" +tableName ");
			printWriter.println("+\" (\"");
			//printWriter.print("+\"name,\"\n"
			//		+ "						+\"seinengappi,\"\n"
			//		+ "						+\"seibetsu)\"")
			printWriter.println("+"+retsumeiLine.toString());
			printWriter.println("+\")\"");
			printWriter.println("+\"VALUES(\"");
			//+"'"+name+"'"+","
			
			printWriter.println("+"+insertHikisuwLine.toString());
			printWriter.println("+\");\"");
			
			printWriter.println("				);\n"
					+ "			}\n"
					+ "		}catch(SQLException exception) {\n"
					+ "			System.out.println(exception.toString());\n"
					+ "		}\n"
					+ "	}\n"
					+ "	\n"
					+ "}");
			printWriter.close();
		
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	/*クラス名を決めてください*/
	/*private void designateClassName() {
		do {
			print("クラス名を決めてください");
			className = inputString();
			print(className+"でよろしいか? (hai/iie)");
		}while(!inputString().equals("hai"));
	}*/
	/*一般化されたもの クラス名を決めてもらうdesignateClassNameを一般化したもの*/
	private void designate(String yourOrder,String objective) {
		do {
			print(yourOrder+"を決めてください。");
			objective = inputString();
			print(objective+"でよろしいか? (hai/iie)");
		}while(!inputString().equals("hai"));
	}
	private void designateProperties() {
		designate("クラス名",className);
		designate("package名",packageName);
		designate("interface名",interfaceName);
		designate("jdbcのアドレス",jdbcAddress);
		designate("database名",databaseName);
		designate("table名",tableName);
		designate("user名",userName);
		designate("パスワード",yourPassword);
	}
	private void requireColumn() {
		String retsuName="";
		String hikisuw="";
		do {
			while(true) {
				boolean result = false;
				System.out.println("列の名前を記入してください");
				retsuName = inputString();

				System.out.println("対応するメソッドの引数を記入してください。");
				hikisuw = inputString();

				System.out.println("列名："+retsuName+"/引数の名前:"+hikisuw);
				System.out.println("OKですか？(ok or no)");
				if(inputString().equals("ok")) {
					//"name,"
					retsumeiLine.append("\""+retsuName+"\"");
					hikisuwLine.append("String ");
					hikisuwLine.append(hikisuw);
					//"'"+name+"'"+","
					insertHikisuwLine.append("\"\'\"+");
					insertHikisuwLine.append(hikisuw);
					insertHikisuwLine.append("+\"\'\"");
					System.out.println("列はまだある？(aru or nai)");
					if(inputString().equals("nai")) {
						result=true;
					}else {
						retsumeiLine.append(",");
						hikisuwLine.append(",");
						insertHikisuwLine.append(",");
					}
				}
				if(result) {
					break;
				}
					
			}

			print("一覧表を表示します。");
			print(retsumeiLine.toString());
			print(hikisuwLine.toString());
			print("これで確定していいか？:(kakutei or yarinaoshi)");
		}while(!inputString().equals("kakutei"));
	}
	private String inputString() {
		Scanner scan = new Scanner(System.in);
		return scan.next();
	}
	private void print(String str) {
		System.out.println(str);
	}
}
