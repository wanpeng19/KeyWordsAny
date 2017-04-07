package javafx.scene.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

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

	public static int nextTextFiled_Y = 120 + Y_INTERVAL;

	public static final int nextLabel_X = 50;
	public static int nextLabel_Y = 120 + Y_INTERVAL;
	
	public static final String KEY_WORDS_GROUP_CH = "关键词组";
	
	@FXML
	AnchorPane myPane;

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

		if (!flag) {
			System.out.println("未选择文件");
			label_common.setText("请先选择文件");
			return;
		}
		label_common.setText("正在分析");
		List<String> andSet1 = new ArrayList<String>();
		List<String> andSet2 = new ArrayList<String>();
		List<String> andSet3 = new ArrayList<String>();

		System.out.println("start");
		// 获得输入
		if (StringUtils.isNoneBlank(key_1_1.getText())) {
			andSet1.add(key_1_1.getText());
		}
		if (StringUtils.isNoneBlank(key_1_2.getText())) {
			andSet1.add(key_1_2.getText());
		}
		if (StringUtils.isNoneBlank(key_1_3.getText())) {
			andSet1.add(key_1_3.getText());
		}
		if (StringUtils.isNoneBlank(key_2_1.getText())) {
			andSet2.add(key_2_1.getText());
		}
		if (StringUtils.isNoneBlank(key_2_2.getText())) {
			andSet2.add(key_2_2.getText());
		}
		if (StringUtils.isNoneBlank(key_2_3.getText())) {
			andSet2.add(key_2_3.getText());
		}
		if (StringUtils.isNoneBlank(key_3_1.getText())) {
			andSet3.add(key_3_1.getText());
		}
		if (StringUtils.isNoneBlank(key_3_2.getText())) {
			andSet3.add(key_3_2.getText());
		}
		if (StringUtils.isNoneBlank(key_3_3.getText())) {
			andSet3.add(key_3_3.getText());
		}
		System.out.println("andSet1=" + andSet1);
		System.out.println("andSet2=" + andSet2);
		System.out.println("andSet3=" + andSet3);

		// 开始过滤
		// 过滤关键词组1
		Set<String> result1 = new HashSet<String>();
		Set<String> result2 = new HashSet<String>();
		Set<String> result3 = new HashSet<String>();
		for (String keywrod : keywords) {
			List<String> keyWrodList = Arrays.asList(keywrod.split(" "));
			if (andSet1.size() > 0 && keyWrodList.containsAll(andSet1)) {
				result1.add(keywrod);
			}
			if (andSet2.size() > 0 && keyWrodList.containsAll(andSet2)) {
				result2.add(keywrod);
			}
			if (andSet3.size() > 0 && keyWrodList.containsAll(andSet3)) {
				result3.add(keywrod);
			}
		}
		result1.addAll(result2);
		result1.addAll(result3);
		System.out.println("过滤后结果为：" + result1);

		// 写入文件
		String path = file.getAbsolutePath();
		System.out.println(path);
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
		String resultPath = path.replace(".xlsx", "_result_" + sdf.format(new Date()) + ".txt");
		System.out.println(resultPath);
		FileUtils.writeLines(new File(resultPath), result1);
		btn_2.setText("开始分析");
		label_common.setText("分析结束，导出结果为" + resultPath);
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
		Label nextLabel = new Label(KEY_WORDS_GROUP_CH + (++keywordNum));
		nextLabel.setLayoutX(nextLabel_X);
		nextLabel.setLayoutY(nextLabel_Y);
		myPane.getChildren().add(nextLabel);
		
		//设置下次的坐标
		
		nextLabel_Y += Y_INTERVAL;
		
	}
}
