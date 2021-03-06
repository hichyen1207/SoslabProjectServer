package com.server.project.fix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.server.project.task.database.TaskToVideo;

public class AddVideo {
	public static void main(String[] args) throws Exception {
		AddVideo addVideo = new AddVideo();
		addVideo.fixVideo();
	}

	public void fix() throws Exception {
		TaskToVideo taskToVideo = new TaskToVideo();
		System.setProperty("webdriver.chrome.driver",
				"/Users/Hao/Documents/Java/SoslabProjectHouseParser/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.youtube.com/feed/trending");
		List<WebElement> list = driver.findElement(By.id("browse-items-primary"))
				.findElement(By.className("feed-item-dismissable")).findElement(By.tagName("ul"))
				.findElements(By.className("expanded-shelf-content-item-wrapper"));
		for (int i = 0; i <= 40; i++) {
			String youtubeId = list.get(i).findElement(By.tagName("a")).getAttribute("href");
			youtubeId = youtubeId.substring(32);
			taskToVideo.toVideo(String.valueOf(i + 110), youtubeId, "aaa", "aAa", "aaa", "aaa", "aaa");
		}
		driver.close();

	}
	
	public void fixVideo() throws Exception{
		Class.forName("org.postgresql.Driver").newInstance();

		String url = "jdbc:postgresql://140.119.19.33:5432/project";
		Connection con = DriverManager.getConnection(url, "postgres", "093622"); // 帳號密碼
		Statement selectST = con.createStatement();
		
		String selectSQL = "select * from video;";
		ResultSet selectRS = selectST.executeQuery(selectSQL);
		while(selectRS.next()){	
			String id = selectRS.getString("id");						
			String shop = selectRS.getString("shop");			
			if(shop == null){
				System.out.println("haha is null");
				Statement updateST = con.createStatement();
				String updateSQL = "update video set shop='餐廳,加油站,超市', weather='晴天', facility='學校, 警察局' where id=" + id + ";";
				updateST.executeUpdate(updateSQL);
				updateST.close();				
			} else if (shop.equals("")) {
				System.out.println("aaaa is empty");				
				Statement updateST = con.createStatement();
				String updateSQL = "update video set shop='餐廳,加油站,超市', weather='晴天', facility='學校, 警察局' where id=" + id + ";";
				updateST.executeUpdate(updateSQL);
				updateST.close();		
			}
		}
		selectRS.close();
		selectST.close();
		con.close();
	}
}
