package javafx.scene.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import fpx.grs.wh.util.FxQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.util.KeyWordsXlsReader;
import javafx.stage.FileChooser;

public class MyControl implements Initializable {
	public static final int Y_INTERVAL = 35;

	public static final int nextTextFiled_1_X = 140;
	public static final int nextTextFiled_2_X = 275;
	public static final int nextTextFiled_3_X = 410;

	public static int initTextFiled_Y = 50 ;
	public static int nextTextFiled_Y = 120 + Y_INTERVAL;
	public static int textFiledLength = 110;
	

	public static final int nextLabel_X = 50;
	public static int nextLabel_Y = 120 + Y_INTERVAL;
	
	public static final String KEY_WORDS_GROUP_CH = "关键词组";
	
	@FXML
	AnchorPane myPane1;
	@FXML
	AnchorPane myPane2;
	@FXML
	AnchorPane myPane3;
	@FXML
	AnchorPane myPane4;
	@FXML
	AnchorPane myPane5;
	
	@FXML
	Button btn_addKey;
	
	@FXML
	TextField key_1_1;
	@FXML
	TextField key_1_2;
	@FXML
	TextField key_1_3;
	@FXML
	TextField key_2_1;
	@FXML
	TextField key_2_2;
	@FXML
	TextField key_2_3;
	@FXML
	TextField key_3_1;
	@FXML
	TextField key_3_2;
	@FXML
	TextField key_3_3;
	@FXML
	Button btn_1;
	@FXML
	Button btn_2;
	@FXML
	Label label_common;

	File file;
	List<String> keywords;

	Boolean flag = false;
	
	int keywordNum = 3;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	}

	@FXML
	private void start(ActionEvent event) throws IOException {
		
		
		Set<List<String>> orSet = new HashSet<List<String>>();
		
		Set<String> result1 = new HashSet<String>();
		Set<String> resultReplaced = new HashSet<String>();
		getInputKeywords(orSet);
        System.out.println("输入关键词组=" + orSet);
		if (!flag) {
			System.out.println("未选择文件");
			label_common.setText("请先选择文件");
			return;
		}
		label_common.setText("正在分析");

//		for (String keywrod : keywords) {
//			String replacedStr = keywrod;
//			List<String> keyWrodList = Arrays.asList(keywrod.split(" "));
//			for(List<String> andList :  orSet){
//				if (andList.size() > 0 && keyWrodList.containsAll(andList)) {
//					result1.add(keywrod);
//					for(String tem : andList){
//						replacedStr = replacedStr.replace(tem, "+"+tem);
//					}
//					resultReplaced.add(replacedStr);
//				}
//			}
//		}
		
		for(List<String> andList :  orSet){
			for (String keywrod : keywords) {
				String replacedStr = keywrod;
				if (andList.size() > 0 && partMatch(keywrod, andList)) {
					result1.add(keywrod);
					for(String tem : andList){
						replacedStr = replacedStr.replace(tem, "+"+tem);
					}
					resultReplaced.add(StringUtils.join(andList," ")+ ", "+replacedStr);
				}
			}
		}
		

		System.out.println("过滤后结果为：" + result1);
		System.out.println("过滤并转换后结果为：" + resultReplaced);
		Collection<String> leftResult = CollectionUtils.subtract(keywords, result1);
		System.out.println("被过滤之后剩下的结果为："+  leftResult);
		
		
		// 写入文件
		String path = file.getAbsolutePath();
		System.out.println(path);
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
		String resultPath = path.replace(".xlsx", "_result_" + sdf.format(new Date()) + ".csv");
		System.out.println(resultPath);
		FileUtils.writeLines(new File(resultPath), resultReplaced);
		btn_2.setText("开始分析");
		label_common.setText("分析结束，导出结果为\n" + resultPath);
		
		String resultLeftPath = path.replace(".xlsx", "_left_result_" + sdf.format(new Date()) + ".csv");
		FileUtils.writeLines(new File(resultLeftPath), leftResult);
	}

	private void getInputKeywords(Set<List<String>> orSet) {
		for(int i = 1; i <= keywordNum; i++ ){
			String selector1 = "#key_"+i+"_"+"1";
			String selector2 = "#key_"+i+"_"+"2";
			String selector3 = "#key_"+i+"_"+"3";
			TextField field1 = FxQuery.$$(selector1, TextField.class);
			TextField field2 = FxQuery.$$(selector2, TextField.class);
			TextField field3 = FxQuery.$$(selector3, TextField.class);
			List<String> andList = new ArrayList<String>();
			
			String text1 = field1.getText();
			if(StringUtils.isNoneBlank(text1)) andList.add(text1);
	
			String text2 = field2.getText();
			if(StringUtils.isNoneBlank(text2)) andList.add(text2);
			
			String text3 = field3.getText();
			if(StringUtils.isNoneBlank(text3)) andList.add(text3);
			
			orSet.add(andList);
		}
	}

	private boolean partMatch(String keywrod, List<String> andList) {
        boolean ismatch = true;
        for (String and :andList){
        	ismatch = ismatch && StringUtils.indexOf(keywrod, and) >= 0;
        }
		return ismatch;
	}

	@FXML
	private void chooseFile() throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		file = fileChooser.showOpenDialog(null);
		keywords = KeyWordsXlsReader.readConfigXlsx(file, 0);
		System.out.println(keywords);
		flag = true;
		label_common.setText("已经选择文件，可以开始分析");
	}

	@FXML
	private void addKeyWord() {
		++keywordNum;
		if(keywordNum <= 10){
			Label nextLabel = new Label(KEY_WORDS_GROUP_CH + keywordNum);
			nextLabel.setLayoutX(nextLabel_X);
			nextLabel.setLayoutY(nextLabel_Y);
			myPane1.getChildren().add(nextLabel);
			
			TextField nextTextField1 = new TextField();
			nextTextField1.setMaxWidth(textFiledLength);
			nextTextField1.setLayoutX(nextTextFiled_1_X);
			nextTextField1.setLayoutY(nextTextFiled_Y);
			nextTextField1.setId("key_"+keywordNum+"_"+"1");
			myPane1.getChildren().add(nextTextField1);
			
			TextField nextTextField2 = new TextField();
			nextTextField2.setMaxWidth(textFiledLength);
			nextTextField2.setLayoutX(nextTextFiled_2_X);
			nextTextField2.setLayoutY(nextTextFiled_Y);
			nextTextField2.setId("key_"+keywordNum+"_"+"2");
			myPane1.getChildren().add(nextTextField2);
			
			TextField nextTextField3 = new TextField();
			nextTextField3.setMaxWidth(textFiledLength);
			nextTextField3.setLayoutX(nextTextFiled_3_X);
			nextTextField3.setLayoutY(nextTextFiled_Y);
			nextTextField3.setId("key_"+keywordNum+"_"+"3");
			myPane1.getChildren().add(nextTextField3);
			
			//设置下次的坐标
			nextLabel_Y += Y_INTERVAL;
			nextTextFiled_Y += Y_INTERVAL;
		} else if(keywordNum > 10 && keywordNum <= 20){
			if(keywordNum == 11){
				nextLabel_Y = initTextFiled_Y;
				nextTextFiled_Y = initTextFiled_Y;
			   }
				Label nextLabel = new Label(KEY_WORDS_GROUP_CH + keywordNum);
				nextLabel.setLayoutX(nextLabel_X);
				nextLabel.setLayoutY(nextLabel_Y);
				myPane2.getChildren().add(nextLabel);
				
				TextField nextTextField1 = new TextField();
				nextTextField1.setMaxWidth(textFiledLength);
				nextTextField1.setLayoutX(nextTextFiled_1_X);
				nextTextField1.setLayoutY(nextTextFiled_Y);
				nextTextField1.setId("key_"+keywordNum+"_"+"1");
				myPane2.getChildren().add(nextTextField1);
				
				TextField nextTextField2 = new TextField();
				nextTextField2.setMaxWidth(textFiledLength);
				nextTextField2.setLayoutX(nextTextFiled_2_X);
				nextTextField2.setLayoutY(nextTextFiled_Y);
				nextTextField2.setId("key_"+keywordNum+"_"+"2");
				myPane2.getChildren().add(nextTextField2);
				
				TextField nextTextField3 = new TextField();
				nextTextField3.setMaxWidth(textFiledLength);
				nextTextField3.setLayoutX(nextTextFiled_3_X);
				nextTextField3.setLayoutY(nextTextFiled_Y);
				nextTextField3.setId("key_"+keywordNum+"_"+"3");
				myPane2.getChildren().add(nextTextField3);
			
				nextLabel_Y += Y_INTERVAL;
				nextTextFiled_Y += Y_INTERVAL;
				
		} else if(keywordNum > 20 && keywordNum <= 30){
			if(keywordNum == 21){
				nextLabel_Y = initTextFiled_Y;
				nextTextFiled_Y = initTextFiled_Y;
			   }
				Label nextLabel = new Label(KEY_WORDS_GROUP_CH + keywordNum);
				nextLabel.setLayoutX(nextLabel_X);
				nextLabel.setLayoutY(nextLabel_Y);
				myPane3.getChildren().add(nextLabel);
				
				TextField nextTextField1 = new TextField();
				nextTextField1.setMaxWidth(textFiledLength);
				nextTextField1.setLayoutX(nextTextFiled_1_X);
				nextTextField1.setLayoutY(nextTextFiled_Y);
				nextTextField1.setId("key_"+keywordNum+"_"+"1");
				myPane3.getChildren().add(nextTextField1);
				
				TextField nextTextField2 = new TextField();
				nextTextField2.setMaxWidth(textFiledLength);
				nextTextField2.setLayoutX(nextTextFiled_2_X);
				nextTextField2.setLayoutY(nextTextFiled_Y);
				nextTextField2.setId("key_"+keywordNum+"_"+"2");
				myPane3.getChildren().add(nextTextField2);
				
				TextField nextTextField3 = new TextField();
				nextTextField3.setMaxWidth(textFiledLength);
				nextTextField3.setLayoutX(nextTextFiled_3_X);
				nextTextField3.setLayoutY(nextTextFiled_Y);
				nextTextField3.setId("key_"+keywordNum+"_"+"3");
				myPane3.getChildren().add(nextTextField3);
			
				nextLabel_Y += Y_INTERVAL;
				nextTextFiled_Y += Y_INTERVAL;
		} else if(keywordNum > 30 && keywordNum <= 40){
			if(keywordNum == 31){
				nextLabel_Y = initTextFiled_Y;
				nextTextFiled_Y = initTextFiled_Y;
			   }
				Label nextLabel = new Label(KEY_WORDS_GROUP_CH + keywordNum);
				nextLabel.setLayoutX(nextLabel_X);
				nextLabel.setLayoutY(nextLabel_Y);
				myPane4.getChildren().add(nextLabel);
				
				TextField nextTextField1 = new TextField();
				nextTextField1.setMaxWidth(textFiledLength);
				nextTextField1.setLayoutX(nextTextFiled_1_X);
				nextTextField1.setLayoutY(nextTextFiled_Y);
				nextTextField1.setId("key_"+keywordNum+"_"+"1");
				myPane4.getChildren().add(nextTextField1);
				
				TextField nextTextField2 = new TextField();
				nextTextField2.setMaxWidth(textFiledLength);
				nextTextField2.setLayoutX(nextTextFiled_2_X);
				nextTextField2.setLayoutY(nextTextFiled_Y);
				nextTextField2.setId("key_"+keywordNum+"_"+"2");
				myPane4.getChildren().add(nextTextField2);
				
				TextField nextTextField3 = new TextField();
				nextTextField3.setMaxWidth(textFiledLength);
				nextTextField3.setLayoutX(nextTextFiled_3_X);
				nextTextField3.setLayoutY(nextTextFiled_Y);
				nextTextField3.setId("key_"+keywordNum+"_"+"3");
				myPane4.getChildren().add(nextTextField3);
			
				nextLabel_Y += Y_INTERVAL;
				nextTextFiled_Y += Y_INTERVAL;
		} else if(keywordNum > 40 && keywordNum <= 50){
			if(keywordNum == 41){
				nextLabel_Y = initTextFiled_Y;
				nextTextFiled_Y = initTextFiled_Y;
			   }
				Label nextLabel = new Label(KEY_WORDS_GROUP_CH + keywordNum);
				nextLabel.setLayoutX(nextLabel_X);
				nextLabel.setLayoutY(nextLabel_Y);
				myPane5.getChildren().add(nextLabel);
				
				TextField nextTextField1 = new TextField();
				nextTextField1.setMaxWidth(textFiledLength);
				nextTextField1.setLayoutX(nextTextFiled_1_X);
				nextTextField1.setLayoutY(nextTextFiled_Y);
				nextTextField1.setId("key_"+keywordNum+"_"+"1");
				myPane5.getChildren().add(nextTextField1);
				
				TextField nextTextField2 = new TextField();
				nextTextField2.setMaxWidth(textFiledLength);
				nextTextField2.setLayoutX(nextTextFiled_2_X);
				nextTextField2.setLayoutY(nextTextFiled_Y);
				nextTextField2.setId("key_"+keywordNum+"_"+"2");
				myPane5.getChildren().add(nextTextField2);
				
				TextField nextTextField3 = new TextField();
				nextTextField3.setMaxWidth(textFiledLength);
				nextTextField3.setLayoutX(nextTextFiled_3_X);
				nextTextField3.setLayoutY(nextTextFiled_Y);
				nextTextField3.setId("key_"+keywordNum+"_"+"3");
				myPane5.getChildren().add(nextTextField3);
			
				nextLabel_Y += Y_INTERVAL;
				nextTextFiled_Y += Y_INTERVAL;
		}
	}
	
	private void saveKeyWord() {

	}
}
