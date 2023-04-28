package anu.capstone.showyourthings.service;

import anu.capstone.showyourthings.entity.BarcodeInfo;
import anu.capstone.showyourthings.repository.BarcodeRepositoryJPA;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Service
public class BarcodeService {
    private final BarcodeRepositoryJPA repository;
    final String ori_url = "http://www.koreannet.or.kr/home/hpisSrchGtin.gs1?gtin=";

    @Autowired
    public BarcodeService(BarcodeRepositoryJPA repository) {
        this.repository = repository;
    }

    public String checkCache(String barcode) {
        Optional<BarcodeInfo> cache = repository.findById(barcode);
        if (cache.isPresent()) {
            return cache.get().getProduct();
        }
        return "not found";
    }

    public String getDataFromAPIByBarcode(String barcode) {
//        API_result result = new API_result();

        String check = checkCache(barcode);
        if(!check.equals("not found")) return check;

        String url = ori_url + barcode;
        Connection connection = Jsoup.connect(url);

        Document document = null;
        try {
            document = connection.get();
            //url의 내용을 HTML Document 객체로 가져온다.
            //https://jsoup.org/apidocs/org/jsoup/nodes/Document.html 참고
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(document.getElementsByClass("noresult").size() == 0) {
            Element title = document.getElementsByClass("productTit").get(0);
            String result = title.text().replace(barcode+"", "");
            repository.save(new BarcodeInfo(barcode, result));
            return result;
        }
        else {
            return "not found";
        }
    }

    public String testBarcode(String barcode) {
        String check = checkCache(barcode);
        if(!check.equals("not found")) return check;
        String result = null;
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\lab330\\Desktop\\Project\\chromedriver.exe");
        String url = "https://www.consumer.go.kr/user/ftc/consumer/goodsinfo/57/selectGoodsInfoList.do";
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Users\\lab330\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");

//        options.addArguments("--no-sandbox"); // Bypass OS security model
//        options.addArguments("start-maximized"); // open Browser in maximized mode
//        options.addArguments("disable-infobars"); // disabling infobars
//        options.addArguments("--ignore-certificate-errors"); // disabling extensions
//        options.addArguments("--single-process");
//        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
//        options.addArguments("--disable-gpu");			//gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음

        WebDriver driver = new ChromeDriver(options);
        try {
            result = getDataList(driver, url, barcode);

        } catch (InterruptedException e) {
            e.printStackTrace();
            driver.quit();
        }
        driver.quit();
        return result;
    }

    private String getDataList(WebDriver driver, String url, String barcode) throws InterruptedException {
        driver.get(url);    //브라우저에서 url로 이동한다.
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.id("column2"))
                //cssSelector로 선택한 부분이 존재할때까지 기다려라
        );//브라우저 로딩될때까지 잠시 기다린다.

        //List<WebElement> elements = driver.findElements(By.cssSelector("#sentence-example-list .sentence-list li"));
        WebElement raido = driver.findElement(By.id("column2"));
        raido.click();

        WebElement input = driver.findElement(By.id("search2"));
        input.sendKeys(barcode);

        WebElement submit = driver.findElement(By.className("btn_submit"));
        submit.click();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.className("satis_choice_area"))
                //cssSelector로 선택한 부분이 존재할때까지 기다려라
        );//브라우저 로딩될때까지 잠시 기다린다.

        String product;
        try {
            driver.findElement(By.id("goodsList"));
            return "not found";
        } catch (Exception e) {
            product = driver.findElements(By.tagName("strong")).get(1).getText();
        }
        repository.save(new BarcodeInfo(barcode, product));
        return product;
    }
}
